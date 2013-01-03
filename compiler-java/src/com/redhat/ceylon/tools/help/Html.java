/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
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
