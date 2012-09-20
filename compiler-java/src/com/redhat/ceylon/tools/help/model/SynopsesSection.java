package com.redhat.ceylon.tools.help.model;

import java.util.Collections;
import java.util.List;

public class SynopsesSection implements Documentation {
    
    private String title;
    
    private List<Synopsis> synopses = Collections.emptyList();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Synopsis> getSynopses() {
        return synopses;
    }

    public void setSynopses(List<Synopsis> synopses) {
        this.synopses = synopses;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.startSynopses(this);
        for (Synopsis synopsis : synopses) {
            synopsis.accept(visitor);
        }
        visitor.endSynopses(this);
    }
}
