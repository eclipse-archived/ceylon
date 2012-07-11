package com.redhat.ceylon.tools;

/**
 * A command line option or option argument accepted by a plugin
 */
class OptionModel {
    private String name;
    private Character shortName;
    private ArgumentModel argument;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Character getShortName() {
        return shortName;
    }
    public void setShortName(Character shortName) {
        this.shortName = shortName;
    }
    public ArgumentModel getArgument() {
        return argument;
    }
    public void setArgument(ArgumentModel argument) {
        this.argument = argument;
    }
}
