package com.redhat.ceylon.model.typechecker.model;

public class Canceler implements Cancellable {
    private boolean cancelled = false;
    public void cancel() {
        cancelled = true;
    }
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
