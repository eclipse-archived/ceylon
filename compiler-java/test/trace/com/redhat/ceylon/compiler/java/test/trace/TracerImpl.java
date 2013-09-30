package com.redhat.ceylon.compiler.java.test.trace;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.java.test.fordebug.Tracer;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.StepRequest;

public class TracerImpl implements Tracer {
    private final String mainClass;
    private String classPath;
    VirtualMachine vm;
    InputStream err;
    InputStream out;
    EventRequestManager rm;
    EventQueue eq;
    public Map<EventRequest, Handler<?>> map = new HashMap<EventRequest, Handler<?>>(1);
    StringBuffer trace = new StringBuffer();
    EventSet events;

    /** Creates a new tracer for executing the given main class with the given class path */
    public TracerImpl(String mainClass, String classPath) {
        this.mainClass = mainClass;
        this.classPath = classPath;
    }
    
    @Override
    public void close() throws Exception {
        if (err != null) {
            err.close();
        }
        if (out != null) {
            out.close();
        }
        if (vm != null) {
            vm.dispose();
        }
    }
    
    /** Echoes the bytes read from the input stream to the given output stream */
    void echo(InputStream in, PrintStream out) throws IOException {
        byte[] buf = new byte[1024];
        int avail = in.available();
        while (avail > 0) {
            int read = in.read(buf, 0, Math.min(avail, buf.length));
            out.print(new String(buf, 0, read));
            avail = in.available();
        }
    }

    ThreadReference findMainThread(VirtualMachine vm) {
        ThreadReference main = null;
        for (ThreadReference thread : vm.allThreads()) {
            if ("main".equals(thread.name())) {
                main = thread;
                break;
            }
        }
        return main;
    }
    
    void log(String event) {
        trace.append(event).append('\n');
    }
    
    public void start() throws Exception {
        VirtualMachineManager vmm = com.sun.jdi.Bootstrap.virtualMachineManager();
        LaunchingConnector conn = vmm.defaultConnector();
        Map<String, Argument> defaultArguments = conn.defaultArguments();
        defaultArguments.get("main").setValue(mainClass);
        defaultArguments.get("options").setValue("-cp " + classPath);
        System.out.println(defaultArguments);
        vm = conn.launch(defaultArguments);
        err = vm.process().getErrorStream();
        out = vm.process().getInputStream();
        eq = vm.eventQueue();
        rm = vm.eventRequestManager();
        outer: while (true) {
            echo(err, System.err);
            echo(out, System.out);
            events = eq.remove();
            for (Event event : events) {
                if (event instanceof VMStartEvent) {
                    System.out.println(event);
                    break outer;
                } else if (event instanceof VMDisconnectEvent
                        || event instanceof VMDeathEvent) {
                    System.out.println(event);
                    vm = null;
                    rm = null;
                    eq = null;
                    break outer;
                }
            }
            events.resume();
        }
        echo(err, System.err);
        echo(out, System.out);
    }
    
    public void resume() throws Exception {
        outer: while (true) {
            echo(err, System.err);
            echo(out, System.out);
            events.resume();
            events = eq.remove();
            for (Event event : events) {
                Handler<?> handler = map.get(event.request());
                if (handler != null) {
                    switch (((Handler)handler).handle(event)) {
                    case RESUME:
                        break;
                    case SUSPEND:
                        return;
                    }
                }
                if (event instanceof VMDisconnectEvent
                        || event instanceof VMDeathEvent) {
                    System.out.println(event);
                    vm = null;
                    rm = null;
                    eq = null;
                    return;
                }
            }
        }
    }

    public boolean isVmAlive() {
        return vm != null;
    }
    
    public String getTrace() {
        return trace.toString();
    }
    
    /** Creates and returns a method entry breakpoint */
    public MethodEntryImpl methodEntry() {
        return new MethodEntryImpl();
    }
    public interface Handler<E> {
        public HandlerResult handle(E event) throws Exception;
    }

    public interface EventLogger<E> {
        void log(E event) throws Exception;
    }
    
    /** Represents a method entry break point */
    public class MethodEntryImpl implements MethodEntry,Handler<MethodEntryEvent> {
        private final MethodEntryRequest request;
        private String methodName;
        private HandlerResult result = HandlerResult.RESUME;

        MethodEntryImpl() {
            this.request = rm.createMethodEntryRequest();
        }
        
        /** Add a class name filter for this break point */
        public MethodEntry classFilter(String className) {
            request.addClassFilter(className);
            return this;
        }
        
