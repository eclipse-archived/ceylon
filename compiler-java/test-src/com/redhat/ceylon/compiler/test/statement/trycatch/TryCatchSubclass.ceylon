@nomodel
class E(Exception? cause, String? message) extends Exception(cause, message) {
	
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
