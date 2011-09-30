@nomodel
class E1(String? description, Exception? cause) extends Exception(description, cause) {
	
}

@nomodel
class E2(String? description, Exception? cause) extends Exception(description, cause) {
	
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
