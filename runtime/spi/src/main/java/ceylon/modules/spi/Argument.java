/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.spi;

/**
 * @author Stephane Epardaud
 */
public enum Argument {

    EXECUTABLE("executable", ArgumentType.IMPL, 1),
    CACHE_CONTENT("cache_content", ArgumentType.IMPL, 0),
    IMPLEMENTATION("impl", ArgumentType.IMPL, 2),
    RUN("run", ArgumentType.CEYLON, 1),
    CWD("cwd", ArgumentType.CEYLON, 1),
    REPOSITORY("rep", ArgumentType.CEYLON, 1),
    SYSTEM_REPOSITORY("sysrep", ArgumentType.CEYLON, 1),
    CACHE_REPOSITORY("cacherep", ArgumentType.CEYLON, 1),
    // Backwards-compat
    MAVEN_OVERRIDES("maven-overrides", ArgumentType.CEYLON, 1),
    OVERRIDES("overrides", ArgumentType.CEYLON, 1),
    DOWNGRADE_DIST("downgrade-dist", ArgumentType.CEYLON, 0),
    NO_DEFAULT_REPOSITORIES("nodefreps", ArgumentType.CEYLON, 0),
    SOURCE("src", ArgumentType.CEYLON, 1),
    VERBOSE("verbose", ArgumentType.CEYLON, 1),
    OFFLINE("offline", ArgumentType.CEYLON, 0),
    AUTO_EXPORT_MAVEN_DEPENDENCIES("auto-export-maven-dependencies", ArgumentType.CEYLON, 0),
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
