shared class NaturalTest() extends Test() {

	@test
	shared void testEqual() {
		Natural one = 1;
		Natural anotherOne = 1;
		Natural two = 2;
		assertEquals(one, anotherOne);
		assertFalse(one == two);
	}

    @test
    shared void testHash() {
        Natural one = 1;
        Natural anotherOne = 1;
        Natural two = 2;
        assertEquals(one.hash, anotherOne.hash);
        assertFalse(one.hash == two.hash);
    }	
	@test
	shared void testZero() {
		Natural one = 1;
		Natural two = 2;
		Natural zero = 0;
		assertTrue(zero.zero);
		assertFalse(one.zero);
		assertFalse(two.zero);
	}
	
	@test
	shared void testUnit() {
		Natural one = 1;
		Natural two = 2;
		Natural zero = 0;
		assertFalse(zero.unit);
		assertTrue(one.unit);
		assertFalse(two.unit);
	}

	@test
	shared void testRemainder() {
		Natural one = 1;
		Natural two = 2;
		Natural tree = 3;
		Natural four = 4;
		Natural five = 5;
		assertTrue(0 == one.remainder(one));
		assertFalse(one == one.remainder(one));
		assertFalse(one == four.remainder(two));
		assertTrue(one == four.remainder(tree));
		assertTrue(one == five.remainder(two));
	}
	
	@test
	shared void testNegativeValue() {
		Natural positive = 1;		
		Natural zero = 0;		
		Integer negativeZero = zero.negativeValue;
		assertEquals(-0, negativeZero);
		Integer negative = positive.negativeValue;
        assertEquals(-1, negative);
	}
	
	@test
	shared void testPositiveValue() {
		Natural positive = 1;	
		Natural zero = 0;		
		assertEquals(+0, zero.positiveValue);
		assertEquals(+1, positive.positiveValue);
	}
	
	@test
	shared void testMinus() {
		Natural ten = 10;
		Natural seven = 7;
		Natural zero = 0;		
		assertEquals(zero,ten.minus(ten));
    	assertEquals(3,ten.minus(seven));
        assertEquals(seven,seven.minus(zero));
		assertEquals(2, 10 - 8);
		try {
		  Natural negativeError = 1 - 2;
		  fail();		
		} catch (Exception e) {
		  
		}		
	}
    @test
    shared void testPlus() {
        Natural ten = 10;
        Natural zero = 0;
        Natural two = 2;
        Natural twelve = 12;  
        assertEquals(ten, ten.plus(zero));
        assertEquals(10, 10 + 0);
        assertEquals(twelve, ten.plus(two));
        assertEquals(12, 10 + 2);
        
    }	
	
	@test
	shared void testTimes() {
		Natural ten = 10;
		Natural seven = 7;
		Natural zero = 0;		
		assertEquals(zero, ten.times(zero));
		assertEquals(zero, zero.times(ten));
		assertEquals(70, ten.times(seven));
		assertEquals(24, 3 * 8);
	}	
	
	@test
	shared void testPower() {		
		Natural two = 2;
		Natural four = 4;		
		Natural zero = 0;		
		assertEquals(1,two.power(zero));
		assertEquals(four,two.power(2));	
	}	
	
	@test
	shared void testDivided() {
		Natural ten = 10;		
		Natural two = 2;
		Natural zero = 0;		
		assertEquals(zero,zero.divided(ten));
		assertEquals(5,ten.divided(two));
		assertEquals(1,ten.divided(ten));
		assertEquals(1, 4 / 3);
		assertEquals(1, 5 / 3);
		try {
		  Natural dividedByZeroError = 1 / 0;
		  fail();
		} catch (Exception e) {
		}
	}		
	
	@test
	shared void testMagnitude() {
		Natural ten = 10;				
		Natural zero = 0;
		assertEquals(zero,zero.magnitude);
		assertEquals(ten,ten.magnitude);
	}
	
	@test
	shared void testFractionalPart() {
		Natural ten = 10;
		Natural zero = 0;
		assertEquals(zero,zero.fractionalPart);
		assertEquals(zero,ten.fractionalPart);		
	}
	
	@test
	shared void testWholePart() {
		Natural ten = 10;
		Natural zero = 0;
		assertEquals(zero,zero.wholePart);
		assertEquals(ten,ten.wholePart);		
	}	
	
	@test
	shared void testPositive() {
		Natural ten = 10;
		Natural zero = 0;
		assertTrue(ten.positive);		
		assertFalse(zero.positive);
	}		

	@test
	shared void testNegative() {
		Natural ten = 10;		
        Natural zero = 0;	
		assertFalse(ten.negative);
		assertFalse(zero.negative);
	}
	@test
	shared void testFloat() {
		Natural ten = 10;
		Float floatTen = ten.float;			
	}
	
	@test
	shared void testInteger() {
		Natural ten = 10;
		Integer other = ten.integer;
	}
	
	@test
	shared void testNatural() {
		Natural ten = 10;
		Natural other = ten.natural;
		assertEquals(ten, other);			
	}
	
	@test
	shared void testSign() {
		Natural ten = 10;
		Natural zero = 0;
		assertEquals(+0, zero.sign);
		assertEquals(+1, ten.sign);		
	}
	
    @test
    shared void testSuccessor() {
        Natural ten = 10;
        Natural zero = 0;
        assertEquals(11, ten.successor);
        assertFalse(9 == ten.successor);
        assertEquals(1, zero.successor);
        assertFalse(-1 == zero.successor);
    }
    
    @test
    shared void testPredecessor() {
        Natural ten = 10; 
        Natural zero = 0;
        assertFalse(11 == ten.predecessor);
        assertEquals(9, ten.predecessor);
        try {
          Natural predecess = zero.predecessor;
          fail();       
        } catch (Exception e) {
          
        }
    }
    
    @test
    shared void testLargerThan() {
        Natural ten = 10;
        Natural zero = 0;
        assertFalse(zero.largerThan(ten));
        assertFalse(zero.largerThan(zero));
        assertTrue(ten.largerThan(zero));
        assertFalse(ten.largerThan(ten));  
    }   
    
    @test
    shared void testAsLargeAs() {
        Natural ten = 10;
        Natural zero = 0;
        assertFalse(zero.asLargeAs(ten));
        assertTrue(zero.asLargeAs(zero));
        assertTrue(ten.asLargeAs(zero));
        assertTrue(ten.asLargeAs(ten));  
    }  
    @test
    shared void testSmallerThan() {
        Natural ten = 10;
        Natural zero = 0;
        assertTrue(zero.smallerThan(ten));
        assertFalse(zero.smallerThan(zero)); 
        assertFalse(ten.smallerThan(zero));
        assertFalse(ten.smallerThan(ten));  
    }


    @test
    shared void testAsSmallAs() {
        Natural ten = 10;
        Natural zero = 0;
        assertTrue(zero.asSmallAs(ten));
        assertTrue(zero.asSmallAs(zero)); 
        assertFalse(ten.asSmallAs(zero));
        assertTrue(ten.asSmallAs(ten));  
    }

    @test
    shared void testComparisionSmaller() {
        Natural ten = 10;
        Natural zero = 0; 
        assertEquals(smaller, zero.compare(ten));
        assertFalse(smaller == zero.compare(zero));
        assertFalse(smaller == ten.compare(ten));
        assertFalse(smaller == ten.compare(zero));
         
    }
    
    @test
    shared void testComparisionLarger() {
        Natural ten = 10; 
        Natural zero = 0; 
        assertEquals(larger, ten.compare(zero));
        assertFalse(larger == ten.compare(ten));
        assertFalse(larger == zero.compare(zero));
        assertFalse(larger == zero.compare(ten));
    }
    
    @test
    shared void testComparisionEqual() {
        Natural ten = 10;
        Natural zero = 0;
        assertFalse(equal == ten.compare(zero));
        assertEquals(equal, ten.compare(ten));
        assertEquals(equal, zero.compare(zero));
        assertFalse(equal == zero.compare(ten));
    }              	
		
}	  