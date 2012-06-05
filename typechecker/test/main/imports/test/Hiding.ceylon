import ceylon.language { Set, String }

class Set() {}
class List() {}

void useThem() {
   @error Set();
   List();
   interface String {}
   @error String s = "hello";
}