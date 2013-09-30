package com.redhat.ceylon.compiler.java.test.fordebug;

public interface Tracer extends AutoCloseable {
    
    /**
     * Enumerates what should happen after a handler has handled an event
     */
    public enum HandlerResult {
        /** The {@link #resume()} method continues its loop; the VM will be resumed */
        RESUME,
        /** The {@link #resume()} method returns; the VM is suspended */
        SUSPEND
    }

    @Override
    public abstract void close() throws Exception;

    /** Starts the VM and awaits receipt of a VMStartEvent */
    public abstract void start() throws Exception;

    public abstract void resume() throws Exception;
    
    public boolean isVmAlive();
    
    public abstract String getTrace();

    

    /** Creates and returns a method entry breakpoint */
    public abstract MethodEntry methodEntry();
    
    /** Represents a method entry break point */
    public interface MethodEntry {
                
        /** Add a class name filter for this break point */
        public MethodEntry classFilter(String className);
        
        /** Sets the method name filter for this break point */
        public MethodEntry methodFilter(String methodName);
        
        /** Enables this breakpoint */
        public MethodEntry enable();
        
        /** Disables this breakpoint */
        public MethodEntry disable();
        
        /** Sets the result of this breakpoint */
        public MethodEntry result(HandlerResult result);
        
    }
    
    /** Creates and returns a method exit breakpoint */
    public abstract MethodExit methodExit();
    
    /** Represents a method exit break point */
    public interface MethodExit {
        
        /** Add a class name filter for this break point */
        public MethodExit classFilter(String className);
        
        /** Sets the method name filter for this break point */
        public MethodExit methodFilter(String methodName);
        
        public MethodExit enable();
        
        public MethodExit disable();
        
        /** Sets the result of this breakpoint */
        public MethodExit result(HandlerResult result);
        
    }


    /** Creates and returns a step breakpoint */
    public abstract Step step();
    
    public interface Step {
        
        public abstract Step within(String within);
        
        public abstract Step enable();
        
        public abstract Step disable();
        
        public abstract Step result(HandlerResult result);
        
        public abstract Step log();
        
    }


}