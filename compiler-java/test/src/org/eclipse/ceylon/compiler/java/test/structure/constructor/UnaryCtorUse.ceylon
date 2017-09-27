void unaryCtorUse() {
    value o1 = UnaryCtor{s1="";};
    value o3 = UnaryCtor.sharedFromInteger{i=0;};
    value o4 = UnaryCtor.sharedFromInteger{i=0;};
    
    value o11 = UnaryCtor("");
    value o13 = UnaryCtor.sharedFromInteger(0);
    value o14 = UnaryCtor.sharedFromInteger(0);
}