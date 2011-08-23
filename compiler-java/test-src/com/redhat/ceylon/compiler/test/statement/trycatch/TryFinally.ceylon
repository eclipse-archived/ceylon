@nomodel
class TryFinally() {
	void t() {
	}
	
	void f() {
	}

	void m() {
		try {
			t();
		} finally {
			f();
		}
	}
}
