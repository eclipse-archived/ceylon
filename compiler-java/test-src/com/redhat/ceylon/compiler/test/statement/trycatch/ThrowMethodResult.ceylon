@nomodel
class ThrowMethodResult() {
    Exception e() {
        return Exception("Bang!", null);
    }
	void t() {
		throw e();
	}
}
