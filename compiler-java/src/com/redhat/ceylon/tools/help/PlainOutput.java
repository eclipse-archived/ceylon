package com.redhat.ceylon.tools.help;

import org.tautua.markdownpapers.ast.Node;

import com.redhat.ceylon.common.tool.WordWrap;

class PlainOutput implements Output, Output.Options, Output.Synopsis {

    private final WordWrap out;
    private int numOptions;
    private String ceylonName;

    PlainOutput(WordWrap wrap) {
        this.out = wrap;
        // TODO Markdown to plain text
    }
    
    private void markdown(Node doc) {
        PlaintextMarkdownVisitor markdownVisitor = new PlaintextMarkdownVisitor(out);
        doc.accept(markdownVisitor);
    }

    @Override
    public void title(String title) {
        // Nothing to do
        this.ceylonName = title;
    }

    @Override
    public void section(Node paraMd) {
        markdown(paraMd);
        out.setIndent(0);
        out.newline();
    }

    @Override
    public Options startOptions(String title) {
        out.append(title.toUpperCase()).newline();
        out.setIndent(8);
        return this;
    }
    
    @Override
    public void option(String shortName, String longName, String argumentName, Node description) {
        numOptions++;
        if (shortName != null) {
            out.append(shortName);
            if (argumentName != null) {
                out.append(" <" + argumentName + ">");
            }
            out.append(", ");
        }
        out.append(longName);
        if (argumentName != null) {
            out.append("=<" + argumentName + ">");
        }
        out.setIndent(12);
        out.newline();
        if (description != null) {
            markdown(description);
        } else {
            out.append("Not documented");
        }
        out.newline();
        out.setIndent(8);
    }
    
    public void endOptions() {
        if(numOptions == 0) {
            out.append(ceylonName+" has no options").newline();
        }
        out.setIndent(0);
        out.newline();
    }

    @Override
    public Synopsis startSynopsis(String title) {
        out.append(title.toUpperCase()).newline();
        out.setIndent(8);
        return this;
    }

    @Override
    public void appendSynopsis(String s) {
        out.append(s);
    }

    @Override
    public void longOptionSynopsis(String string) {
        out.append(string);
    }

    @Override
    public void shortOptionSynopsis(String string) {
        out.append(string);
    }

    @Override
    public void argumentSynopsis(String name) {
        out.append(name);
    }

    @Override
    public void endSynopsis() {
        out.setIndent(0);
        out.newline();
        out.newline();        
    }

    @Override
    public void end() {
        out.flush();
    }

    
}
