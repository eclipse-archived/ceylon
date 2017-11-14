/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.eclipse.ceylon.compiler.java.test.fordebug;

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