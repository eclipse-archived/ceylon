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
shared class RangeTest() extends Test() {

	@test
	shared void testFirst() {	
		Range<Natural> range = Range(1, 10);
		assertEquals(range.first, 1);
	}
	
	@test
	shared void testLast() {	
		Range<Natural> range = Range(1, 10);
		assertEquals(range.last, 10);
	}
	
	@test
	shared void testString() {	
		Range<Natural> range = Range(1, 10);
		String name =  range.string;		
	}
	
	@test
	shared void testSize() {	
		Range<Natural> range = Range(1, 10);
		assertEquals(range.size, 10);		
	}
	
	@test
	shared void testLastIndex() {	
		Range<Natural> range = Range(1, 10);
		assertEquals(range.lastIndex, 9);		
	}
	
	@test
	shared void testRest() {	
		Range<Natural> range = Range(1, 10);
		// TODO!
		fail();				
	}
	
	@test
	shared void testItem() {	
		Range<Natural> range = Range(0, 9);
		Natural? first = range.item(1);
		if (exists first) {
			assertEquals(first,1);
		}
		Natural? ten = range.item(10);
		if (exists ten) {
			fail();
		}						
	}							
	
	@test
	shared void testContains() {	
		Range<Natural> range = Range(1, 10);
		assertTrue(range.contains(1));
		assertTrue(range.contains(5));
		assertTrue(range.contains(10));
		assertFalse(range.contains(11));
	}
	
	@test
	shared void testDecreasing() {	
		Range<Natural> rangeIncreasing = Range(1, 10);		
		Range<Natural> rangeDecreasing = Range(10, 1);
		assertFalse(rangeIncreasing.decreasing);
		assertTrue(rangeDecreasing.decreasing);
	}
	
	@test
	shared void testEquals() {	
		Range<Natural> oneToTen = Range(1, 10);
		Range<Natural> anotherOneToTen = Range(1, 10);		
		Range<Natural> tenToOne = Range(10, 1);
		assertEquals(oneToTen, anotherOneToTen);
		assertFalse(oneToTen == tenToOne);
	}
	
	@test
	shared void testHash() {	
		Range<Natural> range = Range(1, 10);
		Integer rangeHash = range.hash;
	}		
	
}	  