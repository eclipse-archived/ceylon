@nomodel
class E(Exception? cause, String? message) extends Exception(cause, message) {
	
}

@nomodel
class ThrowExceptionSubclass() {
	void t() {
		throw E(null, "Bang!");
	}
}
