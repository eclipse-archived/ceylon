void unaryCtorUse() {
    value o1 = UnaryCtor{s1="";};
    value o3 = UnaryCtor.SharedFromInteger{i=0;};
    value o4 = UnaryCtor.SharedFromInteger{i=0;};
    
    value o11 = UnaryCtor("");
    value o13 = UnaryCtor.SharedFromInteger(0);
    value o14 = UnaryCtor.SharedFromInteger(0);
}