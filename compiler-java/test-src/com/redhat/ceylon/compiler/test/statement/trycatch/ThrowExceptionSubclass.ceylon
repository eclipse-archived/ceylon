@nomodel
class E(String? description, Exception? cause) extends Exception(description, cause) {
	
}

@nomodel
class ThrowExceptionSubclass() {
	void t() {
		throw E("Bang!", null);
	}
}
