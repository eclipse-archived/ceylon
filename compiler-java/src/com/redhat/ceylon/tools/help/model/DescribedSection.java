package com.redhat.ceylon.tools.help.model;

import org.tautua.markdownpapers.ast.Node;

public class DescribedSection implements Documentation {

    public static enum Role {
        SUMMARY,
        DESCRIPTION,
        ADDITIONAL
    }
    
    private Role role;
    
    private Node description;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Node getDescription() {
        return description;
    }

    public void setDescription(Node content) {
        this.description = content;
    }

    @Override
    public void accept(Visitor visitor) {
        switch (role) {
        case SUMMARY:
            visitor.visitSummary(this);
            break;
        case DESCRIPTION:
            visitor.visitDescription(this);
            break;
        case ADDITIONAL:
            visitor.visitAdditionalSection(this);
            break;
        default:
            throw new RuntimeException();
        }
    }
    
}
