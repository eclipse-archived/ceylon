package com.redhat.ceylon.tools.help.model;

import java.util.List;

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.OptionModel;


public class Synopsis {

    private String invocation;
    
    private List<OptionModel<?>> options;
    
    private List<ArgumentModel<?>> arguments;
    
    public String getInvocation() {
        return invocation;
    }

    public void setInvocation(String invocation) {
        this.invocation = invocation;
    }

    public List<OptionModel<?>> getOptions() {
        return options;
    }

    public void setOptions(List<OptionModel<?>> options) {
        this.options = options;
    }

    public List<ArgumentModel<?>> getArguments() {
        return arguments;
    }

    public void setArguments(List<ArgumentModel<?>> arguments) {
        this.arguments = arguments;
    }

    public void accept(Visitor visitor) {
        visitor.visitSynopsis(this);
    }

}
