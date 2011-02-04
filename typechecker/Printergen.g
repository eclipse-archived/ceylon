grammar Printergen;

options { output=template; }

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

nodeList : 
    {
    println("package com.redhat.ceylon.compiler.tree;\n");
    println("import static com.redhat.ceylon.compiler.tree.Tree.*;\n");
    println("public class Printer {\n");
    }
           (DESCRIPTION? node)+ 
           EOF
    { println("}"); }
           ;

node : '^' '('
       n=NODE_NAME 
       { println("public void print" + className($n.text) +"(" + className($n.text) + " node) {"); }
       { println("    System.out.println(\"" + className($n.text) + " {\");"); }
       extendsNode?
       (DESCRIPTION? subnode)*
       (DESCRIPTION? field)*
       ')' 
       { println("    System.out.println(\"}\");"); }
       { println("}"); }
     ;

extendsNode : ':' n=NODE_NAME 
              { println("    print" + className($n.text) + "(node);"); }
            ;

subnode : n=NODE_NAME OPTIONAL?
          { println("    System.out.print(\"" + fieldName($n.text) + "=\");"); }
          { println("    print" + className($n.text) + "(node.get" + className($n.text) + "());"); }
        | mn=NODE_NAME MANY 
          { println("    System.out.println(\"" + fieldName($mn.text) + "=\");"); }
          { println("    for (" + className($mn.text) + " subnode: node.get" + className($mn.text) +"()) {"); }
          { println("        print" + className($mn.text) + "(subnode);"); }
          { println("    }"); }
        ;

field : t=TYPE_NAME f=FIELD_NAME ';'
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
