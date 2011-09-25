shared class StatementTest() extends Test() {
  @test
  shared void testIf() {
    if(true){
    }else{
      fail();
    }
    if(false){
      fail();
    }else{
    }
  }

  @test
  shared void testFor() {
    variable Integer i := +0;
    for(Integer j in {+1,+2,+3}){
      i += j;
    }
    assertTrue(+6 == i);
  }
}