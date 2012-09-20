package com.redhat.ceylon.tools.help.model;

import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.SubtoolModel;


public class Synopsis {

    private String invocation;
    
    public static class NameAndSubtool {
        private String name;
        private SubtoolModel<?> subtool;
        public NameAndSubtool(String name, SubtoolModel<?> subtool) {
            super();
            this.name = name;
            this.subtool = subtool;
        }
        public String getName() {
            return name;
        }
        public SubtoolModel<?> getSubtool() {
            return subtool;
        }
         
    }
    
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
            } else if (o instanceof NameAndSubtool) {
                visitor.visitSynopsisSubtool((NameAndSubtool)o);
            } else if (o instanceof ArgumentModel<?>) {
                visitor.visitSynopsisArgument((ArgumentModel<?>)o);
            } 
        }
        visitor.endSynopsis(this);
    }

}
