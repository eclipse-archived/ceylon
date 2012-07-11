package com.redhat.ceylon.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.tools.annotation.Style;

/**
 * Responsible for instantiating and configuring a {@link Plugin} according to
 * some command line arguments and a {@link PluginModel}.
 * @author tom
 */
public class PluginFactory {
    
    private final ArgumentParserFactory argParserFactory;
    
    public PluginFactory(ArgumentParserFactory argParserFactory) {
        this.argParserFactory = argParserFactory;
    }
    
    public <T extends Plugin> T newInstance(PluginModel<T> toolModel) {
        T tool;
        try {
            tool = toolModel.getToolClass().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new PluginException("Could not instantitate tool " + toolModel.getToolClass(), e);
        }
        
        try {
            toolModel.getToolClass().getMethod("setToolLoader", PluginLoader.class).invoke(tool, toolModel.getToolLoader());
        } catch (NoSuchMethodException e) {
            // Ignore
        } catch (ReflectiveOperationException e) {
            throw new PluginException("Could not instantitate tool " + toolModel.getToolClass(), e);
        }
        
        return tool;
    }
    
    /**
     * Parses the given arguments binding them to the tool.
     */
    public <T extends Plugin> T bindArguments(PluginModel<T> toolModel, Iterable<String> args) throws InvocationTargetException {
        try {
            Style style;
            try {
                style = toolModel.getArgumentStyle().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new PluginException();
            }
            List<String> rest = new ArrayList<String>();
            T tool = newInstance(toolModel);
            Map<ArgumentModel<?>, List<?>> multiValued = new HashMap<ArgumentModel<?>, List<?>>(1);
            boolean eoo = false;
            int argumentIndex = 0;
            int argumentsBoundThisIndex = 0;
            Iterator<String> iter = args.iterator();
            while (iter.hasNext()) {
                final String arg = iter.next();
                OptionModel option;
                String argument;
                if (!eoo && style.isEoo(arg)) {
                    eoo = true;
                    continue;
                } else if (!eoo && style.isLongForm(arg)) {
                    String longName = style.getLongFormOption(arg);
                    option = toolModel.getOption(longName);
                    if (option == null) {
                        rest.add(arg);
                    } else {
                        if (option.getArgument().getMultiplicity().isNone()) {
                            argument = "true";
                        } else {
                            argument = style.getLongFormArgument(arg, iter);
                        }   
                        processArgument(tool, multiValued, option.getArgument(), argument);
                    }
                } else if (!eoo && style.isShortForm(arg)) {
                    for (int idx = 1; idx < arg.length(); idx++) {
                        char shortName = arg.charAt(idx);
                        option = toolModel.getOptionByShort(shortName);
                        if (option == null) {
                            rest.add(arg);
                            continue;
                        } 
                        if (option.getArgument().getMultiplicity().isNone()) {
                            argument = "true";
                        } else {
                            if (idx == arg.length() -1) {// argument is next arg
                                if (!iter.hasNext()) {
                                    throw new OptionArgumentException("Option "+ arg + " should be followed by an argument");
                                }
                                argument = iter.next();
                            } else {// argument is rest of this arg
                                argument = arg.substring(idx+1);
                                idx = arg.length()-1;
                            }
                        }
                        processArgument(tool, multiValued, option.getArgument(), argument);
                    }
                } else {// an argument
                    option = null;
                    argument = arg;
                    if (style.isArgument(arg)) {
                        final List<ArgumentModel<?>> arguments = toolModel.getArguments();
                        final ArgumentModel<?> argumentModel = arguments.get(argumentIndex);
                        processArgument(tool, multiValued, argumentModel, argument);
                        argumentsBoundThisIndex++;
                        if (argumentsBoundThisIndex >= argumentModel.getMultiplicity().getMax()) {
                            argumentIndex++;
                            argumentsBoundThisIndex = 0;
                        }
                    } else {
                        rest.add(arg);
                    }
                }
            }
            
            for (Map.Entry<ArgumentModel<?>, List<?>> entry : multiValued.entrySet()) {
                setValue(tool, entry.getKey().getSetter(), entry.getValue());
            }
            
            if (toolModel.getRest() != null) {
                toolModel.getRest().invoke(tool, rest);
            } else {
                if (rest.size() == 1) {
                    throw new OptionArgumentException("Unrecognised option " + rest.get(0));
                } else if (rest.size() > 1){
                    throw new OptionArgumentException("Unrecognised options " + rest);
                }
            }
            
            for (Method m : toolModel.getPostConstruct()) {
                m.invoke(tool);
            }
            
            return tool;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends Plugin, A> boolean processArgument(T tool,
            Map<ArgumentModel<?>, List<?>> multiValued,
            ArgumentModel<A> argumentModel, 
            String argument) throws IllegalAccessException,
            InvocationTargetException {
        if (!argumentModel.getMultiplicity().isMultivalued()) {
            parseAndSetValue(tool, argumentModel, argument);
            return true;
        } else {
            List<A> values = (List)multiValued.get(argumentModel);
            if (values == null) {
                values = new ArrayList<A>(1);
                multiValued.put(argumentModel, values);
            }
            final A value = parseArgument(argumentModel, argument);
            values.add(value);
            return false;
        }
    }

    private <T extends Plugin, A> void parseAndSetValue(T tool,
            final ArgumentModel<A> argumentModel, String argument)
            throws IllegalAccessException, InvocationTargetException {
        final A value = parseArgument(argumentModel, argument);
        setValue(tool, argumentModel.getSetter(), value);
    }

    private <A> A parseArgument(final ArgumentModel<A> argumentModel, String argument) {
        // Note parser won't be null, because the ModelBuilder checked
        final ArgumentParser<A> parser = argParserFactory.getParser(argumentModel.getType());
        final A value = parser.parse(argument);
        return value;
    }
    
    private <A> void setValue(Plugin tool, final Method setter,
            final A value) throws IllegalAccessException,
            InvocationTargetException {
        setter.invoke(tool, value);
    }

}
