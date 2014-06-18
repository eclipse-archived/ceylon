shared abstract class BaseIterable<Element,Absent>()
        extends Object()
        satisfies Iterable<Element,Absent> 
        given Absent satisfies Null {}

shared abstract class BaseIterator<Element>()
        extends Object()
        satisfies Iterator<Element> {}

shared abstract class BaseMap<Key,Item>()
        extends Object()
        satisfies Map<Key,Item> 
        given Key satisfies Object
        given Item satisfies Object {}

shared abstract class BaseList<Element>()
        extends Object()
        satisfies List<Element> {}