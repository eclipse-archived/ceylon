@nomodel
class E(String? message, Exception? cause) extends Exception(message, cause) {
	
}

@nomodel
class TryCatchSubclass() {
	void t() {
	}
	
	void ce(E  e) {
	
	}
	
	void cexception(Exception e) {
	
	}

	void m() {
		try {
			t();
		} catch (E e) {
			ce(e);
		} catch (Exception e) {
			cexception(e);
		}
	}
}
