package com.redhat.ceylon.cmr.api;

import java.util.Comparator;

/**
 * Version comparator that delegates to MavenVersionComparator 
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class VersionComparator {
    public static Comparator<String> INSTANCE = MavenVersionComparator.INSTANCE;

    public static int compareVersions(String a, String b) {
        return INSTANCE.compare(a, b);
    }

    public static String[] orderVersions(String a, String b) {
        return MavenVersionComparator.orderVersions(a, b);
    }
}
