package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import junit.framework.AssertionFailedError;

import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

/** Tests to make sure the doc visitor retrieves the correct docs.
 * 
 * @author Enrique Zamudio
 */
public class TestDocVisitor {

    final static String path = "src/test/resources/doc/calls.ceylon";
    final static List<String> locations = Arrays.asList(
        "1:0-1:6",     //Boolean
        "1:15-1:21",   //Integer
        "4:0-4:3",     //void
        "4:11-4:16",   //String
        "4:17-4:17",   //Nothing
        "6:4-6:8",     //print
        "9:0-9:5",     //String
        "9:16-9:22",   //Integer
        "9:24-9:29",   //String
        "10:9-10:15",  //Integer
        "11:4-11:11",  //variable
        "11:13-11:19", //Integer
        "13:10-13:16", //smaller
        "14:10-14:15", //larger
        "16:22-16:27", //.string
        "17:16-17:21", //String
        "18:13-18:22", //.uppercased
        "20:9-20:17",  //className
        "22:0-22:5",   //String
        "22:16-22:22", //Integer
        "22:24-22:29", //String
        "24:11-24:17", //Integer
        "25:20-25:25", //.string
        "27:11-27:16", //String
        "28:13-28:22", //.uppercased
        "31:0-31:3",   //void
        "32:2-32:18",  //Sequence<Integer>
        "32:11-32:17", //Integer
        "33:2-33:15",  //Range<Integer>
        "33:8-33:14",  //Integer
        "34:2-34:22",  //Entry<Integer,String>
        "34:8-34:14",  //Integer
        "34:16-34:21", //String
        "35:12-35:14", //Integer
        "36:4-36:8"    //print
    );
    final static DocVisitor doccer = new DocVisitor();

    @BeforeClass
    public static void init() {
        TypeChecker tc = new TypeCheckerBuilder().verbose(false).addSrcDirectory(new File(path)).getTypeChecker();
        tc.process();
        for (PhasedUnit pu : tc.getPhasedUnits().getPhasedUnits()) {
            pu.getCompilationUnit().visit(doccer);
        }
    }

    private void testSameDocs(String name, String... locs) {
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (String loc : locs) {
            if (doccer.getLocations().containsKey(loc)) {
                set.add(doccer.getLocations().get(loc));
            } else {
                throw new AssertionFailedError("Invalid key " + loc);
            }
        }
        if (set.size() != 1) {
            throw new AssertionFailedError(String.format("'%s' points to different docs", name));
        }
    }

    @Test
    public void testLocations() {
        Map<String, Integer> locs = doccer.getLocations();
        System.out.println("Locations: " + locs);
        //Check we have all locations
        for (String loc : locations) {
            if (!locs.containsKey(loc)) {
                throw new AssertionFailedError("No doc for location " + loc);
            }
        }
        //Now check locations we don't have
        for (Map.Entry<String, Integer> dloc : locs.entrySet()) {
            if (!locations.contains(dloc.getKey())) {
                System.out.printf("Unexpected location %s: %d (%s)%n", dloc.getKey(), dloc.getValue(),
                        doccer.getDocs().get(dloc.getValue()));
            }
        }
    }

    @Test
    public void testConsistency() {
        testSameDocs("Integer", "1:15-1:21", "9:16-9:22", "10:9-10:15", "11:13-11:19", "22:16-22:22",
                "24:11-24:17", "32:11-32:17", "33:8-33:14", "34:8-34:14", "35:12-35:14");
        testSameDocs("String", "4:11-4:16", "9:0-9:5", "9:24-9:29", "17:16-17:21", "22:0-22:5",
                "22:24-22:29", "27:11-27:16", "34:16-34:21");
        testSameDocs("print", "6:4-6:8", "36:4-36:8");
        testSameDocs(".string", "16:22-16:27", "25:20-25:25");
        testSameDocs(".uppercased", "18:13-18:22", "28:13-28:22");
    }

}
