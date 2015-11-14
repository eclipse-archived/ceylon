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

package com.redhat.ceylon.ceylondoc;

import java.io.IOException;
import java.io.Writer;

public class Markup {

    protected final Writer writer;

    public Markup(Writer writer) {
        super();
        this.writer = writer;
    }

    protected void write(String... text) throws IOException {
        for (String s : text) {
            writer.append(s);
        }
    }

    protected void tag(String... tags) throws IOException {
        for (String tag : tags) {
            writer.append("<").append(tag).append("/>\n");
        }
    }

    protected void around(String tag, String... text) throws IOException {
        open(tag);
        for (String s : text) {
            writer.append(s);
        }
        int space = tag.indexOf(" ");
        if (space > -1) {
            tag = tag.substring(0, space);
        }
        close(tag);
    }

    protected void close(String... tags) throws IOException {
        for (String tag : tags) {
            writer.append("</").append(tag).append(">");
        }
    }

    protected void open(String... tags) throws IOException {
        for (String tag : tags) {
            writer.append("<").append(tag).append(">");
        }
    }

    protected void openTable(String id, String title, int columnCount, boolean isBodyExpanded) throws IOException {
        open("table id='" + id + "' class='table table-condensed table-bordered table-hover'");
        open("thead");
        
        open("tr class='table-header' title='Click for expand/collapse'");
        open("td colspan='" + columnCount + "'");
        if( isBodyExpanded ) {
            around("i class='icon-expand'");
        } else {
            around("i class='icon-collapse'");
        }
        write(title);
        close("td");
        close("tr");
        
        close("thead");
        if(isBodyExpanded) {
            open("tbody");
        } else {
            open("tbody style='display: none'");
        }
    }
    
    protected void closeTable() throws IOException {
        close("tbody", "table");
    }

    protected void text(String... text) throws IOException {
        for (String s : text) {
            write(s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;"));
        }
    }

}