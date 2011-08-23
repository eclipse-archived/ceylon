@nomodel
class E1(String? message, Exception? cause) extends Exception(message, cause) {
	
}

@nomodel
class E2(String? message, Exception? cause) extends Exception(message, cause) {
	
}

@nomodel
class TryCatchIntersection() {
	void t() {
	}
	
	void c(Exception e) {
	
	}

	void m() {
		try {
			t();
		} catch (E1&E2 e) {
			c(e);
		}
	}
}
