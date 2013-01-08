class DefaultSetter() {
    variable Integer a = 0;
    interface I {
        shared formal variable Integer aFormal;
        shared default Integer aDefault {
            return outer.a;
        }
        assign aDefault {
            outer.a = aDefault;
        }
    }
    abstract class Abstract() satisfies I {
        shared actual default Integer aFormal {
            return aDefault;
        }
        assign aFormal {
            aDefault = aFormal;
        }
    }
}

void variation() {
    @error i++;
    @error i=10;
    @error j++;
    @error j=10;
    Integer k { @error return i; }
    assign k { @error i=k; }
    variable Integer i;
    Integer j { return i; }
    assign j { i=j; }
}

shared abstract class LogicalOperatorsParent<B>(){
    shared formal variable B boxedB1;
    shared formal variable B boxedB2;
}

shared class LogicalOperators() extends LogicalOperatorsParent<Boolean>(){
    shared variable Boolean b1 = false;
    shared variable Boolean b2 = false;
    shared actual variable Boolean boxedB1 = b1;
    shared actual variable Boolean boxedB2 = b2;

    shared default void logical() {
        b1 = !b2;
        b1 = true || b2;
        b1 = false && b2;
        b1 = b1 ||= b2;
        b1 = b1 &&= b2;
        b1 = this.b1 ||= this.b2;
        b1 = this.b1 &&= this.b2;
    }

    shared default void logicalBoxed() {
        boxedB1 = !boxedB2;
        boxedB1 = true || boxedB2;
        boxedB1 = false && boxedB2;
        boxedB1 = boxedB1 ||= boxedB2;
        boxedB1 = boxedB1 &&= boxedB2;
        boxedB1 = this.boxedB1 ||= this.boxedB2;
        boxedB1 = this.boxedB1 &&= this.boxedB2;
    }
}

shared class LogicalOperatorsSub() extends LogicalOperators(){

    shared actual void logical() {
        b1 = super.b1 ||= super.b2;
        b1 = super.b1 &&= super.b2;
    }

    shared actual void logicalBoxed() {
        boxedB1 = super.boxedB1 ||= super.boxedB2;
        boxedB1 = super.boxedB1 &&= super.boxedB2;
    }
}