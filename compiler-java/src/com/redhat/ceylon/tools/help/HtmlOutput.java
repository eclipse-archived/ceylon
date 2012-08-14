package com.redhat.ceylon.tools.help;

import java.io.StringReader;
import java.io.StringWriter;

import org.tautua.markdownpapers.HtmlEmitter;
import org.tautua.markdownpapers.ast.Document;
import org.tautua.markdownpapers.parser.ParseException;
import org.tautua.markdownpapers.parser.Parser;


class HtmlOutput implements Output, Output.Options, Output.Synopsis, Output.Section {

    private final Html html;
    
    HtmlOutput(Html html) {
        this.html = html;
    }
    
    private String markdown(String markdown) {
        Parser parser = new Parser(new StringReader(markdown));
        Document doc;
        try {
            doc = parser.parse();
        } catch (ParseException e) {
            // Really should at least encode this
            return markdown;
        }
        StringWriter sw = new StringWriter();
        HtmlEmitter markdownVisitor = new HtmlEmitter(sw);
        doc.accept(markdownVisitor);
        return sw.toString();
    }
    
    @Override
    public void title(String title) {
        html.open("html", "head");
        html.open("title").text(title).close("title").text("\n");
        html.tag("link rel='stylesheet'type='text/css' href='doc-tool.css'").text("\n");
        html.close("head").text("\n");
        html.open("h1").text(title).close("h1");
        html.text("\n");
    }

    @Override
    public HtmlOutput section(String title) {
        html.open("div class='section section-"+title+"'");
        html.open("h2").unescaped(markdown(title)).close("h2");
        html.text("\n");
        return this;
    }
    
    @Override
    public void endSection() {
        html.close("div").text("\n\n");
    }
    
    @Override
    public void paragraph(String paraMd) {
        html.unescaped(markdown(paraMd));
    }

    @Override
    public HtmlOutput synopsis(String title) {
        html.open("div class='section section-synopsis'").text("\n");
        html.open("h2").unescaped(markdown(title)).close("h2").text("\n");
        html.open("pre");
        return this;
    }
    
    @Override
    public void endSynopsis() {
        html.close("pre").text("\n");
        html.close("div").text("\n\n");
    }

    @Override
    public void appendSynopsis(String s) {
        html.text(s);
    }

    @Override
    public void longOptionSynopsis(String string) {
        html.link(string, "#long" + string);
    }

    @Override
    public void shortOptionSynopsis(String string) {
        html.link(string, "#short" + string);
    }

    @Override
    public void argumentSynopsis(String name) {
        html.link(name, "#arg" + name);
    }

    @Override
    public HtmlOutput options(String title) {
        html.open("div class='section section-options'").text("\n");
        html.open("h2").text(title).close("h2").text("\n");
        html.open("dl");
        return this;
    }
    
    @Override
    public void endOptions() {
        html.close("dl").text("\n");
        html.close("div").text("\n\n");
        
    }
    
    @Override
    public void option(String shortName, String longName, String argumentName,
            String descriptionMd) {
        html.open("dt class='option'");
        html.open("code id='long" + longName + "'").text(longName);
        if (argumentName != null) {
            html.text("=").text(argumentName);
        }
        html.close("code");
        if (shortName != null) {
            html.text(", ");
            html.open("code id='short" + shortName +"'").text(shortName);
            if (argumentName != null) {
                html.text(" ").text(argumentName);
            }
            html.close("code");
        }
        html.close("dt").text("\n");
        html.open("dd class='option-description'");
        html.unescaped(markdown(descriptionMd));
        html.close("dd").text("\n");
    }

    @Override
    public void end() {
        html.close("body", "html");
    }
    
}
