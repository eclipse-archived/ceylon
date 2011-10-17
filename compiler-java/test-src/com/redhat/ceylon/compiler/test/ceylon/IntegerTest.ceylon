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
	
	@test
	shared void testMinus() {
		Integer ten = +10;
		Integer seven = +7;
		Integer minusThree = -3;
		Integer theZero = +0;		
		assertEquals(theZero,ten.minus(ten));
		assertEquals(+3,ten.minus(seven));
		assertEquals(minusThree,seven.minus(ten));
		assertEquals(-10,minusThree.minus(seven));
		assertEquals(+2, +10 - +8);				
	}
	
	@test
	shared void testTimes() {
		Integer ten = +10;
		Integer seven = +7;
		Integer minusThree = -3;
		Integer theZero = +0;		
		assertEquals(theZero, ten.times(theZero));
		assertEquals(theZero, theZero.times(ten));
		assertEquals(+70, ten.times(seven));
		assertEquals(+3, minusThree.times(-1));
		assertEquals(-10, ten.times(-1));
		assertEquals(+24, +3 * +8);
	}	
	
	@test
	shared void testPower() {		
		Integer two = +2;
		Integer four = +4;		
		Integer theZero = +0;		
		assertEquals(+1,two.power(theZero));
		assertEquals(four,two.power(+2));
	}	
	
	@test
	shared void testDivided() {
		Integer ten = +10;		
		Integer two = +2;
		Integer minusOne = -1;
		Integer theZero = +0;		
		assertEquals(theZero,theZero.divided(ten));
		assertEquals(+5,ten.divided(two));
		assertEquals(+1,ten.divided(ten));
		assertEquals(-2,two.divided(-1));
		assertEquals(+1, +4 / +3);
		assertEquals(-1, +4 / -3);
	}		
	
	@test
	shared void testMagnitude() {
		Integer ten = +10;		
		Integer minustwo = -2;		
		Integer theZero = +0;
				
		assertEquals(theZero,theZero.magnitude(theZero));
		
	}				
	
		
}	  