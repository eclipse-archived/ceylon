doc "A type capable of representing the values true and
     false of boolean logic."
by "Gavin"
shared abstract class Boolean(String name) 
        of true | false 
        extends Case(name) {}