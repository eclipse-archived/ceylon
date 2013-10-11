grammar VisitorAdaptorgen;

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
           println("public abstract class VisitorAdaptor extends Visitor {\n");
           println("    public void handleException(Exception e, Node that) { that.handleException(e, this); }\n");
           println("    public void visitAny(Node that) { that.visitChildren(this); }\n");
           }
           (DESCRIPTION? node)+ 
           EOF
           { println("\n}"); }
           ;

node : '^' '('
       'abstract'? n=NODE_NAME
       (
         {
           println("    public void visit" + className($n.text) + "(" + className($n.text) + " that) { visitAny(that); }");
           println("    @Override public final void visit(" + className($n.text) + " that) { visit" + className($n.text) + "(that); }");
         }
       | ':' en=NODE_NAME
         {
           println("    public void visit" + className($n.text) + "(" + className($n.text) + " that) { visit" + className($en.text) + "(that); }");
           println("    @Override public final void visit(" + className($n.text) + " that) { visit" + className($n.text) + "(that); }");
         }
       ) 
       (DESCRIPTION? subnode)*
       (DESCRIPTION? field)*
       ')'
     ;

subnode : n=NODE_NAME '?'? f=FIELD_NAME? 
        | mn=NODE_NAME '*' f=FIELD_NAME?
        ;

field : 'abstract'? (TYPE_NAME|'boolean') FIELD_NAME ';';

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
