package org.eclipse.ceylon.launcher;




public class Java7Checker {

    public static void check() {
        String version = System.getProperty("java.version");
        String[] elems = (version != null) ? version.split("\\.|_|-") : null;
        if (version != null && !version.isEmpty() && elems != null && elems.length >= 1) {
            try {
                int major = Integer.parseInt(elems[0]);
                int minor = elems.length > 1 ? Integer.parseInt(elems[1]) : 0;
                //int release = Integer.parseInt(elems[2]);
                if (major == 1 && minor < 7) {
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
