package com.redhat.ceylon.tools.help;

import java.io.IOException;

import org.tautua.markdownpapers.HtmlEmitter;
import org.tautua.markdownpapers.ast.Node;

class Html {

    private final Appendable out;
    
    private IOException error;
    
    public Html(Appendable out) {
        this.out = out;
    }
    
    private Html append(String s) {
        if (error == null) {
            try {
                out.append(s);
            } catch (IOException e) {
                error = e;
            }
        }
        return this;
    }
    
    private Html append(char s) {
        if (error == null) {
            try {
                out.append(s);
            } catch (IOException e) {
                error = e;
            }
        }
        return this;
    }

    public Html doctype(String type) {
        return append("<!DOCTYPE ").append(type).append('>');
    }
        
    public Html open(String... tags) {
        for (String tag : tags) {
            open(tag);
        }
        return this;
    }

    public Html open(String tag) {
        append('<').append(tag).append('>');
        return this;
    }
    
    public Html close(String... tags) {
        for (String tag : tags) {
            close(tag);
        }
        return this;
    }

    public Html close(String tag) {
        append("</").append(tag).append('>');
        return this;
    }
    
    public Html link(String linkText, String url) {
        return open("a href='" + url + "'").text(linkText).close("a");
    }

    public Html text(String text) {
        return append(
                text.replace("&", "&mp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;"));
        
    }

    public Html unescaped(String html) {
        return append(html);
    }
    
    public Html markdown(Node doc) {
        HtmlEmitter markdownVisitor = new HtmlEmitter(out);
        doc.accept(markdownVisitor);
        return this;
    }

    public Html tag(String tag) {
        append('<').append(tag).append("/>");
        return this;        
    }
    
    
    
}
