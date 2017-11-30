void test3() {
    $type:"I&J" value ij1 = ABottom().thing;
    $type:"I&J" value ij2 = ABottom().thingy("");
}

class ABottom() satisfies AMid1&AMid2<J> {
    shared actual value thing = object satisfies I&J{};
    shared actual function thingy(String s) => object satisfies I&J{};
}

//interface I {} interface J {}

interface ATop {
    shared formal Object thing;
    shared formal Object thingy(String s);
}

interface AMid1 satisfies ATop {
    shared actual formal I thing;
    shared actual formal I thingy(String s);
}

interface AMid2<T> 
        satisfies ATop 
        given T satisfies Object {
    shared actual formal T thing;
    shared actual formal T thingy(String s);
}

void test4() {
    $type:"I&J" value ij1 = ABottom().thing;
    $type:"I&J" value ij2 = ABottom().thingy("");
}


class Cc() {
    shared default Integer|String? i = null;
}

class Dd() extends Cc() {
    shared actual default value i => 0;
    $error string => (i + 1).string;
}

shared void rund() {
    print(Dd());
    $error print(Dd().i + 1);
    $type:"Integer|Null|String" value i = Dd().i; 
}

class Ee() extends Dd() {
    shared actual default value i => "";
}

class WithoutError1() {
    $type:"Boolean" 
    shared actual function equals(Object x) => false;
    $type:"String" 
    shared actual value string => "";
}

class WithoutError2() {
    $type:"Boolean"
    shared actual function equals(Object x) {
        return true;
    }
    $type:"String"
    shared actual value string {
        return "";
    }
}

class WithoutError3() {
    $type:"Boolean"
    shared actual function equals(Object x) {
        return nothing;
    }
    $type:"String"
    shared actual value string {
        return nothing;
    }
}

class WithoutError4() {
    $type:"Boolean" 
    shared actual function equals(Object x) => nothing;
    $type:"String" 
    shared actual value string => nothing;
}


class WithInferenceAndFatArrow() {
    $type:"Boolean" 
    $error shared actual function equals(Object x) => 1;
    $type:"String" 
    $error shared actual value string => 1;
}

class WithShortcutRefinement() {
    $error equals(Object x) => 1;
    $error string => 1;
}

class WithInferenceAndReturn() {
    $type:"Boolean" 
    shared actual function equals(Object x) {
        $error return 1;
    }
    $type:"String" 
    shared actual value string {
        $error return 1;
    }
}
