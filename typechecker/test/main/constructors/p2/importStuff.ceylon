import constructors.p1 { Foo { New }, foo { bar }, barObject=bar { Bar } }

void testImported() {
    Foo foo = New();
    String str = bar;
    @type:"bar.Bar" Bar b = Bar("bar");
    Bar bb = barObject.Bar("");
    String bbar = b.bar;
    @type:"bar.Bar" Bar("bar");
    @type:"bar.Bar" barObject.Bar("bar");
    @type:"Foo" New();
}