void testGenericRefs() {
    $type:"<Element> => Boxx<Element>.create(Element)" value fun1 = Boxx.create;
    $type:"<Element> => Boxx<Element>[2](Element, Element)" value fun2 = Boxx.pair;
    
    <Element> => Boxx<Element>(Element) fun1ref = fun1;
    <Element> => Boxx<Element>[2](Element,Element) fun2ref = fun2;
    
    Boxx<Boolean> box1 = fun1(true);
    $error Boxx<Boolean> box2 = fun1("");
    Boxx<Boolean> box3 = Boxx.create(true);
    $error Boxx<Boolean> box4 = Boxx.create("");
    
    Boxx<Boolean>[2] pair1 = fun2(true, true);
    $error Boxx<Boolean>[2] pair2 = fun2("", "");
    Boxx<Boolean>[2] pair3 = Boxx.pair(true, true);
    $error Boxx<Boolean>[2] pair4 = Boxx.pair("", "");
}

class Boxx<Element> {
    shared static Boxx<Element>[2] pair(Element x, Element y)
            => [create(x), create(y)];
    shared new create(Element element) {}
}