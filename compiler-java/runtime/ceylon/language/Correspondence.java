package ceylon.language;

public interface Correspondence<Key extends Equality,Value> {
    public Value value(Key key);

    public Boolean defines(Key key);
//   return value(key) exists;

    public Category getKeys();
    /*
shared default object keys satisfies Category {
   shared actual Boolean contains(Object value) {
       if (is Key value) {
           return defines(value);
       }
       else {
           return false;
       }
   }
}*/

    public Boolean definesEvery(Key... keys);
    /*
   for (Key key in keys) {
       if (!defines(key)) {
           return false;
       }
   }
   fail {
       return true;
   }
}*/

    public Boolean definesAny(Key... keys);/* {
   for (Key key in keys) {
       if (defines(key)) {
           return true;
       }
   }
   fail {
       return false;
   }
}*/

    public Value[] values(Key... keys);/* {
   if (nonempty keys) {
       return Values(keys.clone);
   }
   else {
       return {};
   }
}*/
   /*
class Values(Sequence<Key> keys)
       extends Object()
       satisfies Sequence<Value?> {
   shared actual Natural lastIndex {
       return keys.lastIndex;
   }
   shared actual Value? first {
       return outer.value(keys.first);
   }
   shared actual Value?[] rest {
       return outer.values(keys.rest);
   }
   shared actual Value? value(Natural index) {
       if (exists Key key = keys.value(index)) {
           return outer.value(key);
       }
       else {
           return null;
       }
   }
   shared actual Sequence<Value?> clone {
       return this;
   }
}
*/
}
