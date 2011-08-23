@nomodel
class TryCatchFinally() {
	void t() {
	}

	void c(Exception e) {
	}
	
	void f() {
	
	}

	void m() {
		try {
			t();
		} catch(Exception e) {
		    c(e);
		}finally {
			f();
		}
	}
}
