shared interface Top1{}
shared interface Middle1 satisfies Top1{}
shared interface Bottom1 satisfies Middle1{}

shared class Invariant<Element>(){}
shared class Covariant<out Element>(){}
shared class Contravariant<in Element>(){}
shared class Bivariant<in In, out Out>(){}

void runtime(){
    Object invTop1 = Invariant<Top1>();
    assert(invTop1 is Invariant<Top1>);
    assert(! invTop1 is Invariant<Middle1>);
    assert(! invTop1 is Invariant<Bottom1>);

    Object invMiddle1 = Invariant<Middle1>();
    assert(! invMiddle1 is Invariant<Top1>);
    assert(invMiddle1 is Invariant<Middle1>);
    assert(! invMiddle1 is Invariant<Bottom1>);

    Object covMiddle1 = Covariant<Middle1>();
    assert(covMiddle1 is Covariant<Top1>);
    assert(covMiddle1 is Covariant<Middle1>);
    assert(! covMiddle1 is Covariant<Bottom1>);

    Object contravMiddle1 = Contravariant<Middle1>();
    assert(! contravMiddle1 is Contravariant<Top1>);
    assert(contravMiddle1 is Contravariant<Middle1>);
    assert(contravMiddle1 is Contravariant<Bottom1>);

    Object bivMiddle1 = Bivariant<Middle1,Middle1>();
    assert(! bivMiddle1 is Bivariant<Top1,Top1>);
    assert(! bivMiddle1 is Bivariant<Top1,Middle1>);
    assert(! bivMiddle1 is Bivariant<Top1,Bottom1>);
    assert(bivMiddle1 is Bivariant<Middle1,Top1>);
    assert(bivMiddle1 is Bivariant<Middle1,Middle1>);
    assert(! bivMiddle1 is Bivariant<Middle1,Bottom1>);
    assert(bivMiddle1 is Bivariant<Bottom1,Top1>);
    assert(bivMiddle1 is Bivariant<Bottom1,Middle1>);
    assert(! bivMiddle1 is Bivariant<Bottom1,Bottom1>);
}
