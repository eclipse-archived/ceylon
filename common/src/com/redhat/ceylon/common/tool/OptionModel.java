package com.redhat.ceylon.common.tool;

/**
 * A command line option or option argument accepted by a plugin
 */
public class OptionModel<A> {
    private String longName;
    private Character shortName;
    private ArgumentModel<A> argument;
    private boolean pureOption;
    public String getLongName() {
        return longName;
    }
    public void setLongName(String name) {
        this.longName = name;
    }
    public Character getShortName() {
        return shortName;
    }
    public void setShortName(Character shortName) {
        this.shortName = shortName;
    }
    public ArgumentModel<A> getArgument() {
        return argument;
    }
    public void setArgument(ArgumentModel<A> argument) {
        this.argument = argument;
    }
    /**
     * Whether this option is a <em>pure</em> option (that is, an option 
     * which doesn't have any associated command line argument, 
     * like {@code grep}'s {@code -i}).
     *  
     * Such options do have an {@linkplain #getArgument() argument model} 
     * with a {@code boolean} {@linkplain ArgumentModel#getType() type}.
     * @return
     */
    public boolean isPureOption() {
        return pureOption;
    }
    public void setPureOption(boolean pureOption) {
        this.pureOption = pureOption;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (longName != null) {
            sb.append("--").append(longName);
        }
        if (longName != null && shortName != null) {
            sb.append("/");
        }
        if (shortName != null) {
            sb.append("-").append(shortName);
        }
        return sb.toString();
    }
}
