@nomodel
class E(String? message, Exception? cause) extends Exception(message, cause) {
	
}

@nomodel
class ThrowExceptionSubclass() {
	void t() {
		throw E("Bang!", null);
	}
}
