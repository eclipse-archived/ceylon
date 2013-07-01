package com.redhat.ceylon.common.tool;

import java.util.Collections;
import java.util.List;

/**
 * Exception for problems parsing options and arguments.
 */
public abstract class OptionArgumentException extends ToolError {

    private static String getIllegalArgumentErrorMessage(Throwable t) {
        String msg = "";
        if (t.getLocalizedMessage() != null) {
            msg += t.getLocalizedMessage();
        }
        if (t.getCause() instanceof IllegalArgumentException
                && t.getCause().getLocalizedMessage() != null
                && !t.getMessage().endsWith(t.getCause().getLocalizedMessage())) {
            msg += ": " + t.getCause().getLocalizedMessage();
        }
        return msg;
    }
    
    private static String getToolName(ArgumentModel<?> argumentModel) {
        return getToolName(argumentModel.getToolModel());
    }
    
    private static String getToolName(OptionModel<?> optionModel) {
        return getToolName(optionModel.getToolModel());
    }
    
    private static String getToolName(ToolModel<?> toolModel) {
        String toolName = toolModel.getName();
        if (toolName == null 
                || toolName.isEmpty()) {
            toolName = Tools.progName();
        }
        return toolName;
    }
    
    private OptionArgumentException(String message) {
        super(message);
    }
    
    private OptionArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    OptionArgumentException(Throwable cause) {
        super(cause);
    }
    
    public static class ToolInitializationException extends OptionArgumentException {
        public ToolInitializationException(Throwable cause) {
            super(cause.getLocalizedMessage(), cause);
        }
        
        @Override
        public String getErrorMessage() {
            return getIllegalArgumentErrorMessage(this);
        }
    }
    
    public static class OptionWithoutArgumentException extends OptionArgumentException {
        private final OptionModel<?> optionModel;

        public OptionWithoutArgumentException(OptionModel<?> optionModel, String arg) {
            super(ToolMessages.msg("option.without.argument", arg, getToolName(optionModel)));
            this.optionModel = optionModel;
        }

        public OptionModel<?> getOptionModel() {
            return optionModel;
        }
        
        public ToolModel<?> getToolModel() {
            return getOptionModel().getToolModel();
        }
        
    }
    
    public static class UnexpectedArgumentException extends OptionArgumentException {
        public UnexpectedArgumentException(String arg, ToolModel<?> toolModel) {
            super(ToolMessages.msg("argument.unexpected", arg, getToolName(toolModel)));
        }
    }
    
    public static class InvalidOptionValueException extends OptionArgumentException {
        private final OptionModel<?> optionModel;
        private final String givenOption;
        private final String badValue;

        public InvalidOptionValueException(Throwable cause, OptionModel<?> optionModel, String givenOption, String badValue) {
            super(ToolMessages.msg("option.invalid.value", givenOption,
                    getToolName(optionModel),
                    badValue), cause);
            this.optionModel = optionModel;
            this.givenOption = givenOption;
            this.badValue = badValue;
        }

        public OptionModel<?> getOptionModel() {
            return optionModel;
        }
        
        public ToolModel<?> getToolModel() {
            return getOptionModel().getToolModel();
        }

        public String getGivenOption() {
            return givenOption;
        }

        public String getBadValue() {
            return badValue;
        }
        
        @Override
        public String getErrorMessage() {
            return getIllegalArgumentErrorMessage(this);
        }
    }
    
    public static class InvalidArgumentValueException extends OptionArgumentException {
        private final ArgumentModel<?> argumentModel;
        private final String badValue;

        public InvalidArgumentValueException(Throwable cause, ArgumentModel<?> argumentModel, String badValue) {
            super(ToolMessages.msg("argument.invalid.value", argumentModel.getName(),
                    getToolName(argumentModel),
                    badValue), cause);
            this.argumentModel = argumentModel;
            this.badValue = badValue;
        }

