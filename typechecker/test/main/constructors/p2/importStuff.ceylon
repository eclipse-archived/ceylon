import constructors.p1 {
    Foo { New },
    Generic { Broken },
    foo { bar },
    barObject=bar { Bar }
}

void testImported() {
    Foo foo = New();
    String str = bar;
    @type:"bar.Bar" Bar b = Bar("bar");
    Bar bb = barObject.Bar("");
    String bbar = b.bar;
    @type:"bar.Bar" Bar("bar");
    @type:"bar.Bar" barObject.Bar("bar");
    @type:"Foo.New" New();
    @type:"Foo" value newfoo = New();
    @type:"Generic<String>" value gen = Generic<String>.Broken("");
    @type:"Generic<String>" value gen1 = Generic.Broken("");
    @type:"Generic<String>" value gen2 = Broken("");
    @error Broken gen3;
    @error Generic gen4;
}