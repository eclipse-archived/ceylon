T infer<T>(Co<T> co) { return bottom; }

interface G {} interface H {}

interface Co<out T> {
    formal shared T get();
}

class SuperCo() satisfies Co<Object> {
    default shared actual Object get() { return 1; }
}
class SubCo() extends SuperCo() satisfies Co<String> {
    default shared actual String get() { return ""; }
    void m() {
        @type:"Object" super.get();
    }
} 
@error class SubCoBroken() extends SuperCo() satisfies Co<String> {}
void testSubCo() {
    value inst = SubCo();
    @type:"String" inst.get();
    Co<String> cog = inst;
    Co<Object> coo = inst;
    @type:"String" infer(inst);
}

class SuperCoOk() satisfies Co<String> {
    default shared actual String get() { return ""; }
}
class SubCoOk() extends SuperCoOk() satisfies Co<Object> {
} 
void testSubCoOk() {
    value inst = SubCoOk();
    @type:"String" inst.get();
    Co<String> cog = inst;
    Co<Object> coo = inst;
    @type:"String" infer(inst);
}


class SuperCoGood() satisfies Co<G> {
    default shared actual G get() { return bottom; }
}
class SubCoGood() extends SuperCoGood() satisfies Co<H> {
    default shared actual H&G get() { return bottom; }
    void m() {
        @type:"G" super.get();
    }
}
void testSubCoGood() {
    value inst = SubCoGood();
    @type:"H&G" inst.get();
    Co<G> cog = inst;
    Co<H> coh = inst;
    Co<Object> coo = inst;
    Co<G&H> cogh = inst;
    @type:"G&H" infer(inst);
}

interface InterCoG satisfies Co<G> {
    default shared actual G get() { return bottom; }
}
interface InterCoH satisfies Co<H> {}
interface InterCo satisfies Co<Object> {}

@error class SatCoBroken() satisfies InterCoG&InterCoH {}

class SatCoOK() satisfies InterCo&InterCoG&InterCoH {
    default shared actual G&H get() { return bottom; }
}
void testSatCoOK() {
    value inst = SatCoOK();
    @type:"G&H" inst.get();
    Co<G> cog = inst;
    Co<H> coh = inst;
    Co<Object> coo = inst;
    Co<G&H> cogh = inst;
    @type:"G&H" infer(inst);
}

class SatCoGood() satisfies InterCo&InterCoG {}
void testSatCoGood() {
    value inst = SatCoGood();
    @type:"G" inst.get();
    Co<G> cog = inst;
    Co<Object> coo = inst;
    @type:"G" infer(inst);
}
class SatCoFine() satisfies InterCo&InterCoH {
    default shared actual H get() { return bottom; }
}
void testSatCoFine() {
    value inst = SatCoFine();
    @type:"H" inst.get();
    Co<H> cog = inst;
    Co<Object> coo = inst;
    @type:"H" infer(inst);
}

interface Contra<in T> {
    formal shared void accept(T t);
}

class SuperContra() satisfies Contra<String> {
    shared actual void accept(String t) {}
}

@error class SubContra() extends SuperContra() satisfies Contra<Object> {} 


interface I1 { shared String name { return "Gavin"; } } 
interface I2 { shared String name { return "Tom"; } }

@error class Broken() satisfies I1&I2 {}

class MethodIfNonEmptySequence() {
    shared void m1(String s) {
        if (nonempty s) {
            Character c = s.first;
            Character[] chars = s.rest;
        }
    }
    shared void m2(String[] s) {
        if (nonempty s) {
            String string = s.first;
            String[] strings = s.rest; 
        }
    }
    shared void m3(Empty|Range<Integer> s) {
        if (nonempty s) {
            Integer i = s.first;
            Integer[] ints = s.rest;
        }
    }
}