        public ArgumentModel<?> getArgumentModel() {
            return argumentModel;
        }
        
        public ToolModel<?> getToolModel() {
            return getArgumentModel().getToolModel();
        }
        
        public String getBadValue() {
            return badValue;
        }
        
        @Override
        public String getErrorMessage() {
            return getIllegalArgumentErrorMessage(this);
        }
    }
    
    public static class OptionMultiplicityException extends OptionArgumentException {
        private final OptionModel<?> optionModel;

        public OptionMultiplicityException(OptionModel<?> optionModel, 
                String givenOptions, int bound, String msgKey) {
            super(ToolMessages.msg(msgKey, givenOptions, getToolName(optionModel), bound));
            this.optionModel = optionModel;
        }

        public OptionModel<?> getOptionModel() {
            return optionModel;
        }
        
        public ToolModel<?> getToolModel() {
            return getOptionModel().getToolModel();
        }
    }
    
    public static class ArgumentMultiplicityException extends OptionArgumentException {
        private ArgumentModel<?> argumentModel;

        public ArgumentMultiplicityException(ArgumentModel<?> argumentModel, int bound, String msgKey) {
            super(ToolMessages.msg(msgKey, argumentModel.getName(), getToolName(argumentModel), bound));
            this.argumentModel = argumentModel;
        }
        
        public ArgumentModel<?> getArgumentModel() {
            return argumentModel;
        }
        
        public ToolModel<?> getToolModel() {
            return getArgumentModel().getToolModel();
        }
    }
    
    public static class UnknownOptionException extends OptionArgumentException {

        private final List<UnknownOptionException> aggregating;
        private final String longName;
        private final Character shortName;
        private ToolModel<?> toolModel;
        
        private UnknownOptionException(
                ToolModel<?> toolModel, 
                String longName,
                Character shortName,
                List<UnknownOptionException> aggregating, String msg) {
            super(msg);
            this.toolModel = toolModel;
            this.longName = longName;
            this.shortName = shortName;
            this.aggregating = aggregating;
        }
        
        public ToolModel<?> getToolModel() {
            return toolModel;
        }
        
        public String getLongName() {
            return longName;
        }

        public Character getShortName() {
            return shortName;
        }

        public List<UnknownOptionException> getAggregating() {
            return aggregating;
        }
        
        public static UnknownOptionException shortOption(ToolModel<?> toolModel, char shortName,
                String arg) {
            String msgKey;
            if (arg.equals("-"+shortName)) {
                msgKey = "option.unknown.short";
            } else {
                msgKey = "option.unknown.short.in.combined";
            }
            return new UnknownOptionException(toolModel, null, shortName,
                    Collections.<UnknownOptionException>emptyList(), 
                    ToolMessages.msg(msgKey, "-" + shortName, getToolName(toolModel), arg));
        }

        public static UnknownOptionException longOption(ToolModel<?> toolModel, String arg) {
            int idx = arg.indexOf('=');
            String longOption = arg.substring(2, idx == -1 ? arg.length() : idx);
            return new UnknownOptionException(toolModel, 
                    longOption, null,
                    Collections.<UnknownOptionException>emptyList(), 
                    ToolMessages.msg("option.unknown.long", arg, getToolName(toolModel)));
        }

        public static UnknownOptionException aggregate(
                List<UnknownOptionException> unrecognised) {
            StringBuilder sb = new StringBuilder();
            for (UnknownOptionException u : unrecognised) {
                if (u.getLongName() != null) {
                    sb.append(u.getLongName());
                } else if (u.getShortName() != null) {
                    sb.append(u.getShortName());
                }
                sb.append(", ");
            }
            sb.setLength(sb.length()-2);
            UnknownOptionException result = new UnknownOptionException(null,
                    null, null,
                    unrecognised,
                    ToolMessages.msg("option.unknown.multiple", sb.toString()));
            return result;
        }
        
    }
}
