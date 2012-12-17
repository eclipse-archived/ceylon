package com.redhat.ceylon.tools.help.model;

import org.tautua.markdownpapers.ast.Node;

public class SummarySection implements Documentation {

    private Node title;
    
    private String summary;

    public Node getTitle() {
        return title;
    }

    public void setTitle(Node title) {
        this.title = title;
    }

    public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
    public void accept(Visitor visitor) {
        visitor.visitSummary(this);
    }
}
