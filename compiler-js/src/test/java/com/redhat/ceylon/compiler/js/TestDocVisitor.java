package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import junit.framework.AssertionFailedError;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

/** Tests to make sure the doc visitor retrieves the correct docs.
 * 
 * @author Enrique Zamudio
 */
@RunWith(Parameterized.class)
public class TestDocVisitor {

    private final String file;
    private final Map<String, List<String>> locations;
    private final DocVisitor doccer = new DocVisitor();
    private final TypeChecker tc;

    @Parameters
    public static Collection<Object[]> sources() {
        Map<String, List<String>> m1 = new HashMap<String, List<String>>();
        m1.put("Boolean", Collections.singletonList("1:0-1:6"));
        m1.put("Integer", Arrays.asList("1:15-1:21", "9:16-9:22", "10:9-10:15", "11:13-11:19",
                "22:16-22:22", "24:11-24:17", "32:11-32:17", "33:8-33:14", "34:8-34:14"
        ));
        m1.put("void", Arrays.asList("4:0-4:3", "31:0-31:3"));
        m1.put("String", Arrays.asList("4:11-4:16", "9:0-9:5", "9:24-9:29", "17:16-17:21",
                "22:0-22:5", "22:24-22:29", "27:11-27:16", "34:16-34:21"));
        m1.put("Null", Collections.singletonList("4:17-4:17"));
        m1.put("print", Arrays.asList("6:4-6:8", "36:4-36:8"));
        m1.put("smaller", Collections.singletonList("13:10-13:16"));
        m1.put("larger", Collections.singletonList("14:10-14:15"));
        m1.put(".string", Arrays.asList("16:22-16:27", "25:20-25:25"));
        m1.put(".uppercased", Arrays.asList("18:13-18:22", "28:13-28:22"));
        m1.put("className", Collections.singletonList("20:9-20:17"));
        m1.put("Sequence", Collections.singletonList("32:2-32:18"));
        m1.put("Range", Collections.singletonList("33:2-33:15"));
        m1.put("Entry", Collections.singletonList("34:2-34:22"));

        Map<String, List<String>> m2 = new HashMap<String, List<String>>();
        m2.put("void", Arrays.asList("1:0-1:3", "13:0-13:3"));
        m2.put("Integer", Arrays.asList("1:17-1:23", "4:11-4:17"));
        m2.put("String", Arrays.asList("1:25-1:30", "3:11-3:16"));
        m2.put("print", Arrays.asList("3:21-3:25", "7:21-7:25", "8:20-8:24", "9:11-9:15"));
        m2.put(".uppercased", Arrays.asList("3:29-3:38"));
        m2.put("smaller", Arrays.asList("7:10-7:16"));
        m2.put("larger", Arrays.asList("8:10-8:15"));

        Map<String, List<String>> m3 = new HashMap<String, List<String>>();
        m3.put("String", Arrays.asList("1:0-1:5", "4:12-4:17", "8:11-8:16"));
        m3.put("Integer", Arrays.asList("1:18-1:24", "4:21-4:27", "8:18-8:24", "9:14-9:20"));
        m3.put("void", Arrays.asList("4:0-4:3", "7:0-7:3"));
        //m3.put("Callable", Arrays.asList("8:2-8:9"));
        return Arrays.asList(new Object[][]{
            {"src/test/resources/doc/calls.ceylon", m1 },
            {"src/test/resources/doc/switch.ceylon", m2 },
            {"src/test/resources/doc/highers.ceylon", m3 }
        });
    }

    public TestDocVisitor(String path, Map<String, List<String>> locations) {
        file = path.substring(path.lastIndexOf('/')+1);
        tc = new TypeCheckerBuilder().verbose(false).addSrcDirectory(new File(path)).getTypeChecker();
        tc.process();
        for (PhasedUnit pu : tc.getPhasedUnits().getPhasedUnits()) {
            pu.getCompilationUnit().visit(doccer);
        }
        this.locations = locations;
    }

    private void testSameDocs(String name, List<String> locs) {
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
        //Check we have all locations
        for (Map.Entry<String, List<String>> locEntry : locations.entrySet()) {
            for (String loc : locEntry.getValue()) {
                if (!locs.containsKey(loc)) {
                    throw new AssertionFailedError("No doc for location " + loc + " in " + file);
                }
            }
        }
        //Now check locations we don't have
        for (Map.Entry<String, Integer> dloc : locs.entrySet()) {
            boolean contains = false;
            for (Map.Entry<String, List<String>> e : locations.entrySet()) {
                contains |= e.getValue().contains(dloc.getKey());
            }
            if (!contains) {
                System.out.printf("Unexpected location %s: %d (%s) at %s%n", dloc.getKey(), dloc.getValue(),
                    doccer.getDocs().get(dloc.getValue()), file);
            }
        }
    }

    @Test
    public void testConsistency() {
        for (Map.Entry<String, List<String>> e : locations.entrySet()) {
            testSameDocs(e.getKey(), e.getValue());
        }
    }

}
