package com.redhat.ceylon.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The multiplicity of an option argument or argument.
 * @author tom
 */
class Multiplicity {

    public static final Multiplicity NONE = new Multiplicity(0, 0);
    public static final Multiplicity STAR = new Multiplicity(0, Integer.MAX_VALUE);
    public static final Multiplicity QUESTION = new Multiplicity(0, 1);
    public static final Multiplicity PLUS = new Multiplicity(1, Integer.MAX_VALUE);
    
    private final int min;
    private final int max;
    
    private Multiplicity(int min, int max) {
        if (min < 0 || max < 0 || max < min) {
            throw new IllegalArgumentException();
        }
        this.min = min;
        this.max = max;
    }
    
    public boolean isNone() {
        return this.max == 0;
    }
    
    public boolean isRequired() {
        return this.min > 0;
    }
    
    public boolean isMultivalued() {
        return this.max > 1;
    }
    
    public boolean isRange() {
        return this.max != this.min;
    }
    
    public boolean isWithinBound(int num) {
        return min <= num && num <= max;
    }

    public int getMax() {
        return max;
    }
    
    public int getMin() {
        return min;
    }
    
    public static Multiplicity fromString(String str) {
        str = str.trim();
        switch (str) {
        case "*":
            return STAR;
        case "?":
            return QUESTION;
        case "+":
            return PLUS;
        }
        Matcher matcher = Pattern.compile("\\{\\s*([0-9]+)\\s*\\}").matcher(str);
        if (matcher.matches()) {
            int num = Integer.parseInt(matcher.group(1));
            return new Multiplicity(num, num);
        }
        matcher = Pattern.compile("\\{\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\}").matcher(str);
        if (matcher.matches()) {
            int min = Integer.parseInt(matcher.group(1));
            int max = Integer.parseInt(matcher.group(2));
            return new Multiplicity(min, max);
        }
        return null;
    }
}
