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