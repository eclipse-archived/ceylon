package com.redhat.ceylon.common;

public class OSUtil {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0;
    private static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;
    
	public static boolean isWindows() {
		return IS_WINDOWS;
	}
    
	public static boolean isMac() {
		return IS_MAC;
	}
    
	public static boolean isUnix() {
		return !IS_WINDOWS && !IS_MAC;
	}
}
