grammar Validatorgen;

@parser::header { 
    package com.redhat.ceylon.compiler.typechecker.treegen; 
    import static com.redhat.ceylon.compiler.typechecker.treegen.Util.*; 
}
@lexer::header { 
    package com.redhat.ceylon.compiler.typechecker.treegen; 
}

nodeList : { 
           println("package com.redhat.ceylon.compiler.typechecker.tree;\n");
           println("import static com.redhat.ceylon.compiler.typechecker.tree.Tree.*;");
           println("import static com.redhat.ceylon.compiler.typechecker.tree.Tree.Package;\n");
           println("public class Validator extends Visitor {\n");
           }
           (DESCRIPTION? node)+ 
           EOF
           { println("\n}"); }
           ;

node : '^' '('
       'abstract'? n=NODE_NAME (':' en=NODE_NAME)?
       { println("    public void visit(" + className($n.text) + " that) {"); }
       { println("        super.visit(that);"); }
       (DESCRIPTION? subnode)*
       (DESCRIPTION? field)*
       { println("    }\n"); }
       ')'
     ;

subnode : n=NODE_NAME f=FIELD_NAME
          { println("        if (that.get" + initialUpper($f.text) + "()==null)"); }
          { println("            that.addError(\"missing tokens\");"); }
        | n=NODE_NAME
          { println("        if (that.get" + className($n.text) + "()==null)"); }
          { println("            that.addError(\"missing " + description($n.text) + "\");"); }
        | NODE_NAME '?' FIELD_NAME?
        | NODE_NAME '*' FIELD_NAME?
        ;

field : 'abstract'? (TYPE_NAME|'boolean') FIELD_NAME ';'
      ;

NODE_NAME : ('A'..'Z'|'_')+;

FIELD_NAME : ('a'..'z') ('a'..'z'|'A'..'Z')*;
TYPE_NAME : ('A'..'Z') ('a'..'z'|'A'..'Z'|'<'|'>')*;

WS : (' ' | '\n' | '\t' | '\r' | '\u000C') { skip(); };

CARAT : '^';

LPAREN : '(';
RPAREN : ')';

MANY : '*'|'+';
OPTIONAL : '?';

EXTENDS : ':';

SEMI : ';';

DESCRIPTION : '\"' (~'\"')* '\"';
