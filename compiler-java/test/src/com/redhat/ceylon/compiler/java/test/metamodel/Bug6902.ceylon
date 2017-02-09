import ceylon.language.meta.model{...}

class Bug6902() {
    shared class MemberWithCtors {
        shared new () {}
        shared new create() {}
        shared new instance {}
    }
}
shared void bug6902() {
    assert (is MemberClassValueConstructor<> mcvc = `Bug6902.MemberWithCtors`.getConstructor<[]>("instance"));
    ValueConstructor<Anything> vc = mcvc(Bug6902());
    assert (vc.get() is Bug6902.MemberWithCtors);
}