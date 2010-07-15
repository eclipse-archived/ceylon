import re

subclass = re.compile(r'''
    /\*\*
     \* The symbol .*?
     \*/
    public static class (.*?) extends CeylonTree \{
        public void accept\(Visitor v\) \{ v.visit\(this\); \}
    \}
''')

path = "src/com/redhat/ceylon/compiler/tree/CeylonTree.java"
text = open(path, "r").read()
while True:
    match = subclass.search(text)
    if not match:
        break
    print match.group(1)
    text = text.replace(match.group(0), "")
    text = text.replace(", %s.class" % match.group(1), ", Operator.class")
    junk = "        public void visit(%-31s { visitDefault(that); }\n" % (
        match.group(1) + " that)")
    old = text
    text = text.replace(junk, "")
    assert text != old
open(path, "w").write(text)
