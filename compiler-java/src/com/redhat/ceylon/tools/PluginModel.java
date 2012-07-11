package com.redhat.ceylon.tools;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.tools.annotation.Style;

/**
 * A model of a plugin including a representation of the command line arguments 
 * accepted by the plugin.
 * @author tom
 * @param <T>
 */
public class PluginModel<T extends Plugin> {
    private String name;
    private PluginLoader loader;
    private Map<String, OptionModel> optionsByName = new LinkedHashMap<>(1);
    private Map<Character, OptionModel> optionsByShort = new HashMap<>(1);
    private List<ArgumentModel<?>> arguments = new ArrayList<>(1);
    private List<Method> postConstruct = new ArrayList<>(1);
    private Class<T> toolClass;
    private Method rest;
    private Class<? extends Style> argumentStyle;
    
    public PluginLoader getToolLoader() {
        return loader;
    }
    
    public void setToolLoader(PluginLoader toolLoader) {
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
    public Collection<OptionModel> getOptions() {
        return optionsByName.values();
    }
    
    public void addOption(OptionModel option) {
        optionsByName.put(option.getName(), option);
        if (option.getShortName() != null) {
            optionsByShort.put(option.getShortName(), option);
        }
    }
    
    public OptionModel getOption(String longName) {
        return optionsByName.get(longName);
    }
    
    public OptionModel getOptionByShort(char shortName) {
        return optionsByShort.get(shortName);
    }
    
    /**
     * The arguments, ordered by {@link com.redhat.ceylon.tools.annotation.Argument#order()}
     * @return
     */
    public List<ArgumentModel<?>> getArguments() {
        return arguments;
    }
    
    public void addArgument(ArgumentModel<?> argument) {
        if (!arguments.isEmpty()
                && arguments.get(arguments.size()-1).getMultiplicity().isRange()) {
            throw new IllegalArgumentException("Arguments after variable-multiplicity arguments are not supported");
        }
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

    public void setArgumentStyle(Class<? extends Style> argumentStyle) {
        this.argumentStyle = argumentStyle;
    }
    
    public Class<? extends Style> getArgumentStyle() {
        return argumentStyle;
    }
    
}
