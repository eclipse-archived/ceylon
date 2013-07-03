T infer<T>(Co<T> co) { return nothing; }

interface G {} interface H {}

interface Co<out T> {
    formal shared T get();
    shared class C<out S>(S* s) {
        shared T get() => nothing;
    }
}

class SuperCo() satisfies Co<Object> {
    default shared actual Object get() { return 1; }
}
class SubCo() extends SuperCo() satisfies Co<String> {
    default shared actual String get() { return ""; }
    void m() {
        @type:"Object" (super of SuperCo).get();
        @error @type:"String" super.get();
        @error @type:"String" (super of Co<String>).get();
        @error (super of Object).get();
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
    default shared actual G get() { return nothing; }
}
class SubCoGood() extends SuperCoGood() satisfies Co<H> {
    default shared actual H&G get() { return nothing; }
    void m() {
        @type:"G&H" @error super.get();
        @type:"G" (super of SuperCoGood).get();
        @type:"H" @error (super of Co<H>).get();
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
    
    @type:"G&H" inst.C("").get();
    Co<G>.C<String|Float> cogc = inst.C("", 0.0);
    Co<H>.C<String|Float> cohc = inst.C("", 0.0);
    Co<Object>.C<String|Float> cooc = inst.C("", 0.0);
    Co<G&H>.C<String|Float> coghc = inst.C("", 0.0);
}

interface InterCoG satisfies Co<G> {
    default shared actual G get() { return nothing; }
}
interface InterCoH satisfies Co<H> {}
interface InterCo satisfies Co<Object> {}

@error class SatCoBroken() satisfies InterCoG&InterCoH {}

class SatCoOK() satisfies InterCo&InterCoG&InterCoH {
    default shared actual G&H get() { return nothing; }
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
    default shared actual H get() { return nothing; }
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
        if (nonempty chars = s.characters) {
            Character c = chars.first;
            Character[] chars = chars.rest;
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
