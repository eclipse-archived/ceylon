shared class MyClass(Integer i) {
	Integer j = i;
	
	shared Integer method(){
		return j;
	}
}