package com.redhat.ceylon.cmr.api;

import java.util.Comparator;

/**
 * Compares two version strings according to the Maven rules:
 * 
 * https://cwiki.apache.org/confluence/display/MAVENOLD/Versioning
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class MavenVersionComparator implements Comparator<String>{

	public static final MavenVersionComparator INSTANCE = new MavenVersionComparator();

    @Override
	public int compare(String a, String b) {
		return compareVersions(a, b);
	}

	public static int compareVersions(String versionAString, String versionBString){
        char[] versionA = versionAString.toCharArray();
        char[] versionB = versionBString.toCharArray();
        int aStart = 0, aEnd = 0;
        int bStart = 0, bEnd = 0;
        int aLevel = 0, bLevel = 0;
        while(true){
            // if we've exhausted both, we stop
            if(aEnd == versionA.length && bEnd == versionB.length)
                return 0;

            boolean aString = false;
            boolean bString = false;
            boolean aNumber = false;
            boolean bNumber = false;
            long a = 0, b = 0;

            // collect all chars until they change type
            while(aEnd < versionA.length && !isSeparator(versionA[aEnd])){
                char c = versionA[aEnd];
                if(!aString && !aNumber){
                    // see what we're looking for
                    if(Character.isDigit(c)){
                        aNumber = true;
                        a = (versionA[aEnd] - '0');
                    }else
                        aString = true;
                }else if(aNumber){ 
                    if(!Character.isDigit(c))
                        break;
                    a = a * 10 + (versionA[aEnd] - '0');
                }else if(aString && Character.isDigit(c))
                    break;
                // eat it
                aEnd++;
            }
            while(bEnd < versionB.length && !isSeparator(versionB[bEnd])){
                char c = versionB[bEnd];
                if(!bString && !bNumber){
                    // see what we're looking for
                    if(Character.isDigit(c)){
                        bNumber = true;
                        b = (versionB[bEnd] - '0');
                    }else
                        bString = true;
                }else if(bNumber){ 
                    if(!Character.isDigit(c))
                        break;
                    b = b * 10 + (versionB[bEnd] - '0');
                }else if(bString && Character.isDigit(c))
                    break;
                // eat it
                bEnd++;
            }
            int compare = compare(versionA, aStart, aEnd, aNumber, aString, a, aLevel,
                                  versionB, bStart, bEnd, bNumber, bString, b, bLevel);
            if(compare != 0)
                return compare;
            if(aEnd < versionA.length && versionA[aEnd] == '-')
                aLevel++;
            if(bEnd < versionB.length && versionB[bEnd] == '-')
                bLevel++;
            // if not, eat any optional separator (we can be separated by just string/int) and loop
            if(aEnd < versionA.length && isSeparator(versionA[aEnd]))
                aEnd++;
            if(bEnd < versionB.length && isSeparator(versionB[bEnd]))
                bEnd++;
            aStart = aEnd;
            bStart = bEnd;
        }
    }
    
    private static boolean isSeparator(char c) {
        return c == '.' || c == '-';
    }

    private static int compare(char[] a, int aStart, int aEnd, boolean aNumber, boolean aString, long aValue, int aLevel, 
                               char[] b, int bStart, int bEnd, boolean bNumber, boolean bString, long bValue, int bLevel) {
        if(aLevel > bLevel){
            // A is in a list, and B is not
            if(bNumber)
                return -1; // number is newer
            if(bString)
                return 1; // list is newer
            // b is null, compare our element against it
        }else if(aLevel < bLevel){
            // B is in a list, and A is not
            if(aNumber)
                return 1; // number is newer
            if(aString)
                return -1; // list is newer
            // a is null, compare our element against it
        }

        if(aNumber){
            if(bNumber)
                return Long.compare(aValue, bValue);
            if(bString)
                return 1; // number is newer
            // must be empty
            if(aValue == 0)
                return 0; // 0 == ""
            return 1; // number is newer
        }
        if(aString){
            if(bNumber)
                return -1; // number is newer
            // either we have bString, or not and it's empty, which compareStrings handles
            return compareStrings(a, aStart, aEnd, b, bStart, bEnd);
        }
        // a is empty
        if(bNumber){
            if(bValue == 0)
                return 0; // 0 == ""
            return -1; // number is newer
        }
        if(bString){
            // we have bString and compareStrings handles a's empty
            return compareStrings(a, aStart, aEnd, b, bStart, bEnd);
        }
        // both empty
        return 0;
    }

    private static int compareStrings(char[] a, int aStart, int aEnd, char[] b, int bStart, int bEnd) {
        int specialWeightA = getSpecialWeight(a, aStart, aEnd);
        int specialWeightB = getSpecialWeight(b, bStart, bEnd);
        if(specialWeightA >= 0){
            if(specialWeightB >= 0)
                return Integer.compare(specialWeightA, specialWeightB);
            // special weight is newer
            return -1;
        }
        if(specialWeightB >= 0){
            // special weight is newer
            return 1;
        }
        // no special weight, normal comparison
        for (; aStart < aEnd && bStart < bEnd; aStart++, bStart++) {
            char aChar = a[aStart];
            char bChar = b[bStart];
            int ret = Character.compare(aChar, bChar);
            if(ret != 0)
                return ret;
            // equal, try the next char
        }
        // shortest one wins
        if(aStart == aEnd && bStart == bEnd)
            return 0;
        if(aStart == aEnd)
            return -1;
        return 1;
    }

    private static int getSpecialWeight(char[] chars, int start, int end) {
        if(start == end)
            return 5;
        if(equalsIgnoreCase("alpha", chars, start, end)
                || equalsIgnoreCase("a", chars, start, end))
            return 0;
        if(equalsIgnoreCase("beta", chars, start, end)
                || equalsIgnoreCase("b", chars, start, end))
            return 1;
        if(equalsIgnoreCase("milestone", chars, start, end)
                || equalsIgnoreCase("m", chars, start, end))
            return 2;
        if(equalsIgnoreCase("cr", chars, start, end)
                || equalsIgnoreCase("rc", chars, start, end))
            return 3;
        if(equalsIgnoreCase("snapshot", chars, start, end))
            return 4;
        if(equalsIgnoreCase("ga", chars, start, end)
                || equalsIgnoreCase("final", chars, start, end))
            return 5;
        if(equalsIgnoreCase("sp", chars, start, end))
            return 6;
        return -1;
    }

    private static boolean equalsIgnoreCase(String string, char[] chars, int start, int end) {
        // same length?
        if((end - start) != string.length())
            return false;
        // since they have the same length, we have enough i, no need to check
        for (int i=0; start < end ; start++, i++) {
            char aChar = chars[start];
            char bChar = string.charAt(i);
            int ret = Character.compare(Character.toLowerCase(aChar), Character.toLowerCase(bChar));
            if(ret != 0)
                return false;
            // equal, try the next char
        }
        // equal
        return true;
    }

    /**
     * Returns versionA and versionB ordered
     */
    public static String[] orderVersions(String versionA, String versionB) {
        if(MavenVersionComparator.compareVersions(versionA, versionB) > 0){
            String permute = versionA;
            versionA = versionB;
            versionB = permute;
        }
        return new String[]{ versionA, versionB };
    }
}
