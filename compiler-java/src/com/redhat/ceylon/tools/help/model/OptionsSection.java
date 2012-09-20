package com.redhat.ceylon.tools.help.model;

import java.util.Collections;
import java.util.List;

public class OptionsSection extends AbstractSection {
    
    private String title;
    
    private List<Option> options = Collections.emptyList();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.startOptions(this);
        for (Option option : options) {
            option.accept(visitor);
        }
        visitor.endOptions(this);
    }
}
