grammar Treegen;

@parser::header { package com.redhat.ceylon.compiler.treegen; }
@lexer::header { package com.redhat.ceylon.compiler.treegen; }

@members {

    public java.io.PrintStream out = System.out;

    String className(String nodeName) { 
        return toJavaIdentifier(nodeName, true); 
    }
    
    String fieldName(String nodeName) { 
        return toJavaIdentifier(nodeName, false); 
    }
    
    String toJavaIdentifier(String nodeName, boolean boundary) {
        StringBuilder result = new StringBuilder();
        for (char c: nodeName.toCharArray()) {
            if (c=='_') {
                boundary=true;
            }
            else if (boundary) {
                result.append(c);
                boundary=false;
            }
            else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
    
    void print(String text) {
       out.print(text); 
    }
    
    void println(String text) {
       out.println(text); 
    }
    
}

nodeList : { 
           println("package com.redhat.ceylon.compiler.tree;\n");
           println("import static com.redhat.ceylon.compiler.tree.Walker.*;\n");
           println("import org.antlr.runtime.tree.CommonTree;\n");
           println("import java.util.*;\n");
           println("public class Tree {\n");
           }
           (nodeDescription? node)+ 
           EOF
           { println("}"); }
           ;

node : '^' '(' 
       { print("    public static class "); }
       n=NODE_NAME 
       { print(className($n.text)); }
       extendsNode
       { println(" {\n"); }
       { println("        public " + className($n.text) + "(CommonTree treeNode) {" ); }
       { println("            super(treeNode);" ); }
       { println("        }\n" ); }
       { println("        public void visit(Visitor visitor) {" ); }
       { println("            visitor.visit(this);" ); }
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

subnode : n=NODE_NAME '?'? ('(' NODE_NAME* ')')?
          { println("        private " + className($n.text) + " " + fieldName($n.text) + ";"); }
          { println("        public " + className($n.text) + " get" + className($n.text) + "() { return " + fieldName($n.text) + "; }"); }
          { println("        public void set" + className($n.text) + "(" + className($n.text) + " node) { " + fieldName($n.text) + " = node; }\n"); }
        | mn=NODE_NAME '*' ('(' NODE_NAME* ')')?
          { println("        private List<" + className($mn.text) + "> " + fieldName($mn.text) + 
                               " = new ArrayList<" + className($mn.text) + ">();"); }
          { println("        public List<" + className($mn.text) + "> get" + className($mn.text) + "() { return " + fieldName($mn.text) + "; }"); }
          { println("        public void add" + className($mn.text) + "(" + className($mn.text) + " node) { " + fieldName($mn.text) + ".add(node); }\n"); }
        ;

field : t=TYPE_NAME f=FIELD_NAME 
          { println("        private " + $t.text + " " + $f.text+ ";"); }
          { println("        public " + $t.text + " get" + $f.text + "() { return " + $f.text + "; }"); }
          { println("        public void set" + $t.text + "(" + $f.text + " value) { " + $f.text + " = value; }\n"); }
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
