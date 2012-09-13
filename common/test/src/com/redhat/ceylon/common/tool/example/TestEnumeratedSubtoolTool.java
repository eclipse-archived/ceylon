package com.redhat.ceylon.common.tool.example;

import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Subtool;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolModel;

public class TestEnumeratedSubtoolTool implements Tool {
    
    public enum EnumeratedTool implements Tool {
        foo {
            public void run() {
                // a run() specific to foo
            }
        },
        bar {
            
        },
        baz;
        
        public void run() {
            // the default run(), inherited by bar and baz    
        }
    }

    private Tool action;
    
    //@Argument(multiplicity="1", argumentName="action", order=1)
    @Subtool(argumentName="action")
    public void setAction(Tool tool) {
        this.action = tool;
    }

    public Tool getAction() {
        return action;
    }

    public void run() throws Exception {
        
    }
    
}
