/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.p2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.ceylon.common.ModuleSpec;

class ModuleInfo {
        private static final String[] PropertyNames = new String[]{
            "Bundle-Name",
            "Bundle-Vendor",
            "Bundle-Description",
            "Bundle-DocUrl",
        };
        final String name, version;
        final File jar;
        Attributes osgiAttributes;
        String osgiVersion;
        private CeylonP2Tool tool;
        
        public static class Dependency extends ModuleSpec {
            
            private boolean optional;

            public Dependency(String name, String version) {
                this(name, version, false);
            }

            public Dependency(String name, String version, boolean optional) {
                super(null, name, version);
                this.optional = optional;
            }
            
            public boolean isOptional() {
                return optional;
            }
        }

        public ModuleInfo(CeylonP2Tool tool, String name, String version, File jar) throws IOException {
            this.tool = tool;
            this.name = name;
            this.version = version;
            this.jar = jar;
            loadOsgiVersion();
        }

        private void loadOsgiVersion() throws IOException {
            JarFile jarFile = new JarFile(jar);
            Manifest manifest = jarFile.getManifest();
            osgiVersion = version;
            if(manifest != null){
                osgiAttributes = manifest.getMainAttributes();
                if(osgiAttributes != null){
                    String tmp = osgiAttributes.getValue("Bundle-Version");
                    if(tmp != null)
                        osgiVersion = tmp;
                }
            }
            jarFile.close();
            osgiVersion = CeylonP2Tool.fixOsgiVersion(osgiVersion);
        }

        public List<ModuleSpec> getExportedPackages() {
            String value = osgiAttributes.getValue("Export-Package");
            List<ModuleSpec> ret = new LinkedList<>();
            if(value != null){
                for(String pkg : split(value, ",")){
                    String[] details = split(pkg, ";");
                    String name = details[0];
                    String version = "";// GRRR: API of ModuleInfo
                    for(int i=1;i<details.length;i++){
                        if(details[i].startsWith("version=")){
                            version = unquote(details[i].substring(8));
                            break;
                        }
                    }
                    ret.add(new ModuleSpec(null, name, version));
                }
            }
            return ret;
        }

        private String unquote(String quotedVersion) {
            if(quotedVersion == null || quotedVersion.isEmpty())
                return quotedVersion;
            char first = quotedVersion.charAt(0);
            char last = quotedVersion.charAt(quotedVersion.length()-1);
            if(first == '"' && last == '"')
                return quotedVersion.substring(1, quotedVersion.length()-1);
            if(first == '\'' && last == '\'')
                return quotedVersion.substring(1, quotedVersion.length()-1);
            return quotedVersion;
        }

        static Pattern quotingPattern = Pattern.compile("(([^\"\']+)|(\"[^\"]*\")|(\'[^\']*\'))");
        public static String[] split(String string, String separator) {
            String quoteTag = "$$$$quote$$$$";
            Matcher matcher = quotingPattern.matcher(string);
            List<String> matchList = new ArrayList<String>();
            int start = 0;
            String stringWithReplacedQuotes = "";
            while (matcher.find(start)) {
                String match= matcher.group(0);
                char matchFirstChar = match.charAt(0);
                char matchLastChar = match.charAt(match.length()-1);
                if (matchFirstChar == '\'' && matchLastChar == '\'' || 
                        matchFirstChar == '\"' && matchLastChar == '\"') {
                    matchList.add(match);
                    stringWithReplacedQuotes += quoteTag;
                } else {
                    stringWithReplacedQuotes += match;
                }
                start = matcher.end();
            }
            
            String[] splittedStringWithReplacedQuotes = stringWithReplacedQuotes.split(separator);
            int quoteIndex = 0;
            for (int i=0; i<splittedStringWithReplacedQuotes.length; i++) {
                String part = splittedStringWithReplacedQuotes[i];
                while(part.contains(quoteTag)) {
                    part=part.replaceFirst(Pattern.quote(quoteTag), matchList.get(quoteIndex++));
                }
                splittedStringWithReplacedQuotes[i] = part;
            }
            return splittedStringWithReplacedQuotes;
        }

        public List<Dependency> getImportedPackages() {
            String value = osgiAttributes.getValue("Import-Package");
            List<Dependency> ret = new LinkedList<>();
            if(value != null){
                for(String pkg : split(value, ",")){
                    String[] details = split(pkg, ";");
                    String name = details[0];
                    String version = "";// GRRR: API of ModuleInfo
                    boolean optional = false;
                    for(int i=1;i<details.length;i++){
                        if(details[i].startsWith("version=")){
                            version = unquote(details[i].substring(8));
                        }
                        if(details[i].startsWith("resolution:=")){
                            String resolution = unquote(details[i].substring(12));
                            if ("optional".equals(resolution)) {
                                optional = true;
                            }
                        }
                    }
                    ret.add(new Dependency(name, version, optional));
                }
            }
            return ret;
        }

        public List<Dependency> getImportedModules() {
            String value = osgiAttributes.getValue("Require-Bundle");
            List<Dependency> ret = new LinkedList<>();
            if(value != null && !value.isEmpty()){
                for(String pkg : split(value, ",")){
                    String[] details = split(pkg, ";");
                    String name = details[0];
                    boolean optional = false;
                    String version = "";// GRRR: API of ModuleInfo
                    for(int i=1;i<details.length;i++){
                        if(details[i].startsWith("bundle-version=")){
                            version = unquote(details[i].substring(15));
                        }
                        if(details[i].startsWith("resolution:=")){
                            String resolution = unquote(details[i].substring(12));
                            if ("optional".equals(resolution)) {
                                optional = true;
                            }
                        }
                    }
                    // skip some modules
                    if(tool.skipModule(name, version))
                        continue;
                    ret.add(new Dependency(name, version, optional));
                }
            }
            return ret;
        }

        public String getAttribute(String name) {
            return osgiAttributes.getValue(name);
        }

        public SortedMap<String,String> getOsgiProperties() {
            SortedMap<String,String> ret = new TreeMap<>();
            for(String name : PropertyNames){
                String attribute = getAttribute(name);
                if(attribute != null){
                    ret.put(name, attribute);
                }
            }
            return ret;
        }
}