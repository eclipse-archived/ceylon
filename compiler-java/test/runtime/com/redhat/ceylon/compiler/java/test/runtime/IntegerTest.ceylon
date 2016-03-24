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
shared class IntegerTest() {

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
        Integer intOne = 1;
        Integer intTwo = 2;
        Integer intZero = 0;
        assertTrue(intZero.zero);
        assertFalse(intOne.zero);
        assertFalse(intTwo.zero);
    }
    
    @test
    shared void testUnit() {
        Integer intOne = 1;
        Integer intTwo = 2;
        Integer intZero = 0;
        assertFalse(intZero.unit);
        assertTrue(intOne.unit);
        assertFalse(intTwo.unit);
    }

    @test
    shared void testRemainder() {
        Integer intOne = 1;
        Integer intTwo = 2;
        Integer intTree = 3;
        Integer intFour = 4;
        Integer intFive = 5;
        assertEquals(0,intOne.remainder(intOne));
        assertFalse(intOne == intOne.remainder(intOne));
        assertFalse(intOne == intFour.remainder(intTwo));
        assertTrue(intOne == intFour.remainder(intTree));
        assertTrue(intOne == intFive.remainder(intTwo));
    }
    
    @test
    shared void testNegativeValue() {
        Integer positive = 1;
        Integer negative = -1;
        Integer theZero = 0;
        assertEquals(theZero,theZero.negated);
        assertEquals(negative,positive.negated);
        assertEquals(positive,negative.negated);
        assertFalse(negative == negative.negated);
        assertFalse(positive == positive.negated);
    }
    
    @test
    shared void testMinus() {
        Integer ten = 10;
        Integer seven = 7;
        Integer minusThree = -3;
        Integer theZero = 0;        
        assertEquals(theZero,ten.minus(ten));
        assertEquals(3,ten.minus(seven));
        assertEquals(minusThree,seven.minus(ten));
        assertEquals(-10,minusThree.minus(seven));
        assertEquals(2, 10 - 8);                
    }
    
    @test
    shared void testPlus() {
        Integer ten = 10;      
        Integer minusTwo = -2;
        Integer theZero = 0;       
        assertEquals(ten, ten.plus(theZero));
        assertEquals(10, 10 + (0));
        assertEquals(10, 10 + (-0));
        assertEquals(8, ten.plus(minusTwo));
        assertEquals(8, 10 + (-2));
        assertEquals(minusTwo, minusTwo.plus(theZero));
        assertEquals(-2, -2 + (0));
        assertEquals(-4, -2 + (-2));   
    }    
    
    @test
    shared void testTimes() {
        Integer ten = 10;
        Integer seven = 7;
        Integer minusThree = -3;
        Integer theZero = 0;        
        assertEquals(theZero, ten.times(theZero));
        assertEquals(theZero, theZero.times(ten));
        assertEquals(70, ten.times(seven));
        assertEquals(3, minusThree.times(-1));
        assertEquals(-10, ten.times(-1));
        assertEquals(24, 3 * 8);
    }    
    
    @test
    shared void testPower() {        
        Integer two = 2;
        Integer four = 4;        
        Integer theZero = 0;        
        assertEquals(1,two.power(theZero));
        assertEquals(four,two.power(2));
    }    
    
    @test
    shared void testDivided() {
        Integer ten = 10;        
        Integer two = 2;
        Integer minusOne = -1;
        Integer theZero = 0;        
        assertEquals(theZero,theZero.divided(ten));
        assertEquals(5,ten.divided(two));
        assertEquals(1,ten.divided(ten));
        assertEquals(-2,two.divided(-1));
        assertEquals(1, 4 / 3);
        assertEquals(-1, 4 / -3);
        try {
          Integer dividedByZeroError = 1 / (0);
          fail();
        } catch (Exception e) {
        }
    }        
    
    @test
    shared void testMagnitude() {
        Integer ten = 10;        
        Integer minusTwo = -2;        
        Integer minusZero = -0;                
        Integer theZero = 0;
        assertEquals(theZero,theZero.magnitude);
        assertEquals(theZero,minusZero.magnitude);
        assertEquals(ten,ten.magnitude);        
        assertEquals(2,minusTwo.magnitude);
        assertEquals(4,(-4).magnitude);
        assertEquals(4,(4).magnitude);
    }
    
    @test
    shared void testFractionalPart() {
        Integer ten = 10;        
        Integer minusTwo = -2;
        Integer theZero = 0;
        assertEquals(theZero,theZero.fractionalPart);
        assertEquals(theZero,minusTwo.fractionalPart);
        assertEquals(theZero,ten.fractionalPart);        
    }
    
    @test
    shared void testWholePart() {
        Integer ten = 10;        
        Integer minusTwo = -2;
        Integer theZero = 0;
        assertEquals(theZero,theZero.wholePart);
        assertEquals(minusTwo,minusTwo.wholePart);
        assertEquals(ten,ten.wholePart);        
    }    
    
    @test
    shared void testPositive() {
        Integer ten = 10;        
        Integer minusTwo = -2;        
        assertFalse(minusTwo.positive);
        assertTrue(ten.positive);        
        assertFalse((0).positive);
        assertFalse((-0).positive);
    }        

    @test
    shared void testNegative() {
        Integer ten = 10;        
        Integer minusTwo = -2;        
        assertFalse(ten.negative);
        assertTrue(minusTwo.negative);
        assertFalse((0).negative);
        assertFalse((-0).negative);        
    }
    
    @test
    shared void testFloat() {
        Integer ten = 10;
        Float floatTen = ten.float;            
    }
    
    @test
    shared void testSign() {
        Integer ten = 10;        
        Integer minusTwo = -2;
        Integer theZero = 0;
        assertEquals(0, theZero.sign);
        assertEquals(1, ten.sign);
        assertEquals(-1, minusTwo.sign);            
    }
    
    @test
    shared void testSuccessor() {
        Integer ten = 10;      
        Integer minusTwo = -2;
        Integer theZero = 0;
        assertEquals(11, ten.successor);
        assertFalse(9 == ten.successor);
        assertEquals(-1, minusTwo.successor);
        assertFalse(-3 == minusTwo.successor);
        assertEquals(1, theZero.successor);
        assertFalse(-1 == theZero.successor);
    }
    
    @test
    shared void testPredecessor() {
        Integer ten = 10;      
        Integer minusTwo = -2;
        Integer theZero = 0;
        assertFalse(11 == ten.predecessor);
        assertEquals(9, ten.predecessor);
        assertFalse(-1 == minusTwo.predecessor);
        assertEquals(-3, minusTwo.predecessor);
        assertFalse(1 == theZero.predecessor);
        assertEquals(-1,theZero.predecessor);
    }
    
    @test
    shared void testLargerThan() {
        Integer ten = 10;      
        Integer minusTwo = -2;
        Integer theZero = 0;
        assertFalse(theZero > ten);
        assertTrue(theZero > minusTwo);
        assertFalse(theZero > theZero);        
        assertFalse(minusTwo > minusTwo);             
        assertFalse(minusTwo > theZero);
        assertFalse(minusTwo > ten);
        assertTrue(ten > theZero);
        assertTrue(ten > minusTwo);
        assertFalse(ten > ten);  
    }   
    
    @test
    shared void testAsLargeAs() {
        Integer ten = 10;      
        Integer minusTwo = -2;
        Integer theZero = 0;
        assertFalse(theZero >= ten);
        assertTrue(theZero >= minusTwo);
        assertTrue(theZero >= theZero);        
        assertTrue(minusTwo >= minusTwo);             
        assertFalse(minusTwo >= theZero);
        assertFalse(minusTwo >= ten);
        assertTrue(ten >= theZero);
        assertTrue(ten >= minusTwo);
        assertTrue(ten >= ten);  
    }  
    
    @test
    shared void testSmallerThan() {
        Integer ten = 10;      
        Integer minusTwo = -2;
        Integer theZero = 0;
        assertTrue(theZero < ten);
        assertFalse(theZero < minusTwo);
        assertFalse(theZero < theZero);        
        assertFalse(minusTwo < minusTwo);             
        assertTrue(minusTwo < theZero);
        assertTrue(minusTwo < ten);
        assertFalse(ten < theZero);
        assertFalse(ten < minusTwo);
        assertFalse(ten < ten);  
    }


    @test
    shared void testAsSmallAs() {
        Integer ten = 10;      
        Integer minusTwo = -2;
        Integer theZero = 0;
        assertTrue(theZero <= ten);
        assertFalse(theZero <= minusTwo);
        assertTrue(theZero <= theZero);        
        assertTrue(minusTwo <= minusTwo);             
        assertTrue(minusTwo <= theZero);
        assertTrue(minusTwo <= ten);
        assertFalse(ten <= theZero);
        assertFalse(ten <= minusTwo);
        assertTrue(ten <= ten);  
    }

    @test
    shared void testComparisionSmaller() {
        Integer ten = 10;      
        Integer minusTwo = -2;
        Integer theZero = 0;        
        assertEquals(smaller, minusTwo.compare(ten));
        assertEquals(smaller, minusTwo.compare(theZero));
        assertFalse(smaller == minusTwo.compare(minusTwo));
        assertEquals(smaller, theZero.compare(ten));
        assertFalse(smaller == theZero.compare(minusTwo));
        assertFalse(smaller == theZero.compare(theZero));
        assertFalse(smaller == ten.compare(ten));
        assertFalse(smaller == ten.compare(theZero));
        assertFalse(smaller == ten.compare(minusTwo));          
    }
    
    @test
    shared void testComparisionLarger() {
        Integer ten = 10;      
        Integer minusTwo = -2;
        Integer theZero = 0;        
        assertEquals(larger, ten.compare(minusTwo));
        assertEquals(larger, ten.compare(theZero));
        assertFalse(larger == ten.compare(ten));
        assertEquals(larger, theZero.compare(minusTwo));
        assertFalse(larger == theZero.compare(theZero));
        assertFalse(larger == theZero.compare(ten));
        assertFalse(larger == minusTwo.compare(minusTwo));
        assertFalse(larger == minusTwo.compare(theZero));
        assertFalse(larger == minusTwo.compare(ten));                           
    }
    
    @test
    shared void testComparisionEqual() {
        Integer ten = 10;      
        Integer minusTwo = -2;
        Integer theZero = 0;        
        assertFalse(equal == ten.compare(minusTwo));
        assertFalse(equal == ten.compare(theZero));
        assertEquals(equal, ten.compare(ten));
        assertFalse(equal == theZero.compare(minusTwo));
        assertEquals(equal, theZero.compare(theZero));
        assertFalse(equal == theZero.compare(ten));
        assertEquals(equal, minusTwo.compare(minusTwo));
        assertFalse(equal == minusTwo.compare(theZero));
        assertFalse(equal == minusTwo.compare(ten));                           
    }                  
        
}      