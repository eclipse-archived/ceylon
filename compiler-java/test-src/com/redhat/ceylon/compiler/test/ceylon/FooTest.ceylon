shared class FooTest () extends Test() {
  @test
  shared void testFail(){
    fail();
  }
  @test
  shared void testSucceed(){
  }
}