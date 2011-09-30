@nomodel
class E(String? description, Exception? cause) extends Exception(description, cause) {
	
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
