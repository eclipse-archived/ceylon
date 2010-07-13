class Token:
    def __init__(self, name):
        self.name = name

    convert = {
        "ANON":   "anonymous",
        "ARG":    "argument",
        "CST":    "constant",
        "CONCAT": "concatenation",
        "DECL":   "declaration",
        "EXPR":   "expression",
        "INIT":   "initializer",
        "INST":   "instance",
        "LANG":   "language",
        "METH":   "method",
        "RET":    "return",
        "STMT":   "statement"}

    @property
    def parts(self):
        return [self.convert.get(part, part) for part in self.name.split("_")]

    @property
    def klass(self):
        return "".join([part.title() for part in self.parts])

    @property
    def comment(self):
        parts = self.parts
        list = parts[-1] == "LIST"
        if list:
            comment = "A list of"
            parts.pop()
        else:
            comment = "A"
            if parts[0][0].upper() in "AEIOU" and parts[0] != "USER":
                comment += "n"
        for part in parts:
            comment += " "
            comment += part.lower()
        if list and parts[-1][-1].upper() != "S":
            comment += "s"
        return comment
    
def get_tokens():
    tokens, started = [], False
    for line in open("Ceylon.g", "r").xreadlines():
        line = line.split("//", 1)[0].strip()
        if not line:
            continue
        if not started:
            if line == "tokens {":
                started = True
        else:
            if line == "}":
                return tokens
            tokens.append(Token(line.rstrip(";")))

if __name__ == "__main__":
    tokens = [(token.name, token) for token in get_tokens()]
    tokens.sort()
    for name, token in tokens:
        if name in ("ABSTRACT_MEMBER_DECL", "INIT_EXPR"):
            print "    classes.put(CeylonParser.%s," % name
            print "                %s);" % token.klass
        else:
            print "    classes.put(CeylonParser.%-22s %s.class);" % (
                name + ",", token.klass)
    tokens = [(token.klass, token) for name, token in tokens]
    tokens.sort()
    print
    for klass, token in tokens:
        print "    public void visit(%-31s { visitDefault(that); }" % (
            klass + " that)")
    for klass, token in tokens:
        print """
  /**
    * %s
    */
  public static class %s extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }""" % (token.comment, klass)
