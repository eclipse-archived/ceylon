doc "A type capable of representing the values true and
     false of Boolean logic."
by "Gavin"
shared abstract class Boolean(String name) 
        of true | false 
        extends Case(name) {}