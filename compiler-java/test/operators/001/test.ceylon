doc "Test arithmetic operators"
void test(Process process) {
    process.writeLine("prefix increment");
    mutable Natural a = 1;
    process.writeLine(a);
    Natural b = ++a;
    process.writeLine(a);
    process.writeLine(b);

    process.writeLine("postfix increment");
    Natural c = a++;
    process.writeLine(a);
    process.writeLine(c);

    process.writeLine("prefix decrement");
    mutable Natural d = 57;
    process.writeLine(d);
    Natural e = --d;
    process.writeLine(d);
    process.writeLine(e);

    process.writeLine("postfix decrement");
    Natural f = d--;
    process.writeLine(d);
    process.writeLine(f);

    process.writeLine("exponentiation");
    Integer g = f ** a;
    process.writeLine(g);

    process.writeLine("negation");
    Integer h = -g;
    process.writeLine(h);
    Integer i = -c;
    process.writeLine(i);

    process.writeLine("slotwise complement");
    Integer j = ~g;
    process.writeLine(j);

    // TODO: format

    process.writeLine("multiplication");
    Integer k = j * i;
    process.writeLine(k);
    
    process.writeLine("division");
    Integer l = k / d;
    process.writeLine(l);
    
    process.writeLine("remainder");
    Integer m = k % d;
    process.writeLine(m);
    
    process.writeLine("slotwise and");
    Natural n = k.natural() & l.natural();
    process.writeLine(n);
    
    process.writeLine("addition");
    Integer o = n + j;
    process.writeLine(o);
    
    process.writeLine("subtraction");
    Natural p = (l - o).natural();
    process.writeLine(p);

    process.writeLine("slotwise or");
    Natural q = p | n;
    process.writeLine(q);

    process.writeLine("slotwise xor");
    Natural r = p ^ n;
    process.writeLine(r);

    /* TODO: WTF is this?
    process.writeLine("slotwise complement in");
    Natural s = q ~ r;
    process.writeLine(s);
    */
}
