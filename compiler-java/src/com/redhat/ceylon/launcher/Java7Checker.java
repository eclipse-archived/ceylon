package com.redhat.ceylon.launcher;
/*
 * Copyright 2012 Red Hat inc. and third party contributors as noted
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



public class Java7Checker {

    public static void check() {
        String version = System.getProperty("java.version");
        String[] elems = (version != null) ? version.split("\\.|_|-") : null;
        if (version != null && !version.isEmpty() && elems != null && elems.length >= 3) {
            try {
                int major = Integer.parseInt(elems[0]);
                int minor = Integer.parseInt(elems[1]);
                int release = Integer.parseInt(elems[2]);
                if (major != 1 || minor < 7) {
                    System.err.println("Your Java version is not supported: " + version);
                    System.err.println("Ceylon needs Java 7 or newer. Please install it from http://www.java.com");
                    System.err.println("Aborting.");
                    System.exit(1);
                }
                return;
            } catch (NumberFormatException ex) {}
        }
        System.err.println("Unable to determine Java version (java.version property missing, empty or has unexpected format: '" + version +"'). Aborting.");
        System.exit(1);
    }
    
}
