package com.redhat.ceylon.tools;

/**
 * A plug-in to the {@code ceylon} command.
 * 
 * A plug-in is a JavaBean which is instantiated (by a {@link PluginLoader}) 
 * in response to the {@code ceylon} program being executed with that 
 * tool name, or for introspection by other tools.
 * 
 * If the tool is going to be executed it is first configured by the 
 * {@link PluginFactory} according to the command line arguments given. 
 * These are configured on the tool bean 
 * according to the annotations on its JavaBean setters. Once the properties 
 * have been set any @PostConstruct methods are invoked before the tool is
 * {@link #run()}.
 */
public interface Plugin {

    public int run();
    
}
