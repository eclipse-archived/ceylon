class Class714<T>(){
  shared class MC<U>(){}
  shared interface MI<U>{}
}
interface Iface714<T>{
  shared class MC<U>(){}
  shared interface MI<U>{}
}

@test
shared void bug714() {
  assert(`Class714<String>`     == `Class714<String>`     &&
         `Class714<out String>` == `Class714<out String>` &&
         `Class714<in String>`  == `Class714<in String>`);
  assert(`Class714<String>`     != `Class714<out String>` &&
         `Class714<String>`     != `Class714<in String>`  &&
         `Class714<in String>`  != `Class714<out String>`);

  assert(`Class714<Object>.MC<String>`     == `Class714<Object>.MC<String>`     &&
         `Class714<Object>.MC<out String>` == `Class714<Object>.MC<out String>` &&
         `Class714<Object>.MC<in String>`  == `Class714<Object>.MC<in String>`);
  assert(`Class714<Object>.MC<String>`     != `Class714<Object>.MC<out String>` &&
         `Class714<Object>.MC<String>`     != `Class714<Object>.MC<in String>`  &&
         `Class714<Object>.MC<in String>`  != `Class714<Object>.MC<out String>`);
  assert(`Class714<Object>.MC<String>`     != `Class714<out Object>.MC<String>` &&
         `Class714<Object>.MC<String>`     != `Class714<in Object>.MC<String>`  &&
         `Class714<in Object>.MC<String>`  != `Class714<out Object>.MC<String>`);

  assert(`Iface714<String>`     == `Iface714<String>` &&
         `Iface714<out String>` == `Iface714<out String>` &&
         `Iface714<in String>`  == `Iface714<in String>`);
  assert(`Iface714<String>`     != `Iface714<out String>` &&
         `Iface714<String>`     != `Iface714<in String>`  &&
         `Iface714<in String>`  != `Iface714<out String>`);

  assert(`Iface714<Object>.MC<String>`     == `Iface714<Object>.MC<String>`     &&
         `Iface714<Object>.MC<out String>` == `Iface714<Object>.MC<out String>` &&
         `Iface714<Object>.MC<in String>`  == `Iface714<Object>.MC<in String>`);
  assert(`Iface714<Object>.MC<String>`     != `Iface714<Object>.MC<out String>` &&
         `Iface714<Object>.MC<String>`     != `Iface714<Object>.MC<in String>`  &&
         `Iface714<Object>.MC<in String>`  != `Iface714<Object>.MC<out String>`);
  assert(`Iface714<Object>.MC<String>`     != `Iface714<out Object>.MC<String>` &&
         `Iface714<Object>.MC<String>`     != `Iface714<in Object>.MC<String>`  &&
         `Iface714<in Object>.MC<String>`  != `Iface714<out Object>.MC<String>`);

}
