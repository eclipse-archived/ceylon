package com.redhat.ceylon.ant;

public class CeyloncJs extends CeylonCompileJsAntTask {
    @Override
    public void execute() {
        Deprecation.message(this, CeylonCompileJsAntTask.class);
        super.execute();
    }
}