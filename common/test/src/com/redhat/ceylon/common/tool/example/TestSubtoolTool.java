package com.redhat.ceylon.common.tool.example;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.common.tool.MapToolLoader;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.ParserFactory;
import com.redhat.ceylon.common.tool.Subtool;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolArgumentParser;

public class TestSubtoolTool implements Tool {

    private final static Map<Class<? extends Tool>, String> map = new HashMap<Class<? extends Tool>, String>();
    static {
        map.put(ArgumentTool1.class, "1");
        map.put(ArgumentTool2.class, "2");
    }
    
    public static final class Parser 
        extends ToolArgumentParser {
        public Parser() {
            super(new MapToolLoader(map));
        }
    }

    public static class ArgumentTool1 implements Tool {
        private boolean foo;
        @Option
        public void setFoo(boolean foo) {
            this.foo = foo;
        }
        public boolean getFoo() {
            return foo;
        }
        @Override
        public void run() throws Exception {
            
        }
    }
    
    public static class ArgumentTool2 implements Tool {
        private boolean bar;
        @Option
        public void setBar(boolean bar) {
            this.bar = bar;
        }
        public boolean getBar() {
            return bar;
        }
        @Override
        public void run() throws Exception {
            
        }
    }

    private Tool action;
    
    //@Argument(multiplicity="1", argumentName="action", order=1)
    @ParserFactory(TestSubtoolTool.Parser.class)
    @Subtool(argumentName="action", order=1)
    /* Must be @Argument (not @OptionArgument) (or does is it a peer of @Argument
     * Must be the *last* @Argument by order -- all following arguments are 
     * implictly consumed by the subtools
     * Must nominate a ToolLoader somehow
     *   (will need to provide some base classes to make that easier
     *    e.g. EnumToolLoader)
     * What about argument rearrangement? In theory it shouldn't be needed should it?
     * What about the role of -- with subtools?
     */
    public void setAction(Tool tool) {
        this.action = tool;
    }

    public Tool getAction() {
        return action;
    }

    public void run() throws Exception {
        // XXX This way of doing things means that its the super tool's job to 
        // configure its subtool. 
        // We need an annotation which allows the ToolFactory to configure 
        // the subtool using all the arguments it has and inject that already 
        // configured into the super tool
        
    }
    
}
