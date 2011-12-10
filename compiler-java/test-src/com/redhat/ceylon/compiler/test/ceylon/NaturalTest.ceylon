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
shared class IntegerTest() extends Test() {

	@test
	shared void testEqual() {
		Integer one = 1;
		Integer anotherOne = 1;
		Integer two = 2;
		assertEquals(one, anotherOne);
		assertFalse(one == two);
	}

    @test
    shared void testHash() {
        Integer one = 1;
        Integer anotherOne = 1;
        Integer two = 2;
        assertEquals(one.hash, anotherOne.hash);
        assertFalse(one.hash == two.hash);
    }	
	@test
	shared void testZero() {
		Integer one = 1;
		Integer two = 2;
		Integer zero = 0;
		assertTrue(zero.zero);
		assertFalse(one.zero);
		assertFalse(two.zero);
	}
	
	@test
	shared void testUnit() {
		Integer one = 1;
		Integer two = 2;
		Integer zero = 0;
		assertFalse(zero.unit);
		assertTrue(one.unit);
		assertFalse(two.unit);
	}

	@test
	shared void testRemainder() {
		Integer one = 1;
		Integer two = 2;
		Integer tree = 3;
		Integer four = 4;
		Integer five = 5;
		assertTrue(0 == one.remainder(one));
		assertFalse(one == one.remainder(one));
		assertFalse(one == four.remainder(two));
		assertTrue(one == four.remainder(tree));
		assertTrue(one == five.remainder(two));
	}
	
	@test
	shared void testNegativeValue() {
		Integer positive = 1;		
		Integer zero = 0;		
		Integer negativeZero = zero.negativeValue;
		assertEquals(-0, negativeZero);
		Integer negative = positive.negativeValue;
        assertEquals(-1, negative);
	}
	
	@test
	shared void testPositiveValue() {
		Integer positive = 1;	
		Integer zero = 0;		
		assertEquals(+0, zero.positiveValue);
		assertEquals(+1, positive.positiveValue);
	}
	
	@test
	shared void testMinus() {
		Integer ten = 10;
		Integer seven = 7;
		Integer zero = 0;		
		assertEquals(zero,ten.minus(ten));
    	assertEquals(3,ten.minus(seven));
        assertEquals(seven,seven.minus(zero));
		assertEquals(2, 10 - 8);
		try {
		  Integer negativeError = 1 - 2;
		  fail();		
		} catch (Exception e) {
		  
		}		
	}
    @test
    shared void testPlus() {
        Integer ten = 10;
        Integer zero = 0;
        Integer two = 2;
        Integer twelve = 12;  
        assertEquals(ten, ten.plus(zero));
        assertEquals(10, 10 + 0);
        assertEquals(twelve, ten.plus(two));
        assertEquals(12, 10 + 2);
        
    }	
	
	@test
	shared void testTimes() {
		Integer ten = 10;
		Integer seven = 7;
		Integer zero = 0;		
		assertEquals(zero, ten.times(zero));
		assertEquals(zero, zero.times(ten));
		assertEquals(70, ten.times(seven));
		assertEquals(24, 3 * 8);
	}	
	
	@test
	shared void testPower() {		
		Integer two = 2;
		Integer four = 4;		
		Integer zero = 0;		
		assertEquals(1,two.power(zero));
		assertEquals(four,two.power(2));	
	}	
	
	@test
	shared void testDivided() {
		Integer ten = 10;		
		Integer two = 2;
		Integer zero = 0;		
		assertEquals(zero,zero.divided(ten));
		assertEquals(5,ten.divided(two));
		assertEquals(1,ten.divided(ten));
		assertEquals(1, 4 / 3);
		assertEquals(1, 5 / 3);
		try {
		  Integer dividedByZeroError = 1 / 0;
		  fail();
		} catch (Exception e) {
		}
	}		
	
	@test
	shared void testMagnitude() {
		Integer ten = 10;				
		Integer zero = 0;
		assertEquals(zero,zero.magnitude);
		assertEquals(ten,ten.magnitude);
	}
	
	@test
	shared void testFractionalPart() {
		Integer ten = 10;
		Integer zero = 0;
		assertEquals(zero,zero.fractionalPart);
		assertEquals(zero,ten.fractionalPart);		
	}
	
	@test
	shared void testWholePart() {
		Integer ten = 10;
		Integer zero = 0;
		assertEquals(zero,zero.wholePart);
		assertEquals(ten,ten.wholePart);		
	}	
	
	@test
	shared void testPositive() {
		Integer ten = 10;
		Integer zero = 0;
		assertTrue(ten.positive);		
		assertFalse(zero.positive);
	}		

	@test
	shared void testNegative() {
		Integer ten = 10;		
        Integer zero = 0;	
		assertFalse(ten.negative);
		assertFalse(zero.negative);
	}
	@test
	shared void testFloat() {
		Integer ten = 10;
		Float floatTen = ten.float;			
	}
	
	@test
	shared void testInteger() {
		Integer ten = 10;
		Integer other = ten.integer;
	}
	
	@test
	shared void testInteger() {
		Integer ten = 10;
		Integer other = ten.natural;
		assertEquals(ten, other);			
	}
	
	@test
	shared void testSign() {
		Integer ten = 10;
		Integer zero = 0;
		assertEquals(+0, zero.sign);
		assertEquals(+1, ten.sign);		
	}
	
    @test
    shared void testSuccessor() {
        Integer ten = 10;
        Integer zero = 0;
        assertEquals(11, ten.successor);
        assertFalse(9 == ten.successor);
        assertEquals(1, zero.successor);
        assertFalse(-1 == zero.successor);
    }
    
    @test
    shared void testPredecessor() {
        Integer ten = 10; 
        Integer zero = 0;
        assertFalse(11 == ten.predecessor);
        assertEquals(9, ten.predecessor);
        try {
          Integer predecess = zero.predecessor;
          fail();       
        } catch (Exception e) {
          
        }
    }
    
    @test
    shared void testLargerThan() {
        Integer ten = 10;
        Integer zero = 0;
        assertFalse(zero > ten);
        assertFalse(zero > zero);
        assertTrue(ten > zero);
        assertFalse(ten > ten);  
    }   
    
    @test
    shared void testAsLargeAs() {
        Integer ten = 10;
        Integer zero = 0;
        assertFalse(zero >= ten);
        assertTrue(zero >= zero);
        assertTrue(ten >= zero);
        assertTrue(ten >= ten);  
    }  
    @test
    shared void testSmallerThan() {
        Integer ten = 10;
        Integer zero = 0;
        assertTrue(zero < ten);
        assertFalse(zero < zero); 
        assertFalse(ten < zero);
        assertFalse(ten < ten);  
    }


    @test
    shared void testAsSmallAs() {
        Integer ten = 10;
        Integer zero = 0;
        assertTrue(zero <= ten);
        assertTrue(zero <= zero); 
        assertFalse(ten <= zero);
        assertTrue(ten <= ten);  
    }

    @test
    shared void testComparisionSmaller() {
        Integer ten = 10;
        Integer zero = 0; 
        assertEquals(smaller, zero.compare(ten));
        assertFalse(smaller == zero.compare(zero));
        assertFalse(smaller == ten.compare(ten));
        assertFalse(smaller == ten.compare(zero));
         
    }
    
    @test
    shared void testComparisionLarger() {
        Integer ten = 10; 
        Integer zero = 0; 
        assertEquals(larger, ten.compare(zero));
        assertFalse(larger == ten.compare(ten));
        assertFalse(larger == zero.compare(zero));
        assertFalse(larger == zero.compare(ten));
    }
    
    @test
    shared void testComparisionEqual() {
        Integer ten = 10;
        Integer zero = 0;
        assertFalse(equal == ten.compare(zero));
        assertEquals(equal, ten.compare(ten));
        assertEquals(equal, zero.compare(zero));
        assertFalse(equal == zero.compare(ten));
    }              	
		
}	  