        /** Sets the method name filter for this break point */
        public MethodEntry methodFilter(String methodName) {
            this.methodName = methodName;
            return this;
        }
        
        /** Enables this breakpoint */
        public MethodEntry enable() {
            if (vm == null) {
                throw new RuntimeException("VM disconnected or died");
            }
            map.put(request, this);
            this.request.enable();
            return this;
        }
        
        /** Disables this breakpoint */
        public MethodEntry disable() {
            if (vm == null) {
                throw new RuntimeException("VM disconnected or died");
            }
            this.request.disable();
            map.remove(request);
            return this;
        }
        
        /** Sets the result of this breakpoint */
        public MethodEntry result(HandlerResult result) {
            this.result = result;
            return this;
        }
        
        @Override
        public HandlerResult handle(MethodEntryEvent event) throws Exception {
            if (methodName != null 
                    && !methodName.equals(event.method().name())) {
                return HandlerResult.RESUME;
            }
            
            return result;
        }
    }
    
    /** Creates and returns a method exit breakpoint */
    public MethodExit methodExit() {
        return new MethodExitImpl();
    }
    
    /** Represents a method exit break point */
    public class MethodExitImpl implements MethodExit, Handler<MethodExitEvent> {
        private final MethodExitRequest request;
        private String methodName;
        private HandlerResult result = HandlerResult.RESUME;

        MethodExitImpl() {
            this.request = rm.createMethodExitRequest();
        }
        
        /** Add a class name filter for this break point */
        public MethodExit classFilter(String className) {
            request.addClassFilter(className);
            return this;
        }
        
        /** Sets the method name filter for this break point */
        public MethodExit methodFilter(String methodName) {
            this.methodName = methodName;
            return this;
        }
        
        public MethodExit enable() {
            if (vm == null) {
                throw new RuntimeException("VM disconnected or died");
            }
            map.put(request, this);
            this.request.enable();
            return this;
        }
        
        public MethodExit disable() {
            if (vm == null) {
                throw new RuntimeException("VM disconnected or died");
            }
            this.request.disable();
            map.remove(request);
            return this;
        }
        
        /** Sets the result of this breakpoint */
        public MethodExit result(HandlerResult result) {
            this.result = result;
            return this;
        }
        
        @Override
        public HandlerResult handle(MethodExitEvent event) throws Exception {
            if (methodName != null 
                    && !methodName.equals(event.method().name())) {
                return HandlerResult.RESUME;
            }
            
            return result;
        }
        
    }
    
    /** Creates and returns a step breakpoint */
    public Step step() {
        return new StepImpl();
    }
    
    public class StepImpl implements Step, Handler<StepEvent> {

        private String within = null;
        private final StepRequest request;
        private HandlerResult result = HandlerResult.RESUME;
        private EventLogger<StepEvent> logger;
        
        StepImpl() {
            super();
            ThreadReference mainThread = findMainThread(vm);
            request = rm.createStepRequest(mainThread, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
        }
        
        public StepImpl within(String within) {
            this.within = within;
            return this;
        }
        
        public StepImpl enable() {
            if (vm == null) {
                throw new RuntimeException("VM disconnected or died");
            }
            map.put(request, this);
            request.enable();
            return this;
        }
        
        public StepImpl disable() {
            if (vm == null) {
                throw new RuntimeException("VM disconnected or died");
            }
            this.request.disable();
            map.remove(request);
            return this;
        }
        
        public StepImpl result(HandlerResult result) {
            this.result = result;
            return this;
        }
        
        public StepImpl log() {
            this.logger = STEP_EVENT_LOGGER;
            return this;
        }
        
        public HandlerResult handle(StepEvent event) throws Exception {
            Location location = event.location();
            if (this.within != null 
                    && !this.within.equals(location.sourceName())) {
                return HandlerResult.RESUME;
            }
            
            if (logger != null) {
                logger.log(event);
            }
            return result;
        }
        
    }
    
    EventLogger<StepEvent> STEP_EVENT_LOGGER = new EventLogger<StepEvent>() {
        @Override
        public void log(StepEvent event) throws Exception {
            Location location = event.location();
            Method m = location.method();
            String atns = m.argumentTypeNames().toString();
            String aa = atns.substring(1, atns.length()-1);
            String methodDesc = m.name()+"(" + aa + ")";
            if (location.lineNumber() == -1) {
                TracerImpl.this.log(location.sourceName() + ":?:" + methodDesc);
            } else {
                TracerImpl.this.log(location.sourceName() + ":" + location.lineNumber() + ":" + methodDesc);
            }
        }
        
    };
    
}