package com.redhat.ceylon.common.tool.example;

import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.Subtool;
import com.redhat.ceylon.common.tool.Tool;

public class TestSubtoolTool implements Tool {

    public static class Subtool1 implements Tool {
        private boolean foo;
        @Option
        public void setFoo(boolean foo) {
            this.foo = foo;
        }
        public boolean getFoo() {
            return foo;
        }
        @Override
        public void initialize() {
        }
        @Override
        public void run() throws Exception {
            
        }
    }
    
    public class Subtool2 implements Tool {
        private boolean bar;
        @Option
        public void setBar(boolean bar) {
            this.bar = bar;
        }
        public boolean getBar() {
            return bar;
        }
        @Override
        public void initialize() {
        }
        @Override
        public void run() throws Exception {
            
        }
    }

    private Tool action;
    
    @Subtool(argumentName="action", order=1,
            classes={Subtool1.class, Subtool2.class})
    public void setAction(Tool tool) {
        this.action = tool;
    }

    public Tool getAction() {
        return action;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void run() throws Exception {
        
    }
    
}
