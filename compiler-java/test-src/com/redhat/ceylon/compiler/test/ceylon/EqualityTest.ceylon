shared class EqualityTest() extends Test() {

	class Entity (Integer id) satisfies Equality {	
	
		shared Integer id = id;
			
	    shared actual Boolean equals(Equality that) {
	    	if (is Entity that) {
	    		return id == that.id;
	    	} 
	    	return false;	    		    	
	    }
	    
    	shared actual Integer hash {
    		return id;
    	}    	
	
	}

	@test
	shared void testEquals() {
		Equality entity1 = Entity(+1);
		Equality entity2 = Entity(+2);
		assertFalse(entity1 == entity2);
	}
		
	
	@test
	shared void testHash() {
		Equality entity = Entity(+1);
		Integer entityHash = entity.hash;
		assertEquals(entityHash, entity.hash);		
	}	
	
}	  