package com.redhat.ceylon.ant;

public class Ceylonc extends CeylonCompileAntTask {
    @Override
    public void execute() {
        Deprecation.message(this, CeylonCompileAntTask.class);
        super.execute();
    }
}
