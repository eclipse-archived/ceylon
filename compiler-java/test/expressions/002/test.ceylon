doc "Test method invocation"
void test(Process process) {
    Test t = Test(process);

    process.writeLine("Positional:");
    t.run("One", "Two", "Three");

    /* TODO
    process.writeLine("Named:");
    t.run(a = "Ham", b = "Eggs", c = "Chips");
    t.run(c = "Peas", b = "Chips", a = "Fish");
    t.run(b = "and", c = "Mash", a = "Pie");
    */
}
