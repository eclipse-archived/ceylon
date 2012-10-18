package com.redhat.ceylon.common.tool;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Exception for problems parsing options and arguments.
 */
@NonFatal
public class OptionArgumentException extends ToolException {

    private OptionArgumentException(String msgKey, Object...msgArgs) {
        super(ToolMessages.msg(msgKey, msgArgs));
    }

    OptionArgumentException(InvocationTargetException e) {
        super(e.getCause().getLocalizedMessage(), e);
    }
    
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
    
    public static class UnexpectedArgumentException extends OptionArgumentException {
        public UnexpectedArgumentException(String arg) {
            super(ToolMessages.msg("argument.unexpected", arg));
        }
    }
    
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
    
    public static class OptionMultiplicityException extends OptionArgumentException {
        public OptionMultiplicityException(OptionModel<?> optionModel, int bound, String msgKey) {
            super(ToolMessages.msg(msgKey, optionModel, bound));
        }
    }
    
    public static class ArgumentMultiplicityException extends OptionArgumentException {
        public ArgumentMultiplicityException(ArgumentModel<?> argumentModel, int bound, String msgKey) {
            super(ToolMessages.msg(msgKey, argumentModel.getName(), bound));
        }
    }
    
    public static class UnknownOptionException extends OptionArgumentException {
        public UnknownOptionException(List<String> unknown) {
            super(ToolMessages.msg("option.unknown"));
            StringBuilder sb = new StringBuilder();
            for (String s : unrecognised) {
                sb.append(s).append(", ");
            }
            sb.setLength(sb.length()-2);// remove last ,
        }
    }
}
