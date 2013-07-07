class Time(shared Integer hour, 
           shared Integer minute) {
    
    assert(hour>=0, minute>=0);
    
    shared default Integer secondsSinceMidnight =>
            minute%60*60 + hour%24*60*60;
    
    shared actual Integer hash => 
            secondsSinceMidnight;
    
    shared actual Boolean equals(Object that) {
        if (is Time that) {
            return secondsSinceMidnight == 
                   that.secondsSinceMidnight;
        }
        else {
            return false;
        }
    }
    
    string => "``this.hour``:``this.minute``";
    
    shared class X() { string=>outer.string + ".X"; }
    
}

class SecondTime(Integer hour, 
                 Integer minute, 
                 second) 
        extends Time(hour, minute) {
    
    assert (second>0);
    
    shared Integer second;
    
    secondsSinceMidnight => 
            super.secondsSinceMidnight+second%60;
    
}

void tryOutTime() {
    Time time1 = Time(13,30);
    print(time1);
    Time time2 = Time(37,30);
    print(time2);
    Time time3 = Time(13,29);
    Integer i1 = time1.secondsSinceMidnight;
    Integer i2 = time1.hour;
    Integer i3 = time1.minute;
    Time.X x = time1.X();
}
