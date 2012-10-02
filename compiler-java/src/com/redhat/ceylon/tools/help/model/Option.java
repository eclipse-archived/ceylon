package com.redhat.ceylon.tools.help.model;

import org.tautua.markdownpapers.ast.Node;

import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.OptionModel.ArgumentType;

public class Option implements Documentation {

    private OptionModel<?> option;
    
    private Node description;

    public OptionModel<?> getOption() {
        return option;
    }

    public void setOption(OptionModel<?> option) {
        this.option = option;
    }

    public Node getDescription() {
        return description;
    }

    public void setDescription(Node description) {
        this.description = description;
    }

    public String getLongName() {
        String longName = option.getLongName() != null ? "--" + option.getLongName() : null;
        return longName;
    }

    public String getShortName() {
        String shortName = option.getShortName() != null ? "-" + option.getShortName() : null;
        return shortName;
    }

    public String getArgumentName() {
        String argumentName = option.getArgumentType() == ArgumentType.NOT_ALLOWED ? null : option.getArgument().getName();
        return argumentName;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitOption(this);
    }
    
}
