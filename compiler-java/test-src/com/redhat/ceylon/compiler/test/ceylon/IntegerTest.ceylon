shared class IntegerTest() extends Test() {

	@test
	shared void testEqual() {
		Integer one = +1;
		Integer anotherOne = +1;
		Integer two = +2;
		assertTrue(one == anotherOne);
		assertFalse(one == two);
	}

	
}	  