package com.redhat.ceylon.common.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The multiplicity of an option argument or argument.
 * @author tom
 */
public class Multiplicity {

    public static final Multiplicity _1 = new Multiplicity(1, 1);
    public static final Multiplicity _0_OR_MORE = new Multiplicity(0, Integer.MAX_VALUE);
    public static final Multiplicity _0_OR_1 = new Multiplicity(0, 1);
    public static final Multiplicity _1_OR_MORE = new Multiplicity(1, Integer.MAX_VALUE);
    
    private final int min;
    private final int max;
    
    Multiplicity(int minMax) {
        this(minMax, minMax);
    }
    
    Multiplicity(int min, int max) {
        if (min < 0 || max <= 0 || max < min) {
            throw new IllegalArgumentException();
        }
        this.min = min;
        this.max = max;
    }
    
    /** true if {@linkplain #getMin() min} > 0 */
    public boolean isRequired() {
        return this.min > 0;
    }
    
    /** true if {@linkplain #getMax() max} > 1 */
    public boolean isMultivalued() {
        return this.max > 1;
    }
    
    /** true if {@linkplain #getMin() min} != {@linkplain #getMax() max} */
    public boolean isRange() {
        return this.max != this.min;
    }
    
    /** true if the given number is between {@linkplain #getMin() min} 
     * and {@linkplain #getMax() max}, inclusive.
     */
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
        case "1":
            return _1;
        case "*":
            return _0_OR_MORE;
        case "?":
            return _0_OR_1;
        case "+":
            return _1_OR_MORE;
        }
        str = str.trim();
        Matcher matcher = Pattern.compile("([0-9]+)").matcher(str);
        if (matcher.matches()) {
            int num = Integer.parseInt(matcher.group(1));
            return new Multiplicity(num);
        }
        matcher = Pattern.compile("([\\(\\[])\\s*([0-9]+)\\s*,\\s*([0-9]+)?\\s*([\\)\\]])").matcher(str);
        if (matcher.matches()) {
            boolean minInclusive = "[".equals(matcher.group(1));
            int min = Integer.parseInt(matcher.group(2));
            boolean maxInclusive = "]".equals(matcher.group(4));
            int max = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : Integer.MAX_VALUE;
            return new Multiplicity(minInclusive ? min : min + 1, 
                    maxInclusive || matcher.group(3) == null ? max : max - 1);
        }
        throw new RuntimeException("Unsupported multiplicity " + str);
    }
    
    @Override
    public int hashCode() {
        return min ^ max;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Multiplicity other = (Multiplicity) obj;
        return this.min == other.min 
                && this.max == other.max;
    }

    public String toString() {
        if (this.equals(_0_OR_MORE)) {
            return "*";
        } else if (this.equals(_0_OR_1)) {
            return "?";
        } else if (this.equals(_1_OR_MORE)) {
            return "+";
        } else if (min == max) {
            return String.valueOf(min);
        }
        return "[" + min + "," + max + "]"; 
    }
}
