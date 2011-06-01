package com.redhat.ceylon.compiler.tree;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.sun.tools.javac.util.List;

public class TreePrinter extends CeylonTree.Visitor {
    private PrintWriter out;

    public TreePrinter(Writer out) {
        this.out = new PrintWriter(out);
    }

    private int depth;

    private void indent() {
        out.println();
        for (int i = 0; i < depth; i++)
            out.print("  ");
    }

    private void enter(String what) {
        indent();
        out.print("(" + what);
        depth++;
    }

    private void leave() {
        depth--;
        out.print(")");
    }

    private String getShortShortName(String shortName) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < shortName.length(); i++) {
            char c = shortName.charAt(i);
            if (Character.isUpperCase(c))
                builder.append(Character.toLowerCase(c));
        }
        return builder.toString();
    }

    private static class NameValuePair implements Comparable<NameValuePair> {
        public String name;
        public Object value;

        public NameValuePair(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public int compareTo(NameValuePair that) {
            boolean thisIsName = this.name.equals("name");
            boolean thatIsName = that.name.equals("name");
            if (thisIsName) {
                if (thatIsName)
                    return 0;
                return -1;
            }
            if (thatIsName)
                return 1;
            return this.name.compareTo(that.name);
        }
    }

    private List<NameValuePair> getFields(CeylonTree tree) {
        return getFields(tree.getClass(), tree);
    }

    private List<NameValuePair> getFields(Class<?> klass, CeylonTree tree) {
        List<NameValuePair> result;
        if (klass == CeylonTree.class)
            result = List.<NameValuePair> nil();
        else
            result = getFields(klass.getSuperclass(), tree);

        List<NameValuePair> tmpL = List.<NameValuePair> nil();
        for (Field field : klass.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()))
                continue;

            if (field.getAnnotation(CeylonTree.NotPrintedInDump.class) != null)
                continue;

            String name = field.getName();
            Object value;
            try {
                value = field.get(tree);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            tmpL = tmpL.append(new NameValuePair(name, value));
        }

        NameValuePair[] tmpA = new NameValuePair[tmpL.size()];
        tmpL.toArray(tmpA);
        Arrays.sort(tmpA);

        return result.appendList(List.<NameValuePair> from(tmpA));
    }

    public void visitDefault(CeylonTree tree) {
        String shortName = tree.getClassName();
        String shortShortName = getShortShortName(shortName);

        enter(shortName);
        for (NameValuePair field : getFields(tree)) {
            Object value = field.value;
            if (value == null)
                continue;

            {
                String source = tree.source != null ? tree.source.line + ":" + tree.source.column : ":";
                enter("[" + source + "] " + shortShortName + "." + field.name);
            }

            if (value instanceof String || value instanceof Boolean) {
                out.print(" " + value);
            } else if (value instanceof Number) {
                out.print(" ");
                if (field.name.equals("operatorKind")) {
                    int operatorKind = ((Number) value).intValue();
                    out.print(CeylonParser.tokenNames[operatorKind]);
                } else {
                    out.print(value);
                }
            } else if (value instanceof CeylonTree) {
                ((CeylonTree) value).accept(this);
            } else if (value instanceof Iterable<?>) {
                for (Object obj : (Iterable<?>) value) {
                    if (obj instanceof CeylonTree) {
                        CeylonTree child = (CeylonTree) obj;
                        if (child != null)
                            child.accept(this);
                        else
                            out.print("(NULL)");
                    } else {
                        out.print(" " + obj + ",");
                    }
                }
            } else {
                throw new RuntimeException(value.getClass().getName());
            }
            leave();
        }
        leave();
    }
}
