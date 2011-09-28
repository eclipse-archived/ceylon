@nomodel
class E1(Exception? cause, String? message) extends Exception(cause, message) {
	
}

@nomodel
class E2(Exception? cause, String? message) extends Exception(cause, message) {
	
}

@nomodel
class TryCatchUnion() {
	void t() {
	}
	
	void c(Exception e) {
	
	}

	void m() {
		try {
			t();
		} catch (E1|E2 e) {
			c(e);
		}
	}
}
