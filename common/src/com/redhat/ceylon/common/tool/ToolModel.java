package com.redhat.ceylon.common.tool;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A model of a plugin including a representation of the command line arguments 
 * accepted by the plugin.
 * @author tom
 * @param <T>
 */
public class ToolModel<T extends Tool> {
    private String name;
    private ToolLoader loader;
    private Map<String, OptionModel<?>> optionsByName = new LinkedHashMap<>(1);
    private Map<Character, OptionModel<?>> optionsByShort = new HashMap<>(1);
    private List<ArgumentModel<?>> arguments = new ArrayList<>(1);
    private List<Method> postConstruct = new ArrayList<>(1);
    private Class<T> toolClass;
    private Method rest;
    private SubtoolModel<?> subtoolModel;
    private ToolModel<?> parentTool;
    private boolean script;
    private String scriptName;

    public ToolLoader getToolLoader() {
        return loader;
    }
    
    public void setToolLoader(ToolLoader toolLoader) {
        this.loader = toolLoader;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * The options and option arguments, in no particular order
     */
    public Collection<OptionModel<?>> getOptions() {
        return optionsByName.values();
    }
    
    public void addOption(OptionModel<?> option) {
        option.setToolModel(this);
        optionsByName.put(option.getLongName(), option);
        if (option.getShortName() != null) {
            optionsByShort.put(option.getShortName(), option);
        }
    }
    
    public OptionModel<?> getOption(String longName) {
        return optionsByName.get(longName);
    }
    
    public OptionModel<?> getOptionByShort(char shortName) {
        return optionsByShort.get(shortName);
    }
    
    /**
     * The arguments, ordered by {@link com.redhat.ceylon.common.tool.Argument#order()}
     * @return
     */
    public List<ArgumentModel<?>> getArguments() {
        return arguments;
    }
    
    public List<ArgumentModel<?>> getArgumentsAndSubtool() {
        List<ArgumentModel<?>> result = new ArrayList<>(getArguments());
        if (subtoolModel != null) {
            result.add(subtoolModel);
        }
        return result;
    }
    
    public void addArgument(ArgumentModel<?> argument) {
        if (!arguments.isEmpty()
                && arguments.get(arguments.size()-1).getMultiplicity().isRange()) {
            throw new IllegalArgumentException("Arguments after variable-multiplicity arguments are not supported");
        }
        if (argument instanceof SubtoolModel) {
            throw new IllegalArgumentException();
        }
        argument.setToolModel(this);
        this.arguments.add(argument);
    }
    
    public void setToolClass(Class<T> toolClass) {
        this.toolClass = toolClass;
    }
    
    public Class<T> getToolClass() {
        return toolClass;
    }
    
    
    public void addPostConstruct(Method method) {
        postConstruct.add(method);
    }
    
    public List<Method> getPostConstruct() {
        return postConstruct;
    }
    
    public void setRest(Method method) {
        this.rest = method;
    }
    
    public Method getRest() {
        return rest;
    }
    
    public boolean isTopLevel() {
        return getName().isEmpty();
    }
    
    /**
     * Determines whether the tool is high level.
     * @return
     */
    public boolean isPorcelain() {
        return !isTopLevel()  
                && (isScript() || toolClass.getAnnotation(Hidden.class) == null);
    }

    /**
     * Determines whether the tool is low level.
     * @return
     */
    public boolean isPlumbing() {
        // Note we have both this and isPorcelain() because the CeylonTool
        // is currently considered neither, which is a fudge for the CeylonDocToolTool
        return !isTopLevel()  
                && !isScript()
                && toolClass.getAnnotation(Hidden.class) != null;
    }

    public SubtoolModel<?> getSubtoolModel() {
        return this.subtoolModel;
    }
    
    public void setSubtoolModel(SubtoolModel<?> subtoolModel) {
        subtoolModel.setToolModel(this);
        this.subtoolModel = subtoolModel;
    }

    public ToolModel<?> getParentTool() {
        return parentTool;
    }

    public void setParentTool(ToolModel<?> parentTool) {
        this.parentTool = parentTool;
    }

    public void setScript(boolean script) {
        this.script = script;
    }

    public boolean isScript(){
        return script;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getScriptName() {
        return scriptName;
    }
}
