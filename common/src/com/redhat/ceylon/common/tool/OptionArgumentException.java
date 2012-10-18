package com.redhat.ceylon.common.tool;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.OptionArgumentException.UnknownOptionException;

/**
 * Exception for problems parsing options and arguments.
 */
@NonFatal
public class OptionArgumentException extends ToolException {

    private OptionArgumentException(String message) {
        super(message);
    }

    OptionArgumentException(InvocationTargetException e) {
        super(e.getCause().getLocalizedMessage(), e);
    }

    @NonFatal
    public static class OptionWithoutArgumentException extends OptionArgumentException {
        private final OptionModel<?> optionModel;

        public OptionWithoutArgumentException(OptionModel<?> optionModel, String arg) {
            super(ToolMessages.msg("option.without.argument", arg));
            this.optionModel = optionModel;
        }

        public OptionModel<?> getOptionModel() {
            return optionModel;
        }
        
    }
    
    @NonFatal
    public static class UnexpectedArgumentException extends OptionArgumentException {
        public UnexpectedArgumentException(String arg) {
            super(ToolMessages.msg("argument.unexpected", arg));
        }
    }
    
    @NonFatal
    public static class InvalidOptionValueException extends OptionArgumentException {
        private final OptionModel<?> optionModel;
        private final String givenOption;
        private final String badValue;

        public InvalidOptionValueException(OptionModel<?> optionModel, String givenOption, String badValue) {
            super(ToolMessages.msg("option.invalid.value", givenOption, badValue));
            this.optionModel = optionModel;
            this.givenOption = givenOption;
            this.badValue = badValue;
        }

        public OptionModel<?> getOptionModel() {
            return optionModel;
        }

        public String getGivenOption() {
            return givenOption;
        }

        public String getBadValue() {
            return badValue;
        }
        
    }
    
    @NonFatal
    public static class InvalidArgumentValueException extends OptionArgumentException {
        private final ArgumentModel<?> argumentModel;
        private final String badValue;

        public InvalidArgumentValueException(ArgumentModel<?> argumentModel, String badValue) {
            super(ToolMessages.msg("argument.invalid.value", argumentModel.getName(), badValue));
            this.argumentModel = argumentModel;
            this.badValue = badValue;
        }

        public ArgumentModel<?> getArgumentModel() {
            return argumentModel;
        }

        public String getBadValue() {
            return badValue;
        }    
    }
    
    @NonFatal
    public static class OptionMultiplicityException extends OptionArgumentException {
        private final OptionModel<?> optionModel;

        public OptionMultiplicityException(OptionModel<?> optionModel, 
                String givenOptions, int bound, String msgKey) {
            super(ToolMessages.msg(msgKey, givenOptions, bound));
            this.optionModel = optionModel;
        }

        public OptionModel<?> getOptionModel() {
            return optionModel;
        }
        
    }
    
    @NonFatal
    public static class ArgumentMultiplicityException extends OptionArgumentException {
        public ArgumentMultiplicityException(ArgumentModel<?> argumentModel, int bound, String msgKey) {
            super(ToolMessages.msg(msgKey, argumentModel.getName(), bound));
        }
    }
    
    @NonFatal
    public static class UnknownOptionException extends OptionArgumentException {

        private final List<UnknownOptionException> aggregating;
        private final String longName;
        private final Character shortName;
        
        private UnknownOptionException(
                String longName,
                Character shortName,
                List<UnknownOptionException> aggregating, String msg) {
            super(msg);
            this.longName = longName;
            this.shortName = shortName;
            this.aggregating = aggregating;
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

        public static UnknownOptionException shortOption(char shortName,
                String arg) {
            String msgKey;
            if (arg.equals("-"+shortName)) {
                msgKey = "option.unknown.short";
            } else {
                msgKey = "option.unknown.short.in.combined";
            }
            return new UnknownOptionException(null, shortName,
                    Collections.<UnknownOptionException>emptyList(), 
                    ToolMessages.msg(msgKey, "-" + shortName, arg));
        }

        public static UnknownOptionException longOption(String arg) {
            int idx = arg.indexOf('=');
            String longOption = arg.substring(2, idx == -1 ? arg.length() : idx);
            return new UnknownOptionException(
                    longOption, null,
                    Collections.<UnknownOptionException>emptyList(), 
                    ToolMessages.msg("option.unknown.long", arg));
        }

        public static UnknownOptionException aggregate(
                List<UnknownOptionException> unrecognised) {
            UnknownOptionException result = new UnknownOptionException(
                    null, null,
                    unrecognised,
                    ToolMessages.msg("option.unknown.multiple"));
            return result;
        }
        
    }
}
