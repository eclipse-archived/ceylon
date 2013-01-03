/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.tools.new_;

import java.io.Console;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Variable {

    protected final String key;
    
    protected final VariableValidator validator;
    
    protected VariableValue variableValue;
    
    protected Variable(String key, VariableValidator validator, VariableValue variableValue) {
        super();
        this.key = key;
        this.variableValue = variableValue;
        this.validator = validator;
    }

    public String getKey() {
        return key;
    }
    
    public VariableValue getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(VariableValue varValue) {
        this.variableValue = varValue;
    }

    public VariableValidator getValidator() {
        return validator;
    }

    /**
     * Initialize this variable.
     * @param projectName the project
     * @param env The environment in which to initialize the variable
     * @return A list of other variables that will need initializing
     */
    public List<Variable> initialize(String projectName, Environment env) {
        String value = getVariableValue().getValue(projectName, env);
        if (!isValid(value)) {
            throw new RuntimeException(Messages.msg("value.invalid", value));
        }
        env.put(getKey(), value);  
        List<Variable> subvars = subvars(value);
        return subvars != null ? subvars : Collections.<Variable>emptyList();
    }

    private boolean isValid(String value) {
        VariableValidator validator = getValidator();
        return validator == null || validator.isValid(value);
    }

    protected List<Variable> subvars(String value) {
        return null;
    }
    
    public static Variable moduleName(String key, String defaultValue) {
        PatternValidator validator = new PatternValidator("[a-zA-Z0-9.]+");
        return new Variable(key, validator,
                new PromptedValue(key, validator, defaultValue));
    }
    
    public static Variable moduleDir(final String key, final String moduleNameKey) {
        return new Variable(key, null, new VariableValue() {  
            @Override
            public String getValue(String projectName, Environment env) {
                return env.get(moduleNameKey).replace('.', '/');
            }
        });
    }
    
    public static Variable moduleVersion(String key, String defaultValue) {
        PatternValidator validator = new PatternValidator("[a-zA-Z0-9.]+");
        return new Variable(key, validator, 
                new PromptedValue(key, validator, defaultValue));
    }
    
    public static ChoiceVariable booleanVariable(String key, List<Variable> yesVars, List<Variable> noVars) {
        PatternValidator validator = new PatternValidator("true|false");
        HashMap<String, List<Variable>> choices = new HashMap<>();
        final String oui = Messages.msg("mnemonic.yes");
        final String non = Messages.msg("mnemonic.no");
        if (oui.equals(non)) {
            throw new RuntimeException();
        }
        choices.put("true", yesVars);
        choices.put("false", noVars);
        PromptedValue prompt = new PromptedValue(key, validator) {
            @Override
            protected String parseValue(String readLine) {
                return oui.equals(readLine)
                        || non.equals(readLine) ? Boolean.toString(readLine.trim().equals(oui)) : null;
            }
        };
        return new ChoiceVariable(key, validator, prompt, choices);
    }
    
    public static ChoiceVariable yesNo(String key, Variable... yesVars) {
        return booleanVariable(key, Arrays.asList(yesVars), null);
    }
}

interface VariableValue {

    public String getValue(String projectName, Environment env);

}

class PromptedValue implements VariableValue {
    
    private final String key;
    private final String defaultValue;
    private final VariableValidator validator;

    public PromptedValue(String key) {
        this(key, null, null);
    }
    
    public PromptedValue(String key, String defaultValue) {
        this(key, null, defaultValue);
    }
    
    public PromptedValue(String key, VariableValidator validator) {
        this(key, validator, null);
    }
    
    public PromptedValue(String key, VariableValidator validator, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.validator = validator;
        if (validator != null && defaultValue != null && !validator.isValid(defaultValue)) {
            throw new RuntimeException("Default value " + defaultValue + " is not valid");
        }
    }
    
    protected String getDefaultValue() {
        return defaultValue;
    }
    
    protected String getDefault(Environment env) {
        if (defaultValue == null) {
            return defaultValue;
        }
        Template template = new Template(defaultValue);
        return template.eval(env);
    }
    
    protected String getPrompt(String projectName, Environment env, String dv) {
        String p = Messages.msg(projectName + ".prompt." + key).trim();
        if (dv != null) {
            p += " [" + dv + "]";
        }
        p += ": ";
        return p;
    }
    
    @Override
    public String getValue(String projectName, Environment env) {
        String value;
        while (true) {
            Console console = System.console();
            if (console == null) {
                throw new RuntimeException(Messages.msg("no.console"));
            }
            String dv = getDefault(env);
            String readLine = console.readLine(getPrompt(projectName, env, dv));
            if (readLine == null) {
                throw new RuntimeException(Messages.msg("exit"));
            } else if (readLine.isEmpty() && dv != null) {
                readLine = dv;
            }
            value = parseValue(readLine);
            if (value != null) {
                break;
            }
        }
        return value;
    }

    protected String parseValue(String readLine) {
        return (validator == null || validator.isValid(readLine)) ? readLine : null;
    }

    protected List<Variable> subvars(String value) {
        return Collections.emptyList();
    }
}

class GivenValue implements VariableValue {

    private String value;

    public GivenValue(String value) {
        this.value = value;
    }
    
    @Override
    public String getValue(String projectName, Environment env) {
        return value;
    }
    
}

class ChoiceVariable extends Variable {

    private Map<String, List<Variable>> choices;

    public ChoiceVariable(String key, 
            VariableValidator validator, 
            VariableValue varValue, 
            Map<String, List<Variable>> choices) {
        super(key, validator, varValue);
        this.choices = choices;
    }
    
    protected List<Variable> subvars(String value) {
        return choices.get(value);
    }

}

interface VariableValidator {

    public boolean isValid(String value);

}

class PatternValidator implements VariableValidator {
    private final Pattern pattern;

    public PatternValidator(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public boolean isValid(String value) {
        return pattern.matcher(value).matches();
    }
}