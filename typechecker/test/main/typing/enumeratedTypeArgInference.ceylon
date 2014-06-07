void enumeratedTypeArgInference() {

class Cat() {}

abstract class Dog() of SmallDog | BigDog {}    
class BigDog() extends Dog() {}    
class SmallDog() extends Dog() {}    

Boolean smallIsBetter = false;    
Dog dog() => smallIsBetter then SmallDog() else BigDog();
SmallDog|BigDog someDog() => smallIsBetter then SmallDog() else BigDog();

Boolean dogIsBetter = false;
Dog|Cat? pet() => dogIsBetter then dog() else Cat();
SmallDog|BigDog|Cat? somePet() => dogIsBetter then someDog() else Cat();


class SequenceBuilder<Value>() {
    shared void append(Value val) {}
    shared Value[] sequence() => [];
}

{Value*} collectTillBreak<Value>(Value?() get) {
    value builder = SequenceBuilder<Value>();
    while (true) {
        value val = get();
        if (exists val) {
            builder.append(val);        
        } else {
            break;
        }            
    }
    return builder.sequence();
}

{<SmallDog|BigDog|Cat?>*} seq3 = collectTillBreak(somePet);    
{<Dog|Cat>*} seq4 = collectTillBreak(pet); 

}
