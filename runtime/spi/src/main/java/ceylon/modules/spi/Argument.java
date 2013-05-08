/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ceylon.modules.spi;

/**
 * @author Stephane Epardaud
 */
public enum Argument {

    EXECUTABLE("executable", ArgumentType.IMPL, 1),
    CACHE_CONTENT("cache_content", ArgumentType.IMPL, 0),
    IMPLEMENTATION("impl", ArgumentType.IMPL, 2),
    RUN("run", ArgumentType.CEYLON, 1),
    REPOSITORY("rep", ArgumentType.CEYLON, 1),
    SYSTEM_REPOSITORY("sysrep", ArgumentType.CEYLON, 1),
    SOURCE("src", ArgumentType.CEYLON, 1),
    VERBOSE("verbose", ArgumentType.CEYLON, 0),
    OFFLINE("offline", ArgumentType.CEYLON, 0),
    HELP("help", ArgumentType.CEYLON, 0, "h", "-help"),
    VERSION("version", ArgumentType.CEYLON, 0, "v", "-version");

    private String value;
    private int requiredValues = 0;
    private ArgumentType argumentType;
    private String[] aliases;

    Argument(String value, ArgumentType type, int requiredValues, String... aliases) {
        this.value = value;
        this.argumentType = type;
        this.requiredValues = requiredValues;
        this.aliases = aliases;
    }

    public int getRequiredValues() {
        return requiredValues;
    }

    public static Argument forArgumentName(String name, ArgumentType type) {
        for (Argument argument : Argument.values()) {
            if (argument.argumentType != type)
                continue;
            if (argument.value.equals(name))
                return argument;
            for (String alias : argument.aliases)
                if (alias.equals(name))
                    return argument;
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
