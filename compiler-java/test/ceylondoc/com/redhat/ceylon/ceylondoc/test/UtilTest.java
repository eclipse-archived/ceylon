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
package com.redhat.ceylon.ceylondoc.test;

import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.ceylondoc.Util;

public class UtilTest {

    private static Locale defaultLocale;
    
    // The BreakIterator stuff is locale-dependent. To make the tests work
    // in any locale we need to set it to something specific
    @BeforeClass
    public static void setLocale() {
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }
    
    @AfterClass
    public static void restoreLocale() {
        Locale.setDefault(defaultLocale);
    }
    
    @Test
    public void testFirstLine() {
        assertFirstLine("", "");
        assertFirstLine("<p>Blah blah blah</p>", "Blah blah blah");
        assertFirstLine("<p>Blah blah blah.</p>", "Blah blah blah. Foo bar baz.");
        assertFirstLine("<p>Blah blah blah!</p>", "Blah blah blah! Foo bar baz.");
        assertFirstLine("<p>Blah blah e.g. blah!</p>", "Blah blah e.g. blah! Foo bar baz.");
        assertFirstLine("<p>Blah blah i.e. blah?</p>", "Blah blah i.e. blah? Foo bar baz.");
        
        assertFirstLine("<p>Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah</p>", 
                         "Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah");
        assertFirstLine("<p>Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blahh</p>", 
                         "Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blahh");
        assertFirstLine("<p>Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah…</p>", 
                         "Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blahhh");
        assertFirstLine("<p>Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah…</p>", 
                         "Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah");
        
        assertFirstLine("<p>Blah blah blah</p>", "Blah blah blah \n\n Foo bar baz");
        assertFirstLine("<p>Blah blah <em>blah</em>.</p>", "Blah blah *blah*. Foo bar baz.");
        assertFirstLine("<p>Blah <a href=\"http://example.com\">link</a> blah.</p>", "Blah [link][] blah. Foo bar baz \n\n [link]: http://example.com");
        
        assertFirstLine("<p>Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah…</p>", 
                "Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blahhh <!-- xxx -->");
        
        // bug #5999
        assertFirstLine("<p>This interface inherits from CharacterData and represents the content of a comment, i.e., all the characters between…</p>",
                "This interface inherits from CharacterData and represents the content of a comment, i.e., all the characters between the starting '<!--' and ending '-->'.");
    }

    private void assertFirstLine(String expected, String text) {
        Assert.assertEquals(expected, Util.getDocFirstLine(text, null));
    }
    
}