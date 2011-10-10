shared class CaseTest() extends Test() {

	class Color (String name) extends Case(name) {}

	@test
	shared void testString() {
		Color red = Color("red");	
		assertEquals("red", red.string);		
	}
		
	@test
	shared void testEquals() {
		Color red = Color("red");
		Color anotherRed = Color("red");
		Color yellow =  Color("yellow");		
		assertFalse(yellow == red);
		assertTrue(yellow == yellow);		
		assertFalse(red == anotherRed);		
	}
	
	@test
	shared void testHash() {
		Color red = Color("red");
		Color green = Color("green");	
		Integer redHash = red.hash;	
		Integer greenHash = green.hash;
		assertTrue(redHash != greenHash);	
	}	
	
}	  