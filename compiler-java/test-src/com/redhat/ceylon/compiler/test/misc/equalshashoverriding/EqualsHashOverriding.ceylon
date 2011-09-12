class EqualsHashOverriding(String s) satisfies Equality {
    shared void print() {}
    shared actual Boolean equals(Equality that) {
    	if (is EqualsHashOverriding that) {
    		return s.equals(that.s);
    	}
    	else {
    		return false;
    	}
    }
    shared actual Integer hash {
    	return s.hash;
    }
}