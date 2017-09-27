package org.eclipse.ceylon.model.typechecker.model;

public interface Cancellable {
    boolean isCancelled();
    
    public static Cancellable ALWAYS_CANCELLED = new Cancellable() {
        @Override
        public boolean isCancelled() {
            return true;
        }
    };
}
