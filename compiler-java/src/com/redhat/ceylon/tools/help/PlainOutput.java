package com.redhat.ceylon.tools.help;

import java.io.StringReader;
import java.io.StringWriter;

import org.tautua.markdownpapers.ast.Document;
import org.tautua.markdownpapers.parser.ParseException;
import org.tautua.markdownpapers.parser.Parser;

import com.redhat.ceylon.common.tool.WordWrap;

class PlainOutput implements Output, Output.Options, Output.Synopsis, Output.Section {

    private final WordWrap out;
    private int numOptions;
    private String ceylonName;

    PlainOutput(WordWrap wrap) {
        this.out = wrap;
        // TODO Markdown to plain text
    }
    
    private String markdown(String markdown) {
        Parser parser = new Parser(new StringReader(markdown));
        
        Document doc;
        try {
            doc = parser.parse();
        } catch (ParseException e) {
            return markdown;
        }
        StringWriter sw = new StringWriter();
        PlaintextMarkdownVisitor markdownVisitor = new PlaintextMarkdownVisitor(out);
        doc.accept(markdownVisitor);
        return sw.toString();
    }

    @Override
    public void title(String title) {
        // Nothing to do
        this.ceylonName = title;
    }

    @Override
    public PlainOutput section(String title) {
        out.append(title.toUpperCase()).newline();
        return this;
    }
    
    @Override
    public void endSection() {
        out.newline();
        out.newline();
    }
    
    @Override
    public void paragraph(String paraMd) {
        out.setIndent(8);
        out.append(markdown(paraMd));
        out.setIndent(0);
    }

    @Override
    public Options options(String title) {
        out.append(title).newline();
        out.setIndent(8);
        return this;
    }
    
    @Override
    public void option(String shortName, String longName, String argumentName, String description) {
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
            out.append(description);
        } else {
            out.append("Not documented");
        }
        out.newline();
        out.setIndent(8);
        out.newline();
    }
    
    public void endOptions() {
        if(numOptions == 0) {
            out.append(ceylonName+" has no options").newline();
        }
        out.setIndent(0);
        out.newline();
    }

    @Override
    public Synopsis synopsis(String title) {
        out.append(title).newline();
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
