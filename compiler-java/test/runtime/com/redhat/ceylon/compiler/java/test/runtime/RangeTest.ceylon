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
shared class RangeTest() {

    @test
    shared void testFirst() {    
        Range<Integer> range = Range(1, 10);
        assertEquals(range.first, 1);
    }
    
    @test
    shared void testLast() {    
        Range<Integer> range = Range(1, 10);
        assertEquals(range.last, 10);
    }
    
    @test
    shared void testString() {    
        Range<Integer> range = Range(1, 10);
        String name =  range.string;        
    }
    
    @test
    shared void testSize() {    
        Range<Integer> range = Range(1, 10);
        assertEquals(range.size, 10);        
    }
    
    @test
    shared void testLastIndex() {    
        Range<Integer> range = Range(1, 10);
        assertEquals(range.lastIndex, 9);        
    }
    
    @test
    shared void testRest() {    
        Range<Integer> range = Range(1, 10);
        Integer[] rest = range.rest;
        assertEquals(9, rest.size);
        assertEquals(2, rest[0]);
        assertEquals(3, rest[1]);
        assertEquals(4, rest[2]);
        assertEquals(5, rest[3]);
        assertEquals(6, rest[4]);
        assertEquals(7, rest[5]);
        assertEquals(8, rest[6]);
        assertEquals(9, rest[7]);
        assertEquals(10, rest[8]);
        assertEquals(null, rest[9]);
        
    }
    
    @test
    shared void testItem() {    
        Range<Integer> range = Range(0, 9);
        Integer? first = range.get(1);
        if (exists first) {
            assertEquals(first,1);
        }
        Integer? ten = range.get(10);
        if (exists ten) {
            fail();
        }                        
    }                            
    
    @test
    shared void testContains() {    
        Range<Integer> range = Range(1, 10);
        assertTrue(range.contains(1));
        assertTrue(range.contains(5));
        assertTrue(range.contains(10));
        assertFalse(range.contains(11));
    }
    
    @test
    shared void testDecreasing() {    
        Range<Integer> rangeIncreasing = Range(1, 10);        
        Range<Integer> rangeDecreasing = Range(10, 1);
        assertFalse(rangeIncreasing.decreasing);
        assertTrue(rangeDecreasing.decreasing);
    }
    
    @test
    shared void testEquals() {    
        Range<Integer> oneToTen = Range(1, 10);
        Range<Integer> anotherOneToTen = Range(1, 10);        
        Range<Integer> tenToOne = Range(10, 1);
        assertEquals(oneToTen, anotherOneToTen);
        assertFalse(oneToTen == tenToOne);
    }
    
    @test
    shared void testHash() {    
        Range<Integer> range = Range(1, 10);
        Integer rangeHash = range.hash;
    }        
    
}      