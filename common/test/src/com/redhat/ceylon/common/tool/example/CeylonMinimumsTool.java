package com.redhat.ceylon.common.tool.example;

import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Plugin;
import com.redhat.ceylon.common.tool.Summary;

@Summary("")
public class CeylonMinimumsTool implements Plugin {

    private List<String> arguments;
    
    public List<String> getArguments() {
        return arguments;
    }

    @Argument(multiplicity="[3,]")
    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void run() {
    }

}
