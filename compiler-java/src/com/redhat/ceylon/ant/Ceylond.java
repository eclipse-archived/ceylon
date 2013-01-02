package com.redhat.ceylon.ant;

public class Ceylond extends CeylonDocAntTask {
    @Override
    public void execute() {
        Deprecation.message(this, CeylonDocAntTask.class);
        super.execute();
    }
}
