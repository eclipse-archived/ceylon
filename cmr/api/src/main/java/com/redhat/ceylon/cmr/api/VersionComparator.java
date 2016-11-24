package com.redhat.ceylon.cmr.api;

import java.util.Comparator;

import com.redhat.ceylon.common.CeylonVersionComparator;

/**
 * Version comparator that delegates to MavenVersionComparator 
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class VersionComparator {
    public static Comparator<String> INSTANCE = CeylonVersionComparator.INSTANCE;

    public static int compareVersions(String a, String b) {
        return INSTANCE.compare(a, b);
    }

    public static String[] orderVersions(String a, String b) {
        return CeylonVersionComparator.orderVersions(a, b);
    }
}
