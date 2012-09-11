package com.redhat.ceylon.common.tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.tool.OptionModel.ArgumentType;


/**
 * Responsible for instantiating and configuring a {@link Tool} according to
 * some command line arguments and a {@link ToolModel}.
 * @author tom
 */
public class ToolFactory {
    
    private static final String SHORT_PREFIX = "-";
    private static final char LONG_SEP = '=';
    private static final String LONG_PREFIX = "--";
    
    private final ArgumentParserFactory argParserFactory;
    
    public ToolFactory(ArgumentParserFactory argParserFactory) {
        this.argParserFactory = argParserFactory;
    }
    
    public <T extends Tool> T newInstance(ToolModel<T> toolModel) {
        T tool;
        try {
            tool = toolModel.getToolClass().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ToolException("Could not instantitate tool " + toolModel.getToolClass(), e);
        }
        
        return tool;
    }

    private <T extends Tool> void setToolLoader(ToolModel<T> toolModel,
            T tool) {
        try {
            toolModel.getToolClass().getMethod("setToolLoader", ToolLoader.class).invoke(tool, toolModel.getToolLoader());
        } catch (NoSuchMethodException e) {
            // Ignore
        } catch (ReflectiveOperationException e) {
            throw new ToolException("Could not instantitate tool " + toolModel.getToolClass(), e);
        }
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
                    throw new ToolException();
                }
            }
            return new Binding<List<A>>(givenOption, 
                    (OptionModel)om, (ArgumentModel)am, listValue);
        }
        
        public OptionArgumentException invalid(Throwable throwable) {
            final String badValue = unparsedArgumentValue != null ? unparsedArgumentValue : String.valueOf(value);
            if (optionModel != null) {
                return new OptionArgumentException("option.invalid.value", givenOption, badValue);
            } else {
                return new OptionArgumentException("option.invalid.value", argumentModel.getName(), badValue);
            }
        }
    }
    
    /**
     * Parses the given arguments binding them to a new instance of the 
     * tool model.
     */
    public <T extends Tool> T bindArguments(ToolModel<T> toolModel, Iterable<String> args) {
        T tool = newInstance(toolModel);
        return bindArguments(toolModel, tool, args);
    }
    
    /**
     * Parses the given arguments binding them to an existing instanceo of the 
     * the tool model.
     * You should probably be using {@link #bindArguments(ToolModel, Iterable)}, 
     * there are few tools which need to call this method directly.
     */
    public <T extends Tool> T bindArguments(ToolModel<T> toolModel, T tool, Iterable<String> args) {
        try {
            List<String> unrecognised = new ArrayList<String>(1);
            List<String> rest = new ArrayList<String>(1);
            setToolLoader(toolModel, tool);
            Map<ArgumentModel<?>, List<Binding<?>>> bindings = new HashMap<ArgumentModel<?>, List<Binding<?>>>(1);
            boolean eoo = false;
            int argumentModelIndex = 0;
            int argumentsBoundThisIndex = 0;
            Iterator<String> iter = args.iterator();
            argloop: while (iter.hasNext()) {
                final String arg = iter.next();
                OptionModel<?> option;
                String argument;
                if (!eoo && isEoo(arg)) {
                    eoo = true;
                    continue;
                } else if (!eoo && isLongForm(arg)) {
                    String longName = getLongFormOption(arg);
                    option = toolModel.getOption(longName);
                    if (option == null) {
                        rest.add(arg);
                    } else {
                        switch (option.getArgumentType()) {
                        case NOT_ALLOWED:
                            argument = "true"; 
                            break;
                        case OPTIONAL:
                        case REQUIRED:
                            argument = getLongFormArgument(arg, iter);
                            if (argument == null && option.getArgumentType() == ArgumentType.REQUIRED) {
                                if (iter.hasNext()) {
                                    argument = iter.next();
                                } else {
                                    throw new OptionArgumentException("option.without.argument", arg);
                                }
                            }
                            break;
                        default:
                            throw new RuntimeException("Assertion failed");
                        }
                        processArgument(tool, bindings, new Binding(longName, option, argument));    
                    }
                } else if (!eoo && isShortForm(arg)) {
                    for (int idx = 1; idx < arg.length(); idx++) {
                        char shortName = arg.charAt(idx);
                        option = toolModel.getOptionByShort(shortName);
                        if (option == null) {
                            String msg;
                            if (arg.equals("-"+shortName)) {
                                msg = arg;
                            } else {
                                msg = ToolMessages.msg("option.unknown.short", shortName, arg);
                            }
                            unrecognised.add(msg);
                            continue argloop;
                        } 
                        switch (option.getArgumentType()) {
                        case NOT_ALLOWED:
                            argument = "true";
                            break;
                        case REQUIRED:
                            if (idx == arg.length() -1) {// argument is next arg
                                if (!iter.hasNext()) {
                                    throw new OptionArgumentException("option.without.argument", arg);
                                }
                                argument = iter.next();
                            } else {// argument is rest of this arg
                                argument = arg.substring(idx+1);
                                idx = arg.length()-1;
                            }
                            break;
                        case OPTIONAL:
                        default:
                            throw new RuntimeException("Assertion failed");
                        }
                        processArgument(tool, bindings, new Binding(String.valueOf(shortName), option, argument));
                    }
                } else {// an argument
                    option = null;
                    argument = arg;
                    if (isArgument(arg)) {
                        final List<ArgumentModel<?>> argumentModels = toolModel.getArguments();
                        final ArgumentModel<?> argumentModel = argumentModels.get(argumentModelIndex);
                        processArgument(tool, bindings, new Binding(argumentModel, argument));
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
            
            checkMultiplicities(toolModel, bindings);
            
            for (Map.Entry<ArgumentModel<?>, List<Binding<?>>> entry : bindings.entrySet()) {
                final ArgumentModel<?> argument = entry.getKey();
                List values = (List)entry.getValue();
                if (argument.getMultiplicity().isMultivalued()) {
                    Binding<List<?>> mv = Binding.mv(values);
                    setValue(tool, mv);    
                }
            }
            
            if (toolModel.getRest() != null) {
                try {
                    toolModel.getRest().invoke(tool, rest);
                } catch (InvocationTargetException e) {
                    throw new OptionArgumentException(e);
                }
            } else {
                unrecognised.addAll(rest);
            }
            
            assertAllRecognised(unrecognised);
            
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
            throw new ToolException(e);
        }
    }

    private void assertAllRecognised(List<String> unrecognised) {
        if (!unrecognised.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String s : unrecognised) {
                sb.append(s).append(", ");
            }
            sb.setLength(sb.length()-2);// remove last ,
            throw new OptionArgumentException("option.unknown", sb.toString());
        }
    }
    
    public  boolean isEoo(final String arg) {
        return arg.equals(LONG_PREFIX);
    }

    public  boolean isLongForm(final String arg) {
        return arg.startsWith(LONG_PREFIX);
    }

    public String getLongFormOption(final String arg) {
        final int eq = arg.indexOf(LONG_SEP);
        String longName;
        if (eq == -1) { // long-form option
            longName = arg.substring(LONG_PREFIX.length());
        } else {// long-form option argument
            longName = arg.substring(LONG_PREFIX.length(), eq);
        }
        return longName;
    }

    public String getLongFormArgument(final String arg, Iterator<String> iter) {
        final int eq = arg.indexOf(LONG_SEP);
        if (eq == -1) {
            return null;
        }
        String argument = arg.substring(eq+1);
        return argument;
    }

    public boolean isShortForm(String arg) {
        return arg.startsWith(SHORT_PREFIX) && !arg.equals(SHORT_PREFIX);
    }

    public boolean isArgument(String arg) {
        return true;
    }

    private <T extends Tool> void checkMultiplicities(
            ToolModel<T> toolModel,
            Map<ArgumentModel<?>, List<Binding<?>>> bindings) {
        for (Map.Entry<ArgumentModel<?>, List<Binding<?>>> entry : bindings.entrySet()) {
            final ArgumentModel<?> argument = entry.getKey();
            List values = (List)entry.getValue();
            checkMultiplicity(argument, values);
        }
        for (OptionModel option : toolModel.getOptions()) {
            ArgumentModel argument = option.getArgument();
            checkMultiplicity(argument, bindings.get(argument));
            
        }
        for (ArgumentModel argument : toolModel.getArguments()) {
            argument.getMultiplicity().getMin();
            checkMultiplicity(argument, bindings.get(argument));
        }
    }

    private void checkMultiplicity(final ArgumentModel<?> argument, List values) {
        OptionModel option = argument.getOption();
        Multiplicity multiplicity = argument.getMultiplicity();
        int size = values != null ? values.size() : 0;
        if (size < multiplicity.getMin()) {
            if (option != null) {
                throw new OptionArgumentException("option.too.few",
                        argument.getOption(), multiplicity.getMin());
            } else {
                throw new OptionArgumentException(
                    "argument.too.few", 
                    argument.getName(), multiplicity.getMin());    
            }
        }
        if (size > multiplicity.getMax()) {
            if (option != null) {
                throw new OptionArgumentException("option.too.many",
                        argument.getOption(), multiplicity.getMax());
            } else {
                throw new OptionArgumentException(
                    "argument.too.many", 
                    argument.getName(), multiplicity.getMax());    
            }
        }
    }

    private <T extends Tool, A> void processArgument(T tool,
            Map<ArgumentModel<?>, List<Binding<?>>> bindings,
            Binding<A> binding) {
        List<Binding<?>> values = bindings.get(binding.argumentModel);
        if (values == null) {
            values = new ArrayList<Binding<?>>(1);
            bindings.put(binding.argumentModel, values);
        }
        if (binding.argumentModel.getMultiplicity().isMultivalued()) {
            binding.value = parseArgument(binding);
        } else {
            parseAndSetValue(tool, binding);
        }
        values.add(binding);
    }

    private <T extends Tool, A> void parseAndSetValue(T tool,
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
    
    private <A> void setValue(Tool tool, Binding<A> binding) {
        try {
            binding.argumentModel.getSetter().invoke(tool, binding.value);
        } catch (IllegalAccessException e) {
            throw new ToolException(e);
        } catch (InvocationTargetException e) {
            throw binding.invalid(e.getCause());
        }
    }

}
