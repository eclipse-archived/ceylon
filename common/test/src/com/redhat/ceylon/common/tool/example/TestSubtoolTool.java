package com.redhat.ceylon.common.tool.example;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Tool;

@Parser(ArgumentP
public class TestSubtoolTool implements Tool {

    /*public static class OptionArgumentTool1 implements Tool {
        @Override
        public void run() throws Exception {
            
        }
    }
    
    public static class OptionArgumentTool2 implements Tool {
        @Override
        public void run() throws Exception {
            
        }
    }*/
    
    public static class ArgumentTool1 implements Tool {
        @Override
        public void run() throws Exception {
            
        }
    }
    
    public static class ArgumentTool2 implements Tool {
        @Override
        public void run() throws Exception {
            
        }
    }

    //private Tool optArg;
    private Tool arg;

    /*@OptionArgument
    public void setOptArg(Tool tool) {
        this.optArg = tool;
    }*/
    
    @Argument(multiplicity="1")
    public void setArg(Tool tool) {
        this.arg = tool;
    }
    
    public void run() {
        
    }
    
}
