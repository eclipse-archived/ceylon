@nomodel
class ThrowExceptionNamedArgs() {
	void t() {
		throw Exception{
		    message="Bang!"; 
		    cause=null;
		};
	}
}
