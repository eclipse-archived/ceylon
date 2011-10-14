shared class IntegerTest() extends Test() {

	@test
	shared void testEqual() {
		Integer one = +1;
		Integer anotherOne = +1;
		Integer two = +2;
		assertTrue(one == anotherOne);
		assertFalse(one == two);
	}
	
	@test
	shared void testZero() {
		Integer intOne = +1;
		Integer intTwo = +2;
		Integer intZero = +0;
		assertTrue(intZero.zero);
		assertFalse(intOne.zero);
		assertFalse(intTwo.zero);
	}
	
	@test
	shared void testUnit() {
		Integer intOne = +1;
		Integer intTwo = +2;
		Integer intZero = +0;
		assertFalse(intZero.unit);
		assertTrue(intOne.unit);
		assertFalse(intTwo.unit);
	}

	@test
	shared void testRemainder() {
		Integer intOne = +1;
		Integer intTwo = +2;
		Integer intTree = +3;
		Integer intFour = +4;
		Integer intFive = +5;
		assertEquals(+0,intOne.remainder(intOne));
		assertFalse(intOne == intOne.remainder(intOne));
		assertFalse(intOne == intFour.remainder(intTwo));
		assertTrue(intOne == intFour.remainder(intTree));
		assertTrue(intOne == intFive.remainder(intTwo));
	}
	
	@test
	shared void testNegativeValue() {
		Integer positive = +1;
		Integer negative = -1;
		Integer theZero = +0;		
		assertEquals(theZero,theZero.negativeValue);
		assertEquals(negative,positive.negativeValue);
		assertEquals(positive,negative.negativeValue);
		assertFalse(negative == negative.negativeValue);
		assertFalse(positive == positive.negativeValue);
	}
	
	@test
	shared void testPositiveValue() {
		Integer positive = +1;
		Integer negative = -1;
		Integer theZero = +0;		
		assertEquals(theZero,theZero.positiveValue);
		assertEquals(positive,positive.positiveValue);
		assertFalse(positive == negative.positiveValue);
		assertEquals(negative,negative.positiveValue);
		assertFalse(negative == positive.positiveValue);
	}
	
		
}	  