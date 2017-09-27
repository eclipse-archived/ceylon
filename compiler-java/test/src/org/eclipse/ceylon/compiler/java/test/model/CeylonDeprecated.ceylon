deprecated class CeylonDeprecated(){}
deprecated class CeylonDeprecated2{
    shared new(){}
}

class CeylonDeprecated3{
    shared deprecated new() {}
    shared deprecated new other () {}
    shared deprecated new val {}
}

class CeylonDeprecated4() {
    shared deprecated String a => "";
    shared deprecated void m() {}
}

class CeylonDeprecated5(shared deprecated String a, shared deprecated void m()) {
    
}

deprecated interface CeylonDeprecated6 {
}

deprecated void ceylonDeprecated7() {
}
deprecated Anything ceylonDeprecated8() => null;
deprecated String ceylonDeprecated9 { return ""; }
deprecated String ceylonDeprecated10 => "";
deprecated object ceylonDeprecated11 {}