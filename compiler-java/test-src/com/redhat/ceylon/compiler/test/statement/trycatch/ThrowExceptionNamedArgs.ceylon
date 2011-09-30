@nomodel
class ThrowExceptionNamedArgs() {
	void t() {
		throw Exception{
		    description="Bang!"; 
		    cause=null;
		};
	}
}
