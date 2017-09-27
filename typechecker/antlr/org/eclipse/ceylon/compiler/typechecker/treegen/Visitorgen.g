grammar Visitorgen;

@parser::header { 
    package org.eclipse.ceylon.compiler.typechecker.treegen; 
    import static org.eclipse.ceylon.compiler.typechecker.treegen.Util.*; 
}
@lexer::header { 
    package org.eclipse.ceylon.compiler.typechecker.treegen; 
}

nodeList : {
           println("package org.eclipse.ceylon.compiler.typechecker.tree;\n");
           println("import static org.eclipse.ceylon.compiler.typechecker.tree.Tree.*;");
           println("import static org.eclipse.ceylon.compiler.typechecker.tree.Tree.Package;\n");
           println("public abstract class Visitor {\n");

           println("    public static interface ExceptionHandler {");
           println("        /*");
           println("         * Returns true if the exception has been handled, false if it should be handled");
           println("         * by the visitor itself");
           println("         */");
           println("        boolean handleException(Exception e, Node that);");
           println("    }");
           println("    private ExceptionHandler externalExceptionHandler = null;");
           println("    public Visitor setExceptionHandler(ExceptionHandler exceptionHandler) { externalExceptionHandler = exceptionHandler; return this; }");
           println("    public void handleException(Exception e, Node that) {");
           println("        if (externalExceptionHandler != null &&");
           println("                externalExceptionHandler.handleException(e, that)) {");
           println("            return;");
           println("        }");
           println("        that.handleException(e, this);"); 
           println("    }");
           
           println("    public void visitAny(Node that) { that.visitChildren(this); }\n");
           }
           (DESCRIPTION? node)+ 
           EOF
           { println("\n}"); }
           ;

node : '^' '('
       'abstract'? n=NODE_NAME
       (
         { println("    public void visit(" + className($n.text) + " that) { visitAny(that); }"); }
       | ':' en=NODE_NAME
         { println("    public void visit(" + className($n.text) + " that) { visit((" + className($en.text) + ") that); }"); }
       ) 
       (DESCRIPTION? subnode)*
       (DESCRIPTION? field)*
       ')'
     ;

subnode : n=NODE_NAME '?'? f=FIELD_NAME?
        | mn=NODE_NAME '*' f=FIELD_NAME?
        ;

field : 'abstract'? (TYPE_NAME|'boolean'|'string') FIELD_NAME ';';

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
