package com.redhat.ceylon.tools;

/**
 * A command line option or option argument accepted by a plugin
 */
class OptionModel<A> {
    private String longName;
    private Character shortName;
    private ArgumentModel<A> argument;
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
}
