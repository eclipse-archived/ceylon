suppressWarnings("unusedDeclaration", "expressionTypeNothing")
void ceylondeprecatedtest() {
    CeylonDeprecated x1 = nothing;
    value x2 = CeylonDeprecated();
    
    CeylonDeprecated2 x3 = nothing;
    value x4 = CeylonDeprecated2();
    
    CeylonDeprecated3 x5 = nothing;// no warning here
    value x6 = CeylonDeprecated3();
    value x7 = CeylonDeprecated3.other();
    value x8 = CeylonDeprecated3.val;
    
    value x9 = (nothing of CeylonDeprecated4).a;
    value x10 = (nothing of CeylonDeprecated4).m();
    
    value x11 = (nothing of CeylonDeprecated5).a;
    (nothing of CeylonDeprecated5).m();
    
    CeylonDeprecated6 x12 = nothing;
    
    ceylonDeprecated7();
    ceylonDeprecated8();
    
    value x13 = ceylonDeprecated9;
    value x14 = ceylonDeprecated10;
    
    value x15 = ceylonDeprecated11;
}