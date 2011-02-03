grammar Buidergen;

options { output=template; }

@members {
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
    
    
}

nodeList : (DESCRIPTION? node)+ EOF;

node : '^' '('
       n=NODE_NAME 
       { System.out.println("public " + className($n.text) + " build" + className($n.text) +"(CommonTree treeNode) {"); }
       { System.out.println("    " + className($n.text) + "node = new " + className($n.text) + "(treeNode);"); }
       { System.out.println("    build" + className($n.text) + "(treeNode, node);"); }
       { System.out.println("    return node;"); }
       { System.out.println("}\n"); }
       { System.out.println("public void build" + className($n.text) + "(CommonTree treeNode, " + className($n.text) + " node) {"); }
       extendsNode?
       (DESCRIPTION? subnode)*
       (DESCRIPTION? field)*
       ')' 
       { System.out.println("}\n"); }
     ;

extendsNode : ':' n=NODE_NAME 
              { System.out.println("    build" + className($n.text) + "(treeNode, node);"); }
            ;

subnode : n=NODE_NAME OPTIONAL?
          { System.out.println("    node.set" + className($n.text) + "(build" + className($n.text) + "(treeNode.getChild(\"" + $n.text + "\"));"); }
        | mn=NODE_NAME MANY 
          { System.out.println("    for (CommonTree childTreeNode: treeNode.getChildren(\"" + $mn.text + "\"))"); }
          { System.out.println("        node.add" + className($mn.text) + "(build" + className($mn.text) + "(childTreeNode));"); }
          { System.out.println("    }"); }
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
