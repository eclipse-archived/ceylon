package com.redhat.ceylon.common;

import java.util.HashSet;

public class JVMModuleUtil {
    /** Quote the given name by prefixing it with a dollar ($) */
    public static String quote(String name) {
        return "$"+name;
    }

    /** Removes dollar ($) prefix from the given name if it exists */
    public static String unquote(String name) {
        if (!name.isEmpty() && name.charAt(0) == '$') {
            return name.substring(1);
        }
        return name;
    }
    
    private static final HashSet<String> tokens;
    private static final String[] tokensArray =         new String[]{
        "abstract",
        "assert",
        "boolean",
        "break",
        "byte",
        "case",
        "catch",
        "char",
        "class",
        "const",
        "continue",
        "default",
        "do",
        "double",
        "else",
        "enum",
        "extends",
        "final",
        "finally",
        "float",
        "for",
        "goto",
        "if",
        "implements",
        "import",
        "instanceof",
        "int",
        "interface",
        "long",
        "native",
        "new",
        "package",
        "private",
        "protected",
        "public",
        "return",
        "short",
        "static",
        "strictfp",
        "super",
        "switch",
        "synchronized",
        "this",
        "throw",
        "throws",
        "transient",
        "try",
        "void",
        "volatile",
        "while",
        "true",
        "false",
        "null",
    };
    static {
        tokens = new HashSet<String>();
        for (String token : tokensArray) {
            tokens.add(token);
        }
    }
    
    /** Determines whether the given name is a Java keyword */
    public static boolean isJavaKeyword(String name) {
        return tokens.contains(name);
    }

    /** Determines whether the given name is a Java keyword */
    public static boolean isJavaKeyword(String string, int start, int end) {
        int length = end - start;
        OUTER:
        for(int i=0;i<tokensArray.length;i++){
            String token = tokensArray[i];
            if(token.length() != length)
                continue;
            for(int c=0;c<length;c++){
                if(string.charAt(c + start) != token.charAt(c))
                    continue OUTER;
            }
            return true;
        }
        return false;
    }

    /** Prefixes the given name with a dollar ($) if it is a Java keyword */
    public static String quoteIfJavaKeyword(String name){
        if(isJavaKeyword(name))
            return quote(name);
        return name;
    }

    /**
     * Returns a copy of the given qualified name, but with any
     * keyword components in the name 
     * {@link #quoteIfJavaKeyword(String) quoted} if necessary 
     * @param qualifiedName
     * @return
     */
    public static String quoteJavaKeywords(String qualifiedName){
        // try not to work for nothing if we don't have to
        if(qualifiedName != null && needsJavaKeywordsQuoting(qualifiedName))
            return join(".", quoteJavaKeywords(qualifiedName.split("\\.")));
        else
            return qualifiedName;
    }
    
    /**
     * Returns a copy of the given array of identifiers, 
     * {@link #quoteIfJavaKeyword(String) quoting} keyword identifiers as 
     * necessary 
     * @param name The parts of a qualified name
     * @return The parts of the qualified name, quoted if necessary
     */
    public static String[] quoteJavaKeywords(String[] name){
        String[] result = new String[name.length];
        for (int ii = 0; ii < name.length; ii++) {
            result[ii] = quoteIfJavaKeyword(name[ii]);
        }
        return result;
    }

    private static boolean needsJavaKeywordsQuoting(String qualifiedName) {
        int nextDot = qualifiedName.indexOf('.');
        int start = 0;
        while(nextDot != -1){
            if(isJavaKeyword(qualifiedName, start, nextDot))
                return true;
            start = nextDot+1;
            nextDot = qualifiedName.indexOf('.', start);
        }
        return isJavaKeyword(qualifiedName, start, qualifiedName.length());
    }

    /** Removes dollar ($) prefix from the given name if it was a quoted Java keyword */
    public static String unquoteIfJavaKeyword(String name){
        String unquoted = unquote(name);
        if(isJavaKeyword(unquoted))
            return unquoted;
        return name;
    }

    /**
     * Returns a copy of the given qualified name, but with any
     * keyword components in the name 
     * {@link #unquoteIfJavaKeyword(String) unquoted} if necessary 
     * @param qualifiedName
     * @return
     */
    public static String unquoteJavaKeywords(String qualifiedName){
        if(qualifiedName != null)
            return join(".", unquoteJavaKeywords(qualifiedName.split("\\.")));
        else
            return qualifiedName;
    }
    
    /**
     * Returns a copy of the given array of identifiers, 
     * {@link #unquoteIfJavaKeyword(String) unquoting} keyword identifiers as 
     * necessary 
     * @param name The parts of a qualified name
     * @return The parts of the qualified name, unquoted if necessary
     */
    public static String[] unquoteJavaKeywords(String[] name){
        String[] result = new String[name.length];
        for (int ii = 0; ii < name.length; ii++) {
            result[ii] = unquoteIfJavaKeyword(name[ii]);
        }
        return result;
    }

    /**
     * Joins the given parts using the given separator
     * @param sep The separator
     * @param parts The parts
     * @return The parts, joined with the separator
     */
    public static String join(String sep, String... parts) {
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part).append(sep);
        }
        return sb.subSequence(0, sb.length() - sep.length()).toString();
    }
}
