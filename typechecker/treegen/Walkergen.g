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
    println("import java.util.List;");
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
          { println("        List<"+className($mn.text)+"> "+className($mn.text)+"s = node.get" + className($mn.text) +"s();"); }
          { println("        for (int i=0,l=" + className($mn.text) + "s.size();i<l;i++){"); }
          { println("            "+className($mn.text)+" subnode = "+className($mn.text)+"s.get(i);"); }
          { println("            subnode.visit(visitor);"); }
          { println("        }"); }
        | mn=NODE_NAME '*' f=FIELD_NAME
          { println("        List<"+className($mn.text)+"> "+className($mn.text)+"s = node.get" + initialUpper($f.text) +"s();"); }
          { println("        for (int i=0,l=" + className($mn.text) + "s.size();i<l;i++){"); }
          { println("            "+className($mn.text)+" subnode = "+className($mn.text)+"s.get(i);"); }
          { println("            subnode.visit(visitor);"); }
          { println("        }"); }
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
