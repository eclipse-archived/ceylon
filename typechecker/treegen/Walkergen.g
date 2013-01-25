grammar Walkergen;

@parser::header { 
    package com.redhat.ceylon.compiler.typechecker.treegen; 
    import static com.redhat.ceylon.compiler.typechecker.treegen.Util.*; 
}
@lexer::header { 
    package com.redhat.ceylon.compiler.typechecker.treegen; 
}

nodeList : 
    {
    println("package com.redhat.ceylon.compiler.typechecker.tree;\n");
    println("import static com.redhat.ceylon.compiler.typechecker.tree.Tree.*;");
    println("import static com.redhat.ceylon.compiler.typechecker.tree.Tree.Package;\n");
    println("public class Walker {\n");
    }
           (DESCRIPTION? node)+ 
           EOF
    { println("}"); }
           ;

node : '^' '('
       'abstract'? n=NODE_NAME 
       { println("    public static void walk" + className($n.text) +"(Visitor visitor, " + className($n.text) + " node) {"); }
       extendsNode?
       (DESCRIPTION? subnode)*
       (DESCRIPTION? field)*
       ')'
       { println("    }\n"); }
     ;

extendsNode : ':' 
              n=NODE_NAME
              { println("        walk" + className($n.text) +"(visitor, node);"); }
            ;

subnode : n=NODE_NAME '?'? f=FIELD_NAME
          { println("        if (node.get" + initialUpper($f.text) + "()!=null)"); }
          { println("            node.get" + initialUpper($f.text) + "().visit(visitor);"); }
        | n=NODE_NAME '?'?
          { println("        if (node.get" + className($n.text) + "()!=null)"); }
          { println("            node.get" + className($n.text) + "().visit(visitor);"); }
        | mn=NODE_NAME '*'
          { println("        for (" + className($mn.text) + " subnode: node.get" + className($mn.text) +"s())"); }
          { println("            subnode.visit(visitor);"); }
        | mn=NODE_NAME '*' f=FIELD_NAME
          { println("        for (" + className($mn.text) + " subnode: node.get" + initialUpper($f.text) +"s())"); }
          { println("            subnode.visit(visitor);"); }
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
