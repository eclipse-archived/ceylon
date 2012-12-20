package com.redhat.ceylon.tools.help.model;

import java.util.Collections;
import java.util.List;

import org.tautua.markdownpapers.ast.Node;

public class DescribedSection implements Documentation {

    public static enum Role {
        DESCRIPTION,
        ADDITIONAL
    }
    
    private Role role;
    
    private Node title;
    
    private Object about;
    
    private Node description;
    
    private List<DescribedSection> subsections = Collections.emptyList();

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Node getTitle() {
        return title;
    }

    public void setTitle(Node title) {
        this.title = title;
    }

    public Node getDescription() {
        return description;
    }

    public void setDescription(Node content) {
        this.description = content;
    }

    public Object getAbout() {
        return about;
    }

    public void setAbout(Object about) {
        this.about = about;
    }

    public List<DescribedSection> getSubsections() {
        return subsections;
    }

    public void setSubsections(List<DescribedSection> subsections) {
        this.subsections = subsections;
    }

    @Override
    public void accept(Visitor visitor) {
        switch (role) {
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
