abstract class Animal<ActualFood>() 
        given ActualFood satisfies Food {
    shared void eat(ActualFood food) {}
    shared void eatALot(ActualFood food) {
        eat(food);
        eat(food);
    }
    shared formal ActualFood diet();
}

abstract class Food() {}
class Grass() extends Food() {} 
class Meat() extends Food() {}

class Cow() extends Animal<Grass>() {
    shared actual Grass diet() { 
        return Grass(); 
    }
}

class Wolf() extends Animal<Meat>() {
    shared actual Meat diet() { 
        return Meat(); 
    }
}

class Devil() extends Animal<Food>() {
    shared actual Meat diet() { 
        return Meat(); 
    }
}

void test() {
    Cow().eat(Grass());
    @error Cow().eat(Meat());
    Devil().eat(Grass());
    void feed<ActualFood>(Animal<ActualFood> a, ActualFood f) 
            given ActualFood satisfies Food {
        a.eat(f);
    }
    feed(Wolf(), Meat());
    feed<Food>(Devil(), Meat());//TODO:remove explicit type arg once we fix the type inference algo!
    @error feed(Cow(), Meat());
    void feedAutomatically<ActualFood>(Animal<ActualFood> a) 
            given ActualFood satisfies Food {
        a.eat(a.diet());
    }
    //TODO:remove explicit type arg once we fix the type inference algo!
    feedAutomatically<Meat>(Wolf());
    feedAutomatically<Grass>(Cow());
    feedAutomatically<Food>(Devil());
}