package com.redhat.ceylon.common.tool;

/**
 * A plug-in to the {@code ceylon} command.
 * 
 * <h3>Tool Conventions</h3>
 * <p>As well as implementing this interface a tool must adhere to a number of 
 * conventions:</p>
 * <ul>
 * <li>The class must be concrete.</li>
 * <li>The class must have a public no argument constructor</li> 
 * <li>The class's name must begin with {@code Ceylon} end with {@code Tool}. 
 *     The tool name is derived from the class name by:
 *   <ol>
 *     <li>Removing the initial {@code Ceylon} trailing {@code Tool},</li>
 *     <li>Lowercasing the initial character</li>
 *     <li>Replacing every subsequent upper case character in the name with a 
 *     dash followed by the lowercased character</li>
 *   </ol>
 *     So, for example the class name {@code CeylonFooBarTool} implies the 
 *     tool name {@code foo-bar}
 * </li> 
 * </ol>
 * <p>In addition to those conventions, if a tool accepts command line options 
 * and/or arguments, those are represented by JavaBean setter methods one of 
 * the following annotations applied:</p>
 * <dl>
 * <dt>{@link Option}</dt>
 *   <dd>For {@linkplain  com.redhat.ceylon.common.tool pure options}</dd>
 * <dt>{@link OptionArgument}</dt>
 *   <dd>For {@linkplain  com.redhat.ceylon.common.tool option arguments}</dd>
 * <dt>{@link Argument}</dt>
 *   <dd>For {@linkplain  com.redhat.ceylon.common.tool arguments}</dd>
 * <dt>{@link Rest}</dt>
 *   <dd>For passing unconstrained options</dd>
 * </dl>
 * 
 * <p>Additional annotations provide for tool documentation:</p>
 * <dl>
 * <dt>{@link Summary}</dt>
 *   <dd>A one sentence summary of what the tool does.</dd>
 * <dt>{@link Description}</dt>
 *   <dd>Detailed description of a tool or its command line options 
 *   and arguments.</dd>
 * <dt>{@link RemainingSections}</dt>
 *   <dd>Additional documentation headings/sections</dd>
 * </dl>
 * 
 * <h3>Lifecycle</h3>
 * A plug-in is a JavaBean which is instantiated (by a {@link ToolLoader}) 
 * in response to the {@code ceylon} program being executed with that 
 * tool name, or for introspection by other tools.
 * 
 * If the tool is going to be executed it is first configured by the 
 * {@link ToolFactory} according to the command line arguments given. 
 * These are configured on the tool bean 
 * according to the annotations on its JavaBean setters. Once the properties 
 * have been set the {@link #initialize()} method is invoked before the tool is
 * {@link #run()}.
 */
public interface Tool {

    /**
     * Initializes the tool. Use this for setup, special handling of attributes
     * or specialized validation of arguments etc 
     * @throws Exception If anything went wrong.
     */
    public void initialize() throws Exception;
    
    /**
     * Executes the tool. 
     * @throws Exception If anything went wrong.
     */
    public void run() throws Exception;
    
}
