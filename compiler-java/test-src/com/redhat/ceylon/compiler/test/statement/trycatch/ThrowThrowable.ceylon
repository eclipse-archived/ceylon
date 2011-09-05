import java.lang {Throwable}

@nomodel
class ThrowThrowable() {
	void m() {
		throw Throwable("Bang!", null);
	}
}
