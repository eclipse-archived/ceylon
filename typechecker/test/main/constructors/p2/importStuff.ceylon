import constructors.p1 {
    Foo { create },
    Generic { broken },
    foo { bar },
    barObject=bar { Bar }
}

void testImported() {
    Foo foo = create();
    String str = bar;
    @type:"bar.Bar" Bar b = Bar("bar");
    Bar bb = barObject.Bar("");
    String bbar = b.bar;
    @type:"bar.Bar" Bar("bar");
    @type:"bar.Bar" barObject.Bar("bar");
    @type:"Foo.create" create();
    @type:"Foo" value newfoo = create();
    @type:"Generic<String>" value gen = Generic<String>.broken("");
    @type:"Generic<String>" value gen1 = Generic.broken("");
    @type:"Generic<String>" value gen2 = broken("");
    //@error Broken gen3;
    @error Generic gen4;
}