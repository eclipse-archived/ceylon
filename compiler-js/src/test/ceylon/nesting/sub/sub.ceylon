import nesting { test489 }

//Metamodel tests (model2)
shared test489 object subSharedObject {
  string="shared object in subpackage";
}
shared Integer subSharedValue=3;

test489 object subUnsharedObject {
  string="unshared object in subpackage";
}
test489 Integer subUnsharedValue=4;

shared void sharedSubFun() {}
test489 void unsharedSubFun() {}
