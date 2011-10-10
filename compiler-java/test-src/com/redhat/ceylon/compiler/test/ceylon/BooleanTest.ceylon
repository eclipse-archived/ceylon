shared class BooleanTest() extends Test() {

  @test
  shared void testIsTrue() {
  	Boolean isTrue = true;
    assertTrue(isTrue);
  }
  
  @test
  shared void testIsFalse() {
  	Boolean isFalse = false;
    assertFalse(isFalse);
  }
  
  @test
  shared void testEqual() {
  	Boolean equal = 1==1;
     assertTrue(equal);
  }

  @test
  shared void testNotEqual() {
  	Boolean notEqual = 1!=2;
    assertTrue(notEqual);
  }      
  
  @test
  shared void testSmaller() {
  	Boolean smaller = 1<2;
    assertTrue(smaller);
  }
  
  @test
  shared void testLarger() {
  	Boolean larger = 2>1;
    assertTrue(larger);
  }         
  
  @test
  shared void testSmallAs() {
  	Boolean smallAs = 1<=2;
     assertTrue(smallAs);
  }
  
  @test
  shared void testLargeAs() {
  	Boolean largeAs = 2>=1;
    assertTrue(largeAs);
  }

}