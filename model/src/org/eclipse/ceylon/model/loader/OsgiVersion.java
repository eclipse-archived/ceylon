/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader;

import java.text.ParseException;
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
    private static int QUALIFIER_OTHER = 7; // Same as the size of the above list
    
    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmm");
    static {
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static interface ErrorReporter {
        void reportError(String errorMessage, Throwable optionalThrowable);
    }
    
    public static String withTimestamp(String osgiVersion, String formattedDateInGmt) {
        return withTimestamp(osgiVersion, formattedDateInGmt, null);
    }

    public static String withTimestamp(String osgiVersion, String formattedDateInGmt, ErrorReporter errorReporter) {
        Date date;
        try {
            date = formatter.parse(formattedDateInGmt);
        } catch (ParseException e) {
            String errorMessage = "The provided OSGI qualifier timestamp cannot be parsed. The current date will be used instead.";
            if (errorReporter != null) {
                errorReporter.reportError(errorMessage, e);
            } else {
                System.err.println("ERROR: " + errorMessage);
            }
            date = new Date();
        }
        return withTimestamp(osgiVersion, date);
    }
    
    public static String withTimestamp(String osgiVersion) {
        return withTimestamp(osgiVersion, new Date());
    }
    
    public static String withTimestamp(String osgiVersion, Date date) {
        return osgiVersion + "-" + formatter.format(date);
    }
    
    public static String fromCeylonVersion(String ceylonVersion) {
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
        // - when not found we add the part prefixed with the size of the qualifier table and a dash
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
                    part = String.format("n%03d", Integer.parseInt(part));
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
                    part = idx.toString();
                } else {
                    part = QUALIFIER_OTHER + "-" + part;
                }
                lastWasQualifier = true;
            }
            resultParts.add(part);
        }
        // We need our version to start with at least 3 numbers
        for (int j = resultParts.size(); j < 3; j++) {
            resultParts.add("0");
            lastWasQualifier = false;
        }
        
        if (!lastWasQualifier) {
            // If we didn't terminate with a qualifier let's treat as if it was "final"
            resultParts.add(ceylonQualifiers.get("final").toString());
        }
        
        // Now join all the resulting parts together. The first 4
        // elements get separated by dots, the rest by dashes and the
        // qualifier always starts with 'osgi-'
        StringBuffer result = new StringBuffer();
        for (int i=0; i < resultParts.size(); i++) {
            String part = resultParts.get(i);
            if (i > 3) {
                result.append("-");
            } else if (i > 0) {
                result.append(".");
            }
            if (i == 3) {
                result.append("osgi-");
            }
            result.append(part);
        }
        
        return result.toString();
    }

    private static boolean isNumber(String txt) {
        return txt.matches("\\d+");
    }
}