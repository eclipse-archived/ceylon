class Token:
    def __init__(self, name, value):
        self.name  = name
        self.value = value

    def is_word(self):
        if self.value is None:
            return False
        for c in self.value:
            if not c.isalpha():
                return False
        return True
        
    def is_symbol(self):
        if self.value is None:
            return False
        for c in self.value:
            if c.isalpha():
                return False
        return True
        
    convert = {
        "ANON":      "anonymous",
        "ARG":       "argument",
        "CST":       "constant",
        "CONCAT":    "concatenation",
        "DECL":      "declaration",
        "DIVIDED":   "divide",
        "EQ":        "equal",
        "EXPR":      "expression",
        "INIT":      "initializer",
        "INST":      "instance",
        "LANG":      "language",
        "METH":      "method",
        "REMAINDER": "modulo",
        "RET":       "return",
        "STMT":      "statement",
        "WS":        "whitespace"}

    symbols = (
        "AND", "BITWISE", "COLON", "COMMA", "COMPARE", "DECREMENT",
        "DIVIDED", "DOT", "ELLIPSIS", "ENTRY", "EQ", "GT", "HASH",
        "IDENTICAL", "IMPLIES", "INCREMENT", "LBRACE", "LBRACKET",
        "LPAREN", "LT", "MINUS", "PLUS", "POWER", "NOT", "OR",
        "QMARK", "QUES", "RBRACE", "RBRACKET", "REMAINDER", "RENDER",
        "RPAREN", "SEMI", "STAR", "TIMES", "XOR")

    expand = {
        "GT":       ("greater", "than"),
        "LBRACE":   ("left", "brace"),
        "LBRACKET": ("left", "bracket"),
        "LPAREN":   ("left", "paren"),
        "LT":       ("less", "than"),
        "QMARK":    ("question", "mark"),
        "QUES":     ("question", "mark"),
        "RBRACE":   ("right", "brace"),
        "RBRACKET": ("right", "bracket"),
        "RPAREN":   ("right", "paren")}

    @property
    def parts(self):
        name = self.name
        if name.endswith("LITERAL"):
            name = name[:-7] + "_" + name[-7:]
        if name.startswith("SIMPLE"):
            name = name[:6] + "_" + name[6:]
        if name.startswith("LINE_"):
            name = "SINGLE_" + name
        if name.startswith("MULTI_"):
            name = name[:6] + "LINE_" + name[6:]
        if self.is_symbol():
            parts = self.split_symbol(name)
        else:
            parts = name.split("_")
        return [self.convert.get(part, part) for part in parts]

    def split_symbol(self, name):
        parts = []
        while name:
            for word in self.symbols:
                if name.startswith(word):
                    break
            else:
                raise ValueError, name
            parts.extend(self.expand.get(word, (word,)))
            name = name[len(word):]
        return parts

    klassmap = {
        "LIDENTIFIER": "LIdentifier",
        "UIDENTIFIER": "UIdentifier"}

    @property
    def klass(self):
        if self.klassmap.has_key(self.name):
            return self.klassmap[self.name]
        return "".join([part.title() for part in self.parts])

    commentmap = {
        "IF_FALSE": "The taken-if-false block of an if clause",
        "IF_TRUE": "The taken-if-true block of an if clause",
        "LIDENTIFIER": "An identifier whose first character is lowercase",
        "UIDENTIFIER": "An identifier whose first character is uppercase",
        "WS":          "Whitespace"}

    commentwords = {
        "abstracts": "abstracted types",
        "satisfies": "satisfied types",
        "char":      "character"}
    
    @property
    def comment(self):
        if self.commentmap.has_key(self.name):
            return self.commentmap[self.name]
        if self.is_word():
            return 'The word "%s"' % self.value
        if self.is_symbol():
            return 'The symbol "%s"' % self.value
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
            part = part.lower()
            comment += " "
            comment += self.commentwords.get(part, part)
        if list and parts[-1][-1].upper() != "S":
            comment += "s"
        return comment

class Grammar:
    def __init__(self, path):
        self.lines = [line.strip() for line in open(path, "r").xreadlines()]
        self.indexes = {}
        for line, index in zip(self.lines, xrange(len(self.lines))):
            if self.indexes.has_key(line):
                self.indexes[line] = None
            else:
                self.indexes[line] = index

    funny_tokens = {
        "DOT":      ".",
        "ELLIPSIS": "..."}

    def lexer_token(self, name):
        if self.funny_tokens.has_key(name):
            return self.funny_tokens[name]
        index = self.indexes.get(name, None)
        if index is None:
            return
        line = self.lines[index + 2].strip()
        if line != ";":
            return
        line = self.lines[index + 1].strip()
        line = line.split()
        if len(line) != 2:
            return
        if line[0] != ":":
            return
        token = line[1]
        if (token[0], token[-1]) != ("'", "'"):
            return
        token = token[1:-1]
        if "'" in token:
            return
        return token
    
def get_tokens():
    grammar = Grammar("Ceylon.g")
    path = "src/com/redhat/ceylon/compiler/parser/CeylonParser.java"
    prefix, suffix = "public static final int ", ";"
    tokens = []
    for line in open(path, "r").xreadlines():
        line = line.strip()
        if not line.startswith(prefix) or not line.endswith(suffix):
            continue
        line = line[len(prefix):-len(suffix)]
        name, value = line.split("=")
        value = int(value)
        if value < 1:
            continue
        if name.startswith("T__"):
            continue
        if [c for c in name if c.islower()]:
            continue
        tokens.append(Token(name, grammar.lexer_token(name)))
    return tokens

if __name__ == "__main__":
    tokens = [(token.name, token) for token in get_tokens()]
    tokens.sort()
    for name, token in tokens:
        if name in ("ABSTRACT_MEMBER_DECL", "INIT_EXPR"):
            print "    classes.put(CeylonParser.%s," % name
            print "                %s.class);" % token.klass
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
