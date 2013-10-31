package com.redhat.ceylon.cmr.api;

import java.util.Comparator;

/**
 * Compares two version strings according to the Debian rules:
 * 
 * http://www.debian.org/doc/debian-policy/ch-controlfields.html#s-f-Version
 *
 * [Start excerpt]
 * 
 * The strings are compared from left to right.
 * 
 * First the initial part of each string consisting entirely of non-digit characters is determined. 
 * These two parts (one of which may be empty) are compared lexically. If a difference is found it is returned. 
 * The lexical comparison is a comparison of ASCII values modified so that all the letters sort earlier than 
 * all the non-letters and so that a tilde sorts before anything, even the end of a part. For example, 
 * the following parts are in sorted order from earliest to latest: ~~, ~~a, ~, the empty part, a.[37]
 * 
 * Then the initial part of the remainder of each string which consists entirely of digit characters is determined. 
 * The numerical values of these two parts are compared, and any difference found is returned as the result 
 * of the comparison. For these purposes an empty string (which can only occur at the end of one or both version 
 * strings being compared) counts as zero.
 * 
 * These two steps (comparing and removing initial non-digit strings and initial digit strings) are repeated 
 * until a difference is found or both strings are exhausted.
 *
 * [End excerpt]
 * 
 * I have just removed the bit about ~ (tilde) because we don't need it.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class VersionComparator implements Comparator<String>{

	public static final VersionComparator INSTANCE = new VersionComparator();

    @Override
	public int compare(String a, String b) {
		return compareVersions(a, b);
	}

	public static int compareVersions(String versionAString, String versionBString){
        char[] versionA = versionAString.toCharArray();
        char[] versionB = versionBString.toCharArray();
        int aStart = 0, aEnd = 0;
        int bStart = 0, bEnd = 0;
        // we follow the debian algo of sorting first all non-digits, then all digits in turn
        while(true){
            // collect all chars until digits or end
            while(aEnd < versionA.length && !Character.isDigit(versionA[aEnd]))
                aEnd++;
            while(bEnd < versionB.length && !Character.isDigit(versionB[bEnd]))
                bEnd++;
            int compare = compare(versionA, aStart, aEnd, versionB, bStart, bEnd);
            if(compare != 0)
                return compare;
            // if we've exhausted one, it wins
            if(aEnd == versionA.length && bEnd == versionB.length)
                return 0;
            if(aEnd == versionA.length)
                return -1;
            if(bEnd == versionB.length)
                return 1;
            // now collect all digits until non-digit or end
            int a = 0, b = 0;
            while(aEnd < versionA.length && Character.isDigit(versionA[aEnd])){
                a = a * 10 + (versionA[aEnd] - '0');
                aEnd++;
            }
            while(bEnd < versionB.length && Character.isDigit(versionB[bEnd])){
                b = b * 10 + (versionB[bEnd] - '0');
                bEnd++;
            }
            // now compare
            compare = Integer.compare(a, b);
            if(compare != 0)
                return compare;
            // if we've exhausted one, it wins
            if(aEnd == versionA.length && bEnd == versionB.length)
                return 0;
            if(aEnd == versionA.length)
                return -1;
            if(bEnd == versionB.length)
                return 1;
            // and on to the next part
        }
    }
    
    private static int compare(char[] a, int aStart, int aEnd, char[] b, int bStart, int bEnd) {
        for (; aStart < aEnd && bStart < bEnd; aStart++, bStart++) {
            char aChar = a[aStart];
            char bChar = b[bStart];
            if(Character.isAlphabetic(aChar)){
                if(Character.isAlphabetic(bChar)){
                    int ret = Character.compare(aChar, bChar);
                    if(ret != 0)
                        return ret;
                }else{
                    // alphabetic wins
                    return -1;
                }
            }else if(Character.isAlphabetic(bChar)){
                // alphabetic wins
                return 1;
            }else{
                // both non-alphabetic
                int ret = Character.compare(aChar, bChar);
                if(ret != 0)
                    return ret;
            }
            // equal, try the next char
        }
        // shortest one wins
        if(aStart == aEnd && bStart == bEnd)
            return 0;
        if(aStart == aEnd)
            return -1;
        return 1;
    }

    /**
     * Returns versionA and versionB ordered
     */
    public static String[] orderVersions(String versionA, String versionB) {
        if(VersionComparator.compareVersions(versionA, versionB) > 0){
            String permute = versionA;
            versionA = versionB;
            versionB = permute;
        }
        return new String[]{ versionA, versionB };
    }
}
