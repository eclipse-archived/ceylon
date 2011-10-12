shared class CharacterTest() extends Test() {

	@test
	shared void testLowercased() {
		Character testChar = `A`;
		assertEquals(`a`,testChar.lowercased);
	}
	
	@test
	shared void testUppercased() {
		Character testChar = `a`;
		assertEquals(`A`,testChar.uppercased);
	}
	
	@test
	shared void testUppercase() {
		Character testCharLower = `a`;
		Character testCharUpper = `A`;
		Character testCharDigit = `1`;
		assertFalse(testCharLower.uppercase);
		assertTrue(testCharUpper.uppercase);
		assertFalse(testCharDigit.uppercase);
	}
	
	@test
	shared void testLowercase() {
		Character testCharLower = `a`;
		Character testCharUpper = `A`;
		Character testCharDigit = `1`;
		assertTrue(testCharLower.lowercase);
		assertFalse(testCharUpper.lowercase);
		assertFalse(testCharDigit.lowercase);
	}
	
	@test
	shared void testDigit() {
		Character testCharLower = `a`;
		Character testCharUpper = `A`;
		Character testCharDigit = `1`;
		assertFalse(testCharLower.digit);
		assertFalse(testCharUpper.digit);
		assertTrue(testCharDigit.digit);
	}
	
	@test
	shared void testLetter() {
		Character testChar = `a`;	
		Character testDigit = `1`;
		Character testWhite = ` `;
		assertTrue(testChar.letter);
		assertFalse(testDigit.letter);
		assertFalse(testWhite.letter);
	}
	
	@test
	shared void testWhite() {
		Character testChar = `a`;	
		Character testDigit = `1`;
		Character testWhite = ` `;
		assertFalse(testChar.whitespace);
		assertFalse(testDigit.whitespace);
		assertTrue(testWhite.whitespace);
	}	
	
}	  