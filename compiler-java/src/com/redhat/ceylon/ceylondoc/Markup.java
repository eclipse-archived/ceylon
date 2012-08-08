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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

public class Markup {

    protected final Writer writer;

    public Markup(Writer writer) {
        super();
        this.writer = writer;
    }

    /**
     * Writes the given strings to the writer
     * @param text The strings to write
     * @throws IOException
     * @see #text(String...)
     */
    protected void write(String... text) throws IOException {
        for (String s : text)
            writer.append(s);
    }

    protected void tag(String... tags) throws IOException {
        for (String tag : tags)
            writer.append("<").append(tag).append("/>\n");
    }

    protected void around(String tag, String... text) throws IOException {
        open(tag);
        for (String s : text)
            writer.append(s);
        int space = tag.indexOf(" ");
        if (space > -1)
            tag = tag.substring(0, space);
        close(tag);
    }

    protected void close(String... tags) throws IOException {
        for (String tag : tags)
            writer.append("</").append(tag).append(">");
    }

    protected void open(String... tags) throws IOException {
        for (String tag : tags)
            writer.append("<").append(tag).append(">");
    }

    protected void openTable(String id, String title) throws IOException {
        String opener = "table class='category'";
        if (id != null) {
            opener += " id='" + id + "'";
        }
        open(opener);
        open("tr class='TableHeadingColor'");
        around("th", title);
        close("tr");
    }

    protected void openTable(String id, String title, String firstColumnTitle, String secondColumnTitle)
            throws IOException {
        String opener = "table class='category'";
        if (id != null) {
            opener += " id='" + id + "'";
        }
        open(opener);
        open("tr class='TableHeadingColor'");
        around("th colspan='2'", title);
        close("tr");
        open("tr class='TableSubHeadingColor'");
        around("th", firstColumnTitle);
        around("th", secondColumnTitle);
        close("tr");
    }

    /**
     * Like {@link #write(String...)}, but HTML encodes the its arguments
     * @param text
     * @throws IOException
     * @see #write(String...)
     */
    protected void text(String... text) throws IOException {
        for (String s : text) {
            write(s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;"));
        }
    }
    
    protected void include(String classpathResource) throws IOException {
        InputStream resource = getClass().getResourceAsStream(classpathResource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
        try {
            String line = reader.readLine();
            while (line != null) {
                write(line);
                line = reader.readLine();
            }
        } finally {
            reader.close();
        }
    }

}