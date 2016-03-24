"Thrown when you try to read attributes that were not shared nor captured and had no
 physical storage allocated, so do not exist at runtime.
 
 For example if you try to read the attribute from this class:
 
     shared class Foo(){
         Integer x = 2;
     }
 
 This will not work because `x` is neither shared nor captured and so it is just not
 retained in the runtime instances of `Foo`.
 "
shared class StorageException(String message) extends Exception(message){}
