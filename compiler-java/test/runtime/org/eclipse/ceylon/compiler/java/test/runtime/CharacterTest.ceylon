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

shared class CharacterTest() {

    @test
    shared void testLowercased() {
        Character testChar = 'A';
        assertEquals('a',testChar.lowercased);
    }
    
    @test
    shared void testUppercased() {
        Character testChar = 'a';
        assertEquals('A',testChar.uppercased);
    }
    
    @test
    shared void testUppercase() {
        Character testCharLower = 'a';
        Character testCharUpper = 'A';
        Character testCharDigit = '1';
        assertFalse(testCharLower.uppercase);
        assertTrue(testCharUpper.uppercase);
        assertFalse(testCharDigit.uppercase);
    }
    
    @test
    shared void testLowercase() {
        Character testCharLower = 'a';
        Character testCharUpper = 'A';
        Character testCharDigit = '1';
        assertTrue(testCharLower.lowercase);
        assertFalse(testCharUpper.lowercase);
        assertFalse(testCharDigit.lowercase);
    }
    
    @test
    shared void testDigit() {
        Character testCharLower = 'a';
        Character testCharUpper = 'A';
        Character testCharDigit = '1';
        assertFalse(testCharLower.digit);
        assertFalse(testCharUpper.digit);
        assertTrue(testCharDigit.digit);
    }
    
    @test
    shared void testLetter() {
        Character testChar = 'a';    
        Character testDigit = '1';
        Character testWhite = ' ';
        assertTrue(testChar.letter);
        assertFalse(testDigit.letter);
        assertFalse(testWhite.letter);
    }
    
    @test
    shared void testWhite() {
        Character testChar = 'a';    
        Character testDigit = '1';
        Character testWhite = ' ';
        assertFalse(testChar.whitespace);
        assertFalse(testDigit.whitespace);
        assertTrue(testWhite.whitespace);
    }    
    
}      