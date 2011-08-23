@nomodel
class TryCatch() {
	void t() {
		
	}
	
	void c(Exception e) {
	
	}

	void m() {
		try {
			t();
		} catch (Exception e) {
			c(e);
		}
	}
}
