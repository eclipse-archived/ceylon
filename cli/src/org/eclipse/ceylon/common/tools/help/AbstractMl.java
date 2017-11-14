
package org.eclipse.ceylon.common.tools.help;

import java.io.IOException;

import org.tautua.markdownpapers.HtmlEmitter;
import org.tautua.markdownpapers.ast.Node;

class AbstractMl<M extends AbstractMl<M>> {

    protected final Appendable out;
    private IOException error;

    public AbstractMl(Appendable out) {
        this.out = out;
    }

    protected M append(String s) {
        if (error == null) {
            try {
                out.append(s);
            } catch (IOException e) {
                error = e;
            }
        }
        return (M)this;
    }

    protected M append(char s) {
        if (error == null) {
            try {
                out.append(s);
            } catch (IOException e) {
                error = e;
            }
        }
        return (M)this;
    }

    public M doctype(String type) {
        return append("<!DOCTYPE ").append(type).append('>');
    }

    public M open(String... tags) {
        for (String tag : tags) {
            open(tag);
        }
        return (M)this;
    }

    public M open(String tag) {
        return append('<').append(tag).append('>');
    }

    public M close(String... tags) {
        for (String tag : tags) {
            close(tag);
        }
        return (M)this;
    }

    public M close(String tag) {
        return append("</").append(tag).append('>');
    }

    public M text(String text) {
        return append(
                text.replace("&", "&mp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;"));
        
    }

    public M unescaped(String html) {
        return append(html);
    }

    public M markdown(Node doc) {
        HtmlEmitter markdownVisitor = new HtmlEmitter(out);
        doc.accept(markdownVisitor);
        return (M)this;
    }

    public M tag(String tag) {
        return append('<').append(tag).append("/>");      
    }

}