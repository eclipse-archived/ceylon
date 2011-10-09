shared class EqualityTest() extends Test() {

	class Entity (Integer initId) satisfies Equality {	
	
		shared Integer id {
			return initId;		
		}
	
	    shared actual Boolean equals(Equality that) {
	    	if (this == that) { 
	    		return true;
	    	}
	    		
	    	if (is Entity that) {
	    		return id.equals(that.id);
	    	} else {
	    		return false;
	    	}	    	
	    }
	    
    	shared actual Integer hash {
    		return id.hash;
    	}    	
	
	}


	@test
	shared void testEquals() {
		Equality entity1 = Entity(+1);
		Equality entity2 = Entity(+2);
		assertFalse(entity1.equals(entity2));
	}
		
	
	@test
	shared void testHash() {
		Equality entity = Entity(+1);
		Integer entityHash = entity.hash;
		assertEquals(entityHash, entity.hash);		
	}	
	
}	  