package com.redhat.ceylon.ant;

public class CeylonJs extends CeylonRunJsAntTask {
    @Override
    public void execute() {
        Deprecation.message(this, CeylonRunJsAntTask.class);
        super.execute();
    }
}