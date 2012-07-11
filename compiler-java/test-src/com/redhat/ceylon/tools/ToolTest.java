package com.redhat.ceylon.tools;

public abstract class ToolTest {

    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final PluginFactory tb = new PluginFactory(apf);
    protected final PluginLoader tl = new PluginLoader(apf);

}
