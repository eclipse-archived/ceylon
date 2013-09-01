grammar Treegen;

@parser::header { 
    package com.redhat.ceylon.compiler.typechecker.treegen; 
    import static com.redhat.ceylon.compiler.typechecker.treegen.Util.*; 
}
@lexer::header { 
    package com.redhat.ceylon.compiler.typechecker.treegen; 
}

nodeList : { 
           println("package com.redhat.ceylon.compiler.typechecker.tree;\n");
           println("import static com.redhat.ceylon.compiler.typechecker.tree.Walker.*;\n");
           println("import org.antlr.runtime.Token;\n");
           println("import java.util.*;\n");
           println("public class Tree {\n");
           }
           (nodeDescription? node)+ 
           EOF
           { println("}"); }
           ;

node : '^' '(' 
       { print("    public static "); }
       ('abstract' { print("abstract "); } )?
       { print("class "); }
       n=NODE_NAME 
       { print(className($n.text)); }
       extendsNode
       { println(" {\n"); }
       { println("        public " + className($n.text) + "(Token token) {" ); }
       { println("            super(token);" ); }
       { println("        }\n" ); }
       { println("        @Override public void visit(Visitor visitor) {" ); }
       { println("            try {" ); }
       { println("                visitor.visit(this);" ); }
       { println("            }" ); }
       { println("            catch (Exception e) {" ); }
       { println("                visitor.handleException(e, this);"); }
       { println("            }" ); }
       { println("        }\n" ); }
       { println("        @Override public void visitChildren(Visitor visitor) {" ); }
       { println("            walk" + className($n.text) +"(visitor, this);"); }      
       { println("        }\n" ); }
       (memberDescription? subnode)*
       (memberDescription? field)*
       ')' 
       { println("    }\n"); }
     ;

extendsNode : ':' n=NODE_NAME 
              { print(" extends " + className($n.text)); }
            | { print(" extends Node"); }
            ;

nodeDescription : d=DESCRIPTION 
                  { println("    /**\n     * " + $d.text.replace("\"", "") + "\n     */"); }
                  ;

memberDescription : d=DESCRIPTION 
                    { println("        /** \n         * " + $d.text.replace("\"", "") + "\n         */"); }
                  ;

subnode : 
          n=NODE_NAME '?'? f=FIELD_NAME
          { println("        private " + className($n.text) + " " + $f.text + ";"); }
          { println("        public " + className($n.text) + " get" + initialUpper($f.text) + "() { return " + $f.text + "; }"); }
          { println("        public void set" + initialUpper($f.text) + "(" + className($n.text) + " node) { " + $f.text + " = node; connect(node); }\n"); }
        | n=NODE_NAME '?'?
          { println("        private " + className($n.text) + " " + fieldName($n.text) + ";"); }
          { println("        public " + className($n.text) + " get" + className($n.text) + "() { return " + fieldName($n.text) + "; }"); }
          { println("        public void set" + className($n.text) + "(" + className($n.text) + " node) { " + fieldName($n.text) + " = node; connect(node); }\n"); }
        | mn=NODE_NAME '*'
          { println("        private List<" + className($mn.text) + "> " + fieldName($mn.text) + 
                               "s = new ArrayList<" + className($mn.text) + ">(3);"); }
          { println("        public List<" + className($mn.text) + "> get" + className($mn.text) + "s() { return " + fieldName($mn.text) + "s; }"); }
          { println("        public void add" + className($mn.text) + "(" + className($mn.text) + " node) { " + fieldName($mn.text) + "s.add(node); connect(node); }\n"); }
        | mn=NODE_NAME '*' f=FIELD_NAME
          { println("        private List<" + className($mn.text) + "> " + $f.text + 
                               "s = new ArrayList<" + className($mn.text) + ">(3);"); }
          { println("        public List<" + className($mn.text) + "> get" + initialUpper($f.text) + "s() { return " + $f.text + "s; }"); }
          { println("        public void add" + initialUpper($f.text) + "(" + className($mn.text) + " node) { " + $f.text + "s.add(node); connect(node); }\n"); }
        ;

field : t=TYPE_NAME f=FIELD_NAME
          { println("        private com.redhat.ceylon.compiler.typechecker.model." + $t.text + " " + $f.text+ ";"); }
          { println("        public com.redhat.ceylon.compiler.typechecker.model." + $t.text + " get" + initialUpper($f.text) + "() { return " + $f.text + "; }"); }
          { println("        public void set" + initialUpper($f.text) + "(com.redhat.ceylon.compiler.typechecker.model." + $t.text + " value) { " + $f.text + " = value; }\n"); }
        ';'
      | 'boolean' f=FIELD_NAME
          { println("        private boolean " + $f.text+ ";"); }
          { println("        public boolean get" + initialUpper($f.text) + "() { return " + $f.text + "; }"); }
          { println("        public void set" + initialUpper($f.text) + "(boolean value) { " + $f.text + " = value; }\n"); }
        ';'
      | l=TYPE_NAME '<' t=TYPE_NAME '>' f=FIELD_NAME
          { println("        private " + $l.text + "<com.redhat.ceylon.compiler.typechecker.model." + $t.text + "> " + $f.text+ ";"); }
          { println("        public " + $l.text + "<com.redhat.ceylon.compiler.typechecker.model." + $t.text + "> get" + initialUpper($f.text) + "() { return " + $f.text + "; }"); }
          { println("        public void set" + initialUpper($f.text) + "(" + $l.text + "<com.redhat.ceylon.compiler.typechecker.model." + $t.text + "> value) { " + $f.text + " = value; }\n"); }
        ';'
      | 'abstract' t=TYPE_NAME f=FIELD_NAME
          { println("        public abstract com.redhat.ceylon.compiler.typechecker.model." + $t.text + " get" + initialUpper($f.text) + "();\n"); }
        ';'
      ;

NODE_NAME : ('A'..'Z'|'_')+;

FIELD_NAME : ('a'..'z') ('a'..'z'|'A'..'Z')*;
TYPE_NAME : ('A'..'Z') ('a'..'z'|'A'..'Z')*;

WS : (' ' | '\n' | '\t' | '\r' | '\u000C') { skip(); };

CARAT : '^';

LPAREN : '(';
RPAREN : ')';

MANY : '*'|'+';
OPTIONAL : '?';

EXTENDS : ':';

SEMI : ';';

DESCRIPTION : '\"' (~'\"')* '\"';
