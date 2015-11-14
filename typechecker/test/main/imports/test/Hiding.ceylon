import ceylon.language { @error Set, Map, String }

class Set() {}
class List() {}

void useThem() {
   Set();
   @error Map();
   List();
   String s = "hello";
   void nested() {
       class Map() {}
       Map();
       interface String {}
       @error String s = "hello";
   }
}