package com.redhat.ceylon.compiler.typechecker.parser;

/**
 * Contains methods for checking if a token is a Ceylon keyword. Synchronised from Ceylon.g
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class ParseUtil {

    // WARNING: keep in sync with Ceylon.g
    private static String[] keywords = new String[]{
        "abstracts"
        ,"alias"
        ,"assembly"
        ,"assert"
        ,"assign"
        ,"break"
        ,"case"
        ,"catch"
        ,"class"
        ,"continue"
        ,"dynamic"
        ,"else"
        ,"exists"
        ,"extends"
        ,"finally"
        ,"for"
        ,"function"
        ,"given"
        ,"if"
        ,"import"
        ,"in"
        ,"interface"
        ,"is"
        ,"let"
        ,"module"
        ,"new"
        ,"nonempty"
        ,"object"
        ,"of"
        ,"out"
        ,"outer"
        ,"package"
        ,"return"
        ,"satisfies"
        ,"super"
        ,"switch"
        ,"then"
        ,"this"
        ,"throw"
        ,"try"
        ,"value"
        ,"void"
        ,"while"
    };

    public static boolean isCeylonKeyword(String token){
        for(String keyword : keywords)
            if(keyword.equals(token))
                return true;
        return false;
    }
    
    public static boolean isCeylonKeyword(String string, int start, int end) {
        int length = end - start;
        OUTER:
        for(int i=0;i<keywords.length;i++){
            String token = keywords[i];
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

    /** Prefixes the given name with a "\i" if it is a Ceylon keyword */
    public static String quoteIfCeylonKeyword(String name){
        if(isCeylonKeyword(name))
            return "\\i"+name;
        return name;
    }

    /**
     * Returns a copy of the given qualified name, but with any
     * keyword components in the name 
     * {@link #quoteIfCeylonKeyword(String) quoted} if necessary 
     * @param qualifiedName
     * @return
     */
    public static String quoteCeylonKeywords(String qualifiedName){
        // try not to work for nothing if we don't have to
        if(needsCeylonKeywordsQuoting(qualifiedName))
            return join(".", quoteCeylonKeywords(qualifiedName.split("\\.")));
        else
            return qualifiedName;
    }
    
    /**
     * Returns a copy of the given array of identifiers, 
     * {@link #quoteIfCeylonKeyword(String) quoting} keyword identifiers as 
     * necessary 
     * @param name The parts of a qualified name
     * @return The parts of the qualified name, quoted if necessary
     */
    public static String[] quoteCeylonKeywords(String[] name){
        String[] result = new String[name.length];
        for (int ii = 0; ii < name.length; ii++) {
            result[ii] = quoteIfCeylonKeyword(name[ii]);
        }
        return result;
    }

    private static boolean needsCeylonKeywordsQuoting(String qualifiedName) {
        int nextDot = qualifiedName.indexOf('.');
        int start = 0;
        while(nextDot != -1){
            if(isCeylonKeyword(qualifiedName, start, nextDot))
                return true;
            start = nextDot+1;
            nextDot = qualifiedName.indexOf('.', start);
        }
        return isCeylonKeyword(qualifiedName, start, qualifiedName.length());
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
