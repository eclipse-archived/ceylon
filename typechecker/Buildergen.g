grammar Buildergen;

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
    println("import static com.redhat.ceylon.compiler.parser.CeylonParser.*;\n");
    println("import org.antlr.runtime.tree.CommonTree;\n");
    println("import java.util.*;\n");
    println("public class TreeBuilder {\n");
    println("CommonTree getChild(CommonTree node, int type) {");
    println("    for (CommonTree child: (List<CommonTree>) node.getChildren()) {");
    println("        if (type==child.getType()) return child;");
    println("    }");
    println("    return null;");
    println("}\n");
    println("List<CommonTree> getChildren(CommonTree node, int type) {");
    println("    List<CommonTree> list = new ArrayList<CommonTree>();");
    println("    for (CommonTree child: (List<CommonTree>) node.getChildren()) {");
    println("        if (type==child.getType()) list.add(child);");
    println("    }");
    println("    return list;");
    println("}\n");
    }
           (DESCRIPTION? node)+ 
           EOF
    { println("}"); }
           ;

node : '^' '('
       n=NODE_NAME 
       { println("public " + className($n.text) + " build" + className($n.text) +"(CommonTree treeNode) {"); }
       { println("    " + className($n.text) + " node = new " + className($n.text) + "(treeNode);"); }
       { println("    build" + className($n.text) + "(treeNode, node);"); }
       { println("    return node;"); }
       { println("}\n"); }
       { println("public void build" + className($n.text) + "(CommonTree treeNode, " + className($n.text) + " node) {"); }
       extendsNode?
       (DESCRIPTION? subnode)*
       (DESCRIPTION? field)*
       ')' 
       { println("}\n"); }
     ;

extendsNode : ':' n=NODE_NAME 
              { println("    build" + className($n.text) + "(treeNode, node);"); }
            ;

subnode : n=NODE_NAME OPTIONAL?
          { println("    node.set" + className($n.text) + "(build" + className($n.text) + "(getChild(treeNode, \"" + $n.text + "\"));"); }
        | mn=NODE_NAME MANY 
          { println("    for (CommonTree childTreeNode: getChildren(treeNode, " + $mn.text + ")) {"); }
          { println("        node.add" + className($mn.text) + "(build" + className($mn.text) + "(childTreeNode));"); }
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
