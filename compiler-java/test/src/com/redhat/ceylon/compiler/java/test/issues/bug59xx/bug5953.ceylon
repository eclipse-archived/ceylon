shared abstract class Thingee5953() of one5953 | two5953 | three5953 {}

shared object one5953 extends Thingee5953() {}
shared object two5953 extends Thingee5953() {}
shared object three5953 extends Thingee5953() {}

Thingee5953? parseThingee5953(String str) 
        => if ("Jan" == str) then one5953
else if ("Feb" == str) then two5953
else if ("Mar" == str) then three5953 
else null;