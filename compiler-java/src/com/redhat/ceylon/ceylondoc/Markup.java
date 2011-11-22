/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Markup {

    protected Writer writer;
    protected final File file;

    public Markup(File file) {
        super();
        this.file = file;
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

    protected void openTable(String title) throws IOException {
        open("table class='category'");
        open("tr class='TableHeadingColor'");
        around("th", title);
        close("tr");
    }

    protected void openTable(String title, String firstColumnTitle, String secondColumnTitle)
            throws IOException {
        open("table class='category' id='"+title.toLowerCase()+"'");
        open("tr class='TableHeadingColor'");
        around("th colspan='2'", title);
        close("tr");
        open("tr class='TableSubHeadingColor'");
        around("th", firstColumnTitle);
        around("th", secondColumnTitle);
        close("tr");
    }

    protected void setupWriter() throws IOException {
        File dir = file.getParentFile();
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Couldn't create directory for file: " + file);
        }
        this.writer = new FileWriter(file);
    }

    /**
     * Like {@link #write(String...)}, but HTML encodes the its arguments
     * @param text
     * @throws IOException
     * @see #write(String...)
     */
    public void text(String... text) throws IOException {
        for (String s : text) {
            write(s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;"));
        }
    }

}