package com.redhat.ceylon.tools;

import java.lang.reflect.Method;

/**
 * A command line argument accepted by a plugin
 * @author tom
 * @param <T>
 */
class ArgumentModel<T> {
    private Method setter;
    private Class<T> type;
    private String name;
    private Multiplicity multiplicity;
    
    public Multiplicity getMultiplicity() {
        return multiplicity;
    }
    public void setMultiplicity(Multiplicity multiplicity) {
        this.multiplicity = multiplicity;
    }
    public Method getSetter() {
        return setter;
    }
    public void setSetter(Method setter) {
        this.setter = setter;
    }
    
    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    
}
