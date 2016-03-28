package com.redhat.ceylon.compiler.js.util;

public class JsUtils {

    /** Escapes a StringLiteral (needs to be quoted). */
    public static String escapeStringLiteral(String s) {
        StringBuilder text = new StringBuilder(s);
        //Escape special chars
        for (int i=0; i < text.length();i++) {
            final char c = text.charAt(i);
            switch(c) {
            case 8:text.replace(i, i+1, "\\b"); i++; break;
            case 9:text.replace(i, i+1, "\\t"); i++; break;
            case 10:text.replace(i, i+1, "\\n"); i++; break;
            case 12:text.replace(i, i+1, "\\f"); i++; break;
            case 13:text.replace(i, i+1, "\\r"); i++; break;
            case 34:text.replace(i, i+1, "\\\""); i++; break;
            case 39:text.replace(i, i+1, "\\'"); i++; break;
            case 92:text.replace(i, i+1, "\\\\"); i++; break;
            default:
                if (c < 32 || c > 127) {
                    final String rep = String.format("\\u%04x", (int)c);
                    text.replace(i, i+1, rep);
                    i+=rep.length()-1;
                }
            }
        }
        return text.toString();
    }

}
