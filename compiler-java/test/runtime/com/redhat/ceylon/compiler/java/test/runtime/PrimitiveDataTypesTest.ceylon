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
shared class PrimitiveDataTypesTest () {
  
  @test
  shared void testNumbers(){
    variable Integer i = +2;
    assertEquals(+2, i);
    assertTrue(+2 == i);
    assertTrue(i == +2);
    assertFalse(i == +3);
    assertTrue(i != +3);

    i = +3;
    assertEquals(+3, i);
    assertTrue(+3 == i);
    assertTrue(i == +3);
    assertFalse(i == +2);
    assertTrue(i != +2);
    
    assertTrue(i == i);
    assertTrue(+3 == +3);
    
    assertFalse(+3 == +2);
    assertTrue(+3 != +2);
    
    variable Integer j = i + i;
    assertTrue(j == +6);

    j = i * i;
    assertTrue(j == +9);

    j = i - i;
    assertTrue(j == +0);

    j = i / i;
    assertTrue(j == +1);

    j = i % i;
    assertTrue(j == +0);

    j = i ** i;
    assertTrue(j == +27);

    j = +i;
    assertTrue(j == +3);

    j = -i;
    assertTrue(j == -3);

    assertTrue(+2 < +3);
    assertFalse(+2 > +3);

    assertTrue(+3 >= +3);
    assertTrue(+3 <= +3);
    assertFalse(+3 > +3);
    assertFalse(+3 < +3);
  }
  
  @test
  shared void testBooleans(){
    assertTrue(true);
    assertTrue(!false);
    assertFalse(false);
    assertFalse(!true);

    variable Boolean t = true;
    assertTrue(t);
    assertFalse(!t);

    variable Boolean f = false;
    assertFalse(f);
    assertTrue(!f);
    
    assertTrue(t && !f);
    assertTrue(!f && t);
    assertFalse(t && f);
    assertFalse(f && t);
    
    assertTrue(t || f);
    assertTrue(f || t);
    assertTrue(t || t);
    assertFalse(f || f);
    
    t ||= true;
    assertTrue(t);
    t ||= false;
    assertTrue(t);

    f ||= false;
    assertFalse(f);
    f ||= true;
    assertTrue(f);
    // reset
    f = false;
    
    t &&= true;
    assertTrue(t);
    t &&= false;
    assertFalse(t);

    f &&= true;
    assertFalse(f);
    f &&= false;
    assertFalse(f);
  }
}