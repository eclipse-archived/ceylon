import ceylon.language.meta.model { Method }

@noanno
interface Out1927<out Element> 
        given Element satisfies Object {
}

@noanno
interface In1927<in Element>
        satisfies Out1927<Object>
        given Element satisfies Sequence<Anything> {
    
    shared formal Boolean add(Element element);
    
}
@noanno
class Test1927() {
    shared void construct() {
        class State<CompilerBugWorkaround=Nothing>(positions) {
            shared In1927<[Method<Nothing,Anything,Nothing>, Integer]> positions;
            positions.add([`construct`, 0]);
        }
    }
}