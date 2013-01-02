package com.redhat.ceylon.ant;

public class Ceylon extends CeylonRunAntTask {
    @Override
    public void execute() {
        Deprecation.message(this, CeylonRunAntTask.class);
        super.execute();
    }
}