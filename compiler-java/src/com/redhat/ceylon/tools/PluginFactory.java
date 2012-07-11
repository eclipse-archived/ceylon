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
    
    private static class Binding<A> {
        final String givenOption;
        final OptionModel<A> optionModel;
        final ArgumentModel<A> argumentModel;
        final String unparsedArgumentValue;
        A value;
        public Binding(String givenOption, OptionModel<A> optionModel,
                String unparsedArgumentValue) {
            super();
            this.givenOption = givenOption;
            this.optionModel = optionModel;
            this.argumentModel = optionModel.getArgument();
            this.unparsedArgumentValue = unparsedArgumentValue;
        }
        public Binding(ArgumentModel<A> argumentModel,
                String unparsedArgumentValue) {
            super();
            this.givenOption = null;
            this.optionModel = null;
            this.argumentModel = argumentModel;
            this.unparsedArgumentValue = unparsedArgumentValue;
        }
        private Binding(String givenOption, OptionModel<A> optionModel, ArgumentModel<A> argumentModel, A value) {
            this.givenOption = givenOption;
            this.optionModel = optionModel;
            this.argumentModel = argumentModel;
            this.unparsedArgumentValue = null;
            this.value = value;
        }
        static <A> Binding<List<A>> mv(List<Binding<A>> bindings) {
            List<A> listValue = new ArrayList<A>(bindings.size());
            String givenOption = null;
            OptionModel<A> om = null;
            ArgumentModel<A> am = null;
            for (Binding<A> binding : bindings) {
                listValue.add(binding.value);
                if (om == null) {
                    om = binding.optionModel;
                    am = binding.argumentModel;
                    givenOption = binding.givenOption;
                } else if (om != binding.optionModel
                        || am != binding.argumentModel) {
                    throw new PluginException();
                }
            }
            return new Binding<List<A>>(givenOption, 
                    (OptionModel)om, (ArgumentModel)am, listValue);
        }
        
        public OptionArgumentException invalid(Throwable throwable) {
            final String badValue = unparsedArgumentValue != null ? unparsedArgumentValue : String.valueOf(value);
            if (optionModel != null) {
                return new OptionArgumentException("Invalid value " + badValue + " given for option " + givenOption);
            } else {
                return new OptionArgumentException("Invalid value " + badValue + " given for argument " + argumentModel.getName());
            }
        }
    }
    
    /**
     * Parses the given arguments binding them to the tool.
     */
    public <T extends Plugin> T bindArguments(PluginModel<T> toolModel, Iterable<String> args) {
        try {
            Style style;
            try {
                style = toolModel.getArgumentStyle().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new PluginException();
            }
            List<String> rest = new ArrayList<String>();
            T tool = newInstance(toolModel);
            Map<ArgumentModel<?>, List<Binding<?>>> multiValuedArguments = new HashMap<ArgumentModel<?>, List<Binding<?>>>(1);
            boolean eoo = false;
            int argumentModelIndex = 0;
            int argumentsBoundThisIndex = 0;
            Iterator<String> iter = args.iterator();
            while (iter.hasNext()) {
                final String arg = iter.next();
                OptionModel<?> option;
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
                        processArgument(tool, multiValuedArguments, new Binding(longName, option, argument));    
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
                        processArgument(tool, multiValuedArguments, new Binding(String.valueOf(shortName), option, argument));
                    }
                } else {// an argument
                    option = null;
                    argument = arg;
                    if (style.isArgument(arg)) {
                        final List<ArgumentModel<?>> argumentModels = toolModel.getArguments();
                        final ArgumentModel<?> argumentModel = argumentModels.get(argumentModelIndex);
                        processArgument(tool, multiValuedArguments, new Binding(argumentModel, argument));
                        argumentsBoundThisIndex++;
                        if (argumentsBoundThisIndex >= argumentModel.getMultiplicity().getMax()) {
                            argumentModelIndex++;
                            argumentsBoundThisIndex = 0;
                        }
                    } else {
                        rest.add(arg);
                    }
                }
            }
            
            for (Map.Entry<ArgumentModel<?>, List<Binding<?>>> entry : multiValuedArguments.entrySet()) {
                final ArgumentModel<?> argument = entry.getKey();
                Binding<List<?>> mv = Binding.mv((List)entry.getValue());
                setValue(tool, mv);    
            }
            
            if (toolModel.getRest() != null) {
                try {
                    toolModel.getRest().invoke(tool, rest);
                } catch (InvocationTargetException e) {
                    throw new OptionArgumentException(e);
                }
            } else {
                if (rest.size() >= 1) {
                    throw new OptionArgumentException("Unrecognised option(s): " + rest);
                }
            }
            
            for (Method m : toolModel.getPostConstruct()) {
                try {
                    m.invoke(tool);
                } catch (InvocationTargetException e) {
                    throw new OptionArgumentException(e);
                }
            }
            
            return tool;
        } catch (IllegalAccessException e) {
            // Programming error 
            throw new PluginException(e);
        }
    }

    private <T extends Plugin, A> void processArgument(T tool,
            Map<ArgumentModel<?>, List<Binding<?>>> multiValued,
            Binding<A> binding) {
        final ArgumentModel<A> argumentModel = binding.argumentModel;
        if (!argumentModel.getMultiplicity().isMultivalued()) {
            parseAndSetValue(tool, binding);
        } else {
            List<Binding<?>> values = multiValued.get(binding.argumentModel);
            if (values == null) {
                values = new ArrayList<Binding<?>>(1);
                multiValued.put(binding.argumentModel, values);
            }
            binding.value = parseArgument(binding);
            values.add(binding);
        }
    }

    private <T extends Plugin, A> void parseAndSetValue(T tool,
            Binding<A> binding) {
        binding.value = parseArgument(binding);
        setValue(tool, binding);
    }

    private <A> A parseArgument(Binding<A> binding) {
        // Note parser won't be null, because the ModelBuilder checked
        final ArgumentParser<A> parser = argParserFactory.getParser(binding.argumentModel.getType());
        try {
            final A value = parser.parse(binding.unparsedArgumentValue);
            return value;
        } catch (Exception e) {
            throw binding.invalid(e);
        }
    }
    
    private <A> void setValue(Plugin tool, Binding<A> binding) {
        try {
            binding.argumentModel.getSetter().invoke(tool, binding.value);
        } catch (IllegalAccessException e) {
            throw new PluginException(e);
        } catch (InvocationTargetException e) {
            throw binding.invalid(e.getCause());
        }
    }

}
