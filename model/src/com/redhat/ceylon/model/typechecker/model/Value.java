package com.redhat.ceylon.model.typechecker.model;

import java.util.Set;



/**
 * Represents any value - either a reference or getter.
 *
 * @author Gavin King
 */
public class Value extends FunctionOrValue implements Scope {
    
    private static final int VARIABLE = 1<<22;
    private static final int TRANSIENT = 1<<23;
    private static final int LATE = 1<<24;
    private static final int ENUM_VALUE = 1<<25;
    private static final int SPECIFIED_IN_FOR_ELSE = 1<<26;
    private static final int INFERRED = 1<<27;
    private static final int SELF_CAPTURED = 1<<28;
    
    private Setter setter;

    public Setter getSetter() {
        return setter;
    }
    
    public void setSetter(Setter setter) {
        this.setter = setter;
    }
    
    @Override
    public boolean isVariable() {
        return (flags&VARIABLE)!=0 || setter!=null;
    }

    public void setVariable(boolean variable) {
        if (variable) {
            flags|=VARIABLE;
        }
        else {
            flags&=(~VARIABLE);
        }
    }

    @Override
    public boolean isTransient() {
        return (flags&TRANSIENT)!=0;
    }
    
    public void setTransient(boolean trans) {
        if (trans) {
            flags|=TRANSIENT;
        }
        else {
            flags&=(~TRANSIENT);
        }
    }
    
    @Override
    public boolean isLate() {
		return (flags&LATE)!=0;
	}
    
    public void setLate(boolean late) {
        if (late) {
            flags|=LATE;
        }
        else {
            flags&=(~LATE);
        }
	}

    public boolean isEnumValue() {
        return (flags&ENUM_VALUE)!=0;
    }

    public void setEnumValue(boolean enumValue) {
        if (enumValue) {
            flags|=ENUM_VALUE;
        }
        else {
            flags&=(~ENUM_VALUE);
        }
    }

    public boolean isSpecifiedInForElse() {
        return (flags&SPECIFIED_IN_FOR_ELSE)!=0;
    }

    public void setSpecifiedInForElse(boolean assignedInFor) {
        if (assignedInFor) {
            flags|=SPECIFIED_IN_FOR_ELSE;
        }
        else {
            flags&=(~SPECIFIED_IN_FOR_ELSE);
        }
    }

    /**
     * used for object declarations that use their own 
     * value binding in their body
     */
    @Override
    public boolean isSelfCaptured(){
        return (flags&SELF_CAPTURED)!=0;
    }
    
    public void setSelfCaptured(boolean selfCaptured) {
        if (selfCaptured) {
            flags|=SELF_CAPTURED;
        }
        else {
            flags&=(~SELF_CAPTURED);
        }
    }
    
    public boolean isInferred() {
        return (flags&INFERRED)!=0;
    }
    
    public void setInferred(boolean inferred) {
        if (inferred) {
            flags|=INFERRED;
        }
        else {
            flags&=(~INFERRED);
        }
    }

    @Override
    public Set<String> getScopedBackends() {
        return super.getScopedBackends();
    }
    
    @Override
    public String toString() {
        Type type = getType();
        if (type==null) {
            return "value " + toStringName();
        }
        else {
            return "value " + toStringName() + 
                    " => " + type.asString();
        }
    }

}
