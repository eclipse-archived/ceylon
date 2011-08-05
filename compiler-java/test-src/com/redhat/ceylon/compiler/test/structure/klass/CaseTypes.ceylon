shared object foo extends CaseTypes("foo") {}
shared object bar extends CaseTypes("bar") {}

shared abstract class CaseTypes(String name) 
        of foo | bar 
        extends Case(name) {}
