grammar Treegen;

@parser::header { package com.redhat.ceylon.compiler.typechecker.treegen; }
@lexer::header { package com.redhat.ceylon.compiler.typechecker.treegen; }

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
    
    String initialUpper(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    
    void print(String text) {
       out.print(text); 
    }
    
    void println(String text) {
       out.println(text); 
    }
    
}

nodeList : { 
           println("package com.redhat.ceylon.compiler.typechecker.tree;\n");
           println("import static com.redhat.ceylon.compiler.typechecker.tree.Walker.*;\n");
           println("import org.antlr.runtime.tree.CommonTree;\n");
           println("import com.redhat.ceylon.compiler.typechecker.model.Class;");
           println("import com.redhat.ceylon.compiler.typechecker.model.Interface;");
           println("import com.redhat.ceylon.compiler.typechecker.model.Method;");
           println("import com.redhat.ceylon.compiler.typechecker.model.Value;");
           println("import com.redhat.ceylon.compiler.typechecker.model.Getter;");
           println("import com.redhat.ceylon.compiler.typechecker.model.Setter;");
           println("import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;");
           println("import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;");
           println("import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;");
           println("import com.redhat.ceylon.compiler.typechecker.model.ProducedType;");
           println("import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;\n");
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
       { println("        public " + className($n.text) + "(CommonTree treeNode) {" ); }
       { println("            super(treeNode);" ); }
       { println("        }\n" ); }
       { println("        public void visit(Visitor visitor) {" ); }
       { println("            visitor.visit(this);" ); }
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
          n=NODE_NAME '?'? f=FIELD_NAME ('(' NODE_NAME* ')')?
          { println("        private " + className($n.text) + " " + $f.text + ";"); }
          { println("        public " + className($n.text) + " get" + initialUpper($f.text) + "() { return " + $f.text + "; }"); }
          { println("        public void set" + initialUpper($f.text) + "(" + className($n.text) + " node) { " + $f.text + " = node; }\n"); }
        | n=NODE_NAME '?'? ('(' NODE_NAME* ')')?
          { println("        private " + className($n.text) + " " + fieldName($n.text) + ";"); }
          { println("        public " + className($n.text) + " get" + className($n.text) + "() { return " + fieldName($n.text) + "; }"); }
          { println("        public void set" + className($n.text) + "(" + className($n.text) + " node) { " + fieldName($n.text) + " = node; }\n"); }
        | mn=NODE_NAME '*' ('(' NODE_NAME* ')')?
          { println("        private List<" + className($mn.text) + "> " + fieldName($mn.text) + 
                               "s = new ArrayList<" + className($mn.text) + ">();"); }
          { println("        public List<" + className($mn.text) + "> get" + className($mn.text) + "s() { return " + fieldName($mn.text) + "s; }"); }
          { println("        public void add" + className($mn.text) + "(" + className($mn.text) + " node) { " + fieldName($mn.text) + "s.add(node); }\n"); }
        | mn=NODE_NAME '*' f=FIELD_NAME ('(' NODE_NAME* ')')?
          { println("        private List<" + className($mn.text) + "> " + $f.text + 
                               "s = new ArrayList<" + className($mn.text) + ">();"); }
          { println("        public List<" + className($mn.text) + "> get" + initialUpper($f.text) + "s() { return " + $f.text + "s; }"); }
          { println("        public void add" + initialUpper($f.text) + "(" + className($mn.text) + " node) { " + $f.text + "s.add(node); }\n"); }
        ;

field : t=TYPE_NAME f=FIELD_NAME 
          { println("        private " + $t.text + " " + $f.text+ ";"); }
          { println("        public " + $t.text + " get" + initialUpper($f.text) + "() { return " + $f.text + "; }"); }
          { println("        public void set" + initialUpper($f.text) + "(" + $t.text + " value) { " + $f.text + " = value; }\n"); }
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
