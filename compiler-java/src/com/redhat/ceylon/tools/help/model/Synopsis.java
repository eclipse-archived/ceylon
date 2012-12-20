package com.redhat.ceylon.tools.help.model;

import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.OptionModel;


public class Synopsis {

    private String invocation;
    
    private List<?> optionsAndArguments = Collections.emptyList();
    
    public List<?> getOptionsAndArguments() {
        return optionsAndArguments;
    }

    public void setOptionsAndArguments(List<?> options) {
        this.optionsAndArguments = options;
    }

    public String getInvocation() {
        return invocation;
    }

    public void setInvocation(String invocation) {
        this.invocation = invocation;
    }

    public void accept(Visitor visitor) {
        visitor.startSynopsis(this);
        for (Object o : getOptionsAndArguments()) {
            if (o instanceof OptionModel<?>) {
                visitor.visitSynopsisOption((OptionModel<?>)o);
            } else if (o instanceof SubtoolVisitor.ToolModelAndSubtoolModel) {
                visitor.visitSynopsisSubtool((SubtoolVisitor.ToolModelAndSubtoolModel)o);
            } else if (o instanceof ArgumentModel<?>) {
                visitor.visitSynopsisArgument((ArgumentModel<?>)o);
            } 
        }
        visitor.endSynopsis(this);
    }

}
