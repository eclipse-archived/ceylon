package com.redhat.ceylon.model.loader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OsgiVersion {
    private static HashMap<String, Integer> ceylonQualifiers;
    static {
        ceylonQualifiers = new HashMap<String, Integer>();
        ceylonQualifiers.put("alpha", 0);
        ceylonQualifiers.put("a", 0);
        ceylonQualifiers.put("beta", 1);
        ceylonQualifiers.put("b", 1);
        ceylonQualifiers.put("milestone", 2);
        ceylonQualifiers.put("m", 2);
        ceylonQualifiers.put("rc", 3);
        ceylonQualifiers.put("cr", 3);
        ceylonQualifiers.put("snapshot", 4);
        ceylonQualifiers.put("ga", 5);
        ceylonQualifiers.put("final", 5);
        ceylonQualifiers.put("sp", 6);
    }
    private static int QUALIFIER_OTHER = 6; // Same as largest index in above list
    
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmm");
    static {
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    
    public static String fromCeylonVersion(String ceylonVersion, boolean addTimeStamp) {
        // Insert a "." between digits and letters
        StringBuffer buf = new StringBuffer();
        Pattern p = Pattern.compile("\\d\\pL|\\pL\\d");
        Matcher m = p.matcher(ceylonVersion);
        while (m.find()) {
            String found = m.group();
            assert(found.length() == 2);
            m.appendReplacement(buf, found.charAt(0) + "." + found.charAt(1));
        }
        m.appendTail(buf);
        
        // Split on dots and dashes
        String[] versionParts = buf.toString().split("[\\.-]");

        // Now iterator over all the parts applying the following rules:
        // - any empty part is treated as "0"
        // - any number in the first 3 positions are added as-is
        // - any number after that gets padded with leading zeros to a length of 3 and prefixed with the letter "n"
        // - when a non-number is encountered in the first 3 positions enough "0" parts are added to have at least 3 numbers at the start
        // - a non-number gets lookup in the qualifier table and if found replaced by its index
        // - when not found we add the part prefixed with the largest index in the qualifier table and a dash
        boolean inOsgiQualifier = false;
        boolean lastWasQualifier = false;
        ArrayList<String> resultParts = new ArrayList<String>();
        for (int i = 0; i < versionParts.length; i++) {
            String part = versionParts[i];
            if (part.isEmpty()) {
                part = "0";
            }
            inOsgiQualifier = inOsgiQualifier || i >= 3;
            if (isNumber(part)) {
                if (inOsgiQualifier) {
                    resultParts.add(String.format("n%03d", Integer.parseInt(part)));
                } else {
                    resultParts.add(part);
                }
                lastWasQualifier = false;
            } else {
                if (!inOsgiQualifier) {
                    // We need our version to start with at least 3 numbers
                    for (int j = i; j < 3; j++) {
                        resultParts.add("0");
                    }
                    inOsgiQualifier = true;
                }
                Integer idx = ceylonQualifiers.get(part.toLowerCase());
                if (idx != null) {
                    resultParts.add(idx.toString());
                } else {
                    resultParts.add(QUALIFIER_OTHER + "-" + part);
                }
                lastWasQualifier = true;
            }
        }
        // We need our version to start with at least 3 numbers
        for (int j = resultParts.size(); j < 3; j++) {
            resultParts.add("0");
        }
        
        if (!lastWasQualifier) {
            // If we didn't terminate with a qualifier let's treat as if it was "final"
            resultParts.add(ceylonQualifiers.get("final").toString());
        }
        
        if (addTimeStamp) {
            resultParts.add(formatter.format(new Date()));
        }

        // Now join all the resulting parts together. The first 4
        // elements get separated by dots, the rest by dashes
        StringBuffer result = new StringBuffer();
        for (int i=0; i < resultParts.size(); i++) {
            String part = resultParts.get(i);
            if (i > 3) {
                result.append("-");
            } else if (i > 0) {
                result.append(".");
            }
            result.append(part);
        }
        
        return result.toString();
    }

    private static boolean isNumber(String txt) {
        return txt.matches("\\d+");
    }
}