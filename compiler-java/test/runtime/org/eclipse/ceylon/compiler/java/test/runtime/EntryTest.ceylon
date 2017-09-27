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
shared class EntryTest() {
    
    @test
    shared void testKey() {
        Integer key = +1;
        String item = "One";        
        Entry<Integer, String> first = Entry(key,item); 
        assertTrue(key == first.key);    
    }
    
    @test
    shared void testItem() {
        Integer key = +1;
        String item = "One";        
        Entry<Integer, String> first = Entry(key,item); 
        assertTrue(item == first.item);    
    }    
        
    @test
    shared void testEquals() {
        Integer key1 = +1;
        String item1 = "One";
        Entry<Integer, String> first = Entry(key1,item1);
        Entry<Integer, String> anotherFirst = Entry(key1,item1);
        Integer key2 = +2;
        String item2 = "Two";
        Entry<Integer, String> second = Entry(key2,item2);
        assertEquals(first, anotherFirst);
        assertFalse(first == second);
        Entry<Integer, String> mixed = Entry(key1,item2);
        assertFalse(first == mixed);
        assertFalse(second == mixed);
    }
    
    @test
    shared void testHash() {
        Entry<Integer, String> first = Entry(+1,"One");
        Integer firstHash = first.hash;
        assertEquals(first.hash, firstHash);
    }
    
    @test
    shared void testString() {
        Entry<Integer, String> first = Entry(+1,"One");
        String name = first.string;        
    }    
    
}      