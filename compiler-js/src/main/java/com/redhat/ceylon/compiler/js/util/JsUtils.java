package com.redhat.ceylon.compiler.js.util;

public class JsUtils {

    /** Escapes a StringLiteral (needs to be quoted). */
    public static String escapeStringLiteral(String s) {
        StringBuilder text = new StringBuilder(s);
        //Escape special chars
        for (int i=0; i < text.length();i++) {
            switch(text.charAt(i)) {
            case 8:text.replace(i, i+1, "\\b"); i++; break;
            case 9:text.replace(i, i+1, "\\t"); i++; break;
            case 10:text.replace(i, i+1, "\\n"); i++; break;
            case 12:text.replace(i, i+1, "\\f"); i++; break;
            case 13:text.replace(i, i+1, "\\r"); i++; break;
            case 34:text.replace(i, i+1, "\\\""); i++; break;
            case 39:text.replace(i, i+1, "\\'"); i++; break;
            case 92:text.replace(i, i+1, "\\\\"); i++; break;
            case 0x2028:text.replace(i, i+1, "\\u2028"); i++; break;
            case 0x2029:text.replace(i, i+1, "\\u2029"); i++; break;
            }
        }
        return text.toString();
    }

}
