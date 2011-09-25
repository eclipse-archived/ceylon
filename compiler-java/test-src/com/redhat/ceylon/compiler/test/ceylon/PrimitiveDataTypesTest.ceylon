shared class PrimitiveDataTypesTest () extends Test() {
  @test
  shared void testFail(){
    fail();
  }
  @test
  shared void testSucceed(){
  }
  
  @test
  shared void testNumbers(){
    variable Integer i := +2;
    assertEquals(+2, i);
    assertTrue(+2 == i);
    assertTrue(i == +2);
    assertFalse(i == +3);
    assertTrue(i != +3);

    i := +3;
    assertEquals(+3, i);
    assertTrue(+3 == i);
    assertTrue(i == +3);
    assertFalse(i == +2);
    assertTrue(i != +2);
    
    assertTrue(i == i);
    assertTrue(+3 == +3);
    
    assertFalse(+3 == +2);
    assertTrue(+3 != +2);
    
    variable Integer j := i + i;
    assertTrue(j == +6);

    j := i * i;
    assertTrue(j == +9);

    j := i - i;
    assertTrue(j == +0);

    j := i / i;
    assertTrue(j == +1);

    assertTrue(+2 < +3);
    assertFalse(+2 > +3);

    assertTrue(+3 >= +3);
    assertTrue(+3 <= +3);
    assertFalse(+3 > +3);
    assertFalse(+3 < +3);
  }
  
  @test
  shared void testBooleans(){
    assertTrue(true);
    assertTrue(!false);
    assertFalse(false);
    assertFalse(!true);

    variable Boolean t := true;
    assertTrue(t);
    assertFalse(!t);

    variable Boolean f := false;
    assertFalse(f);
    assertTrue(!f);
    
    assertTrue(t && !f);
    assertTrue(!f && t);
    assertFalse(t && f);
    assertFalse(f && t);
    
    assertTrue(t || f);
    assertTrue(f || t);
    assertTrue(t || t);
    assertFalse(f || f);
    
    t ||= true;
    assertTrue(t);
    t ||= false;
    assertTrue(t);

    f ||= false;
    assertFalse(f);
    f ||= true;
    assertTrue(f);
    // reset
    f := false;
    
    t &&= true;
    assertTrue(t);
    t &&= false;
    assertFalse(t);

    f &&= true;
    assertFalse(f);
    f &&= false;
    assertFalse(f);
  }
}