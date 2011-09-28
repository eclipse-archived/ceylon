@nomodel
class ThrowMethodResult() {
    Exception e() {
        return Exception(null, "Bang!");
    }
	void t() {
		throw e();
	}
}
