package util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.redhat.ceylon.compiler.js.loader.MetamodelGenerator;

public class ModelUtils {

    /** Asserts that every specified key is in the map and that it equals the corresponding value. */
    public static void checkMap(Map<String, Object> map, Object... keysValues) {
        for (int i = 0; i < keysValues.length; i+=2) {
            Assert.assertEquals(keysValues[i+1], map.get(keysValues[i]));
        }
    }

    /** Asserts that a method contains the expected parameter at the expected position with optional default value.
     * @return The type map of the parameter. */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> checkParam(Map<String, Object> method, int pos, String name, String type,
            boolean defaulted, boolean sequenced) {
        List<?> plists = (List<?>)method.get(MetamodelGenerator.KEY_PARAMS);
        List<Map<String, Object>> params;
        if (plists.get(0) instanceof List) {
            params = ((List<List<Map<String, Object>>>)plists).get(0);
        } else {
            params = (List<Map<String, Object>>)plists;
        }
        Assert.assertNotNull(params);
        Assert.assertTrue(params.get(0).size() > pos);
        Map<String, Object> parm = params.get(pos);
        checkMap(parm, MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_PARAMETER,
                MetamodelGenerator.KEY_NAME, name);
        if (defaulted) {
            Assert.assertNotNull(String.format("Param %s of method %s should be defaulted",
                    name, method.get(MetamodelGenerator.KEY_NAME)), parm.get(MetamodelGenerator.KEY_DEFAULT));
        } else {
            Assert.assertNull(String.format("Default value of param %s of method %s",
                    name, method.get(MetamodelGenerator.KEY_NAME)), parm.get(MetamodelGenerator.KEY_DEFAULT));
        }
        Map<String, Object> tmap;
        if (parm.get(MetamodelGenerator.KEY_TYPE) instanceof Map) {
            tmap = (Map<String, Object>)parm.get(MetamodelGenerator.KEY_TYPE);
            Assert.assertNotNull(tmap);
        } else {
            tmap = new HashMap<String, Object>();
            tmap.put(MetamodelGenerator.KEY_NAME, parm.get(MetamodelGenerator.KEY_TYPE));
        }
        if (sequenced) {
            Assert.assertEquals(String.format("Param %s of method %s is not sequenced",
                    name, method.get(MetamodelGenerator.KEY_NAME)),
                    1, parm.get("seq"));
            Assert.assertEquals("Sequenced parameter should be last", params.size()-1, pos);
            Assert.assertEquals("ceylon.language::Sequential", String.format("%s::%s", packageName(tmap),
                    tmap.get(MetamodelGenerator.KEY_NAME)));
            List<Map<String, Object>> pts = (List<Map<String, Object>>)tmap.get(MetamodelGenerator.KEY_TYPE_PARAMS);
            checkTypeParameters(0, pts, type);
        } else {
            Assert.assertNull("Param " + name + " of method " + method.get(MetamodelGenerator.KEY_NAME) + " should not be sequenced",
                    parm.get("seq"));
            checkType(parm, type);
        }
        return tmap;
    }

    /** Check that the map either contains the specified type or is the specified type.
     * The type name can be parameterized, i.e. Sequence&lt;String&gt; (but with fully qualified names);
     * when it's parameterized, the type parameters are checked as well. */
    @SuppressWarnings("unchecked")
    public static void checkType(Map<String, Object> map, String name) {
        Map<String, Object> tmap;
        if (map.get(MetamodelGenerator.KEY_TYPE) instanceof Map) {
            tmap = (Map<String, Object>)map.get(MetamodelGenerator.KEY_TYPE);
        } else if (map.get(MetamodelGenerator.KEY_TYPE) instanceof String) {
            tmap = new HashMap<String, Object>();
            tmap.put(MetamodelGenerator.KEY_NAME, map.get(MetamodelGenerator.KEY_TYPE));
        } else {
            tmap = map;
        }
        int join = name.indexOf('&');
        if (join > 0) {
            while (join > 0 && !pointyBracketsEven(name.substring(0, join))) {
                join = name.indexOf('&', join+1);
            }
            if (join > 0) {
                Assert.assertEquals("not an intersection type", "i", tmap.get("comp"));
                checkTypeParameters(-1, (List<Map<String,Object>>)tmap.get(MetamodelGenerator.KEY_TYPES), name.substring(0, join));
                checkTypeParameters(-1, (List<Map<String,Object>>)tmap.get(MetamodelGenerator.KEY_TYPES), name.substring(join+1));
                return;
            }
        }
        join = name.indexOf('|');
        if (join > 0) {
            while (join > 0 && !pointyBracketsEven(name.substring(0, join))) {
                join = name.indexOf('|', join+1);
            }
            if (join > 0) {
                Assert.assertEquals("not a union type", "u", tmap.get("comp"));
                checkTypeParameters(-1, (List<Map<String,Object>>)tmap.get(MetamodelGenerator.KEY_TYPES), name.substring(0, join));
                checkTypeParameters(-1, (List<Map<String,Object>>)tmap.get(MetamodelGenerator.KEY_TYPES), name.substring(join+1));
                return;
            }
        }
        int sep = name.indexOf('<');
        String typeParams = null;
        if (sep > 0) {
            typeParams = name.substring(sep+1, name.length()-1);
            name = name.substring(0, sep);
        }
        if (tmap.containsKey(MetamodelGenerator.KEY_PACKAGE)) {
            Assert.assertEquals(name, String.format("%s::%s", packageName(tmap),
                    tmap.get(MetamodelGenerator.KEY_NAME)));
        } else {
            Assert.assertEquals(name, tmap.get(MetamodelGenerator.KEY_NAME));
        }
        if (typeParams != null) {
            List<Map<String, Object>> tparms = (List<Map<String, Object>>)tmap.get(MetamodelGenerator.KEY_TYPE_PARAMS);
            Assert.assertFalse("Type parameters shouldn't be empty", tparms.isEmpty());
            checkTypeParameters(0, tparms, typeParams);
        }
    }

    public static void checkTypeParameters(int pos, List<Map<String, Object>> map, String name) {
        int comma = name.indexOf(',');
        if (comma > 0) {
            while (comma > 0 && !pointyBracketsEven(name.substring(0, comma))) {
                comma = name.indexOf(',', comma+1);
            }
            if (comma > 0) {
                String left = name.substring(0, comma);
                checkTypeParameters(pos, map, left);
                left = name.substring(comma+1);
                checkTypeParameters(pos+1, map, left);
                return;
            }
            
        }
        int lt = name.indexOf('<');
        if (lt > 0) {
            //Type with parameters
            if (pos >= 0) {
                Map<String, Object> tp = map.get(pos);
                checkType(tp, name);
            } else {
                String plain = name.substring(0, lt);
                for (Map<String, Object> tp : map) {
                    if (plain.equals(String.format("%s::%s", packageName(tp),
                            tp.get(MetamodelGenerator.KEY_NAME)))) {
                        checkType(tp, name);
                        return;
                    }
                }
                Assert.assertTrue("Missing parameter type " + name, false);
            }
        } else {
            //Simple type
            if (pos >= 0) {
                Map<String, Object> tp = map.get(pos);
                checkType(tp, name);
            } else {
                for (Map<String, Object> tp : map) {
                    String expectedName = tp.containsKey(MetamodelGenerator.KEY_PACKAGE) ?
                            String.format("%s::%s", packageName(tp),
                                    tp.get(MetamodelGenerator.KEY_NAME)) : (String)tp.get(MetamodelGenerator.KEY_NAME);
                    if (name.equals(expectedName)) {
                        checkType(tp,name);
                        return;
                    }
                }
                Assert.assertTrue("Missing parameter type " + name, false);
            }
        }
    }

    public static void checkAnnotations(Map<String, Object> decl, String... names) {
        if (names != null && names.length > 0) {
            final int pa = decl.containsKey(MetamodelGenerator.KEY_PACKED_ANNS) ?
                    (int)decl.get(MetamodelGenerator.KEY_PACKED_ANNS) : 0;
            ArrayList<String> anames = new ArrayList<>(names.length);
            anames.addAll(Arrays.asList(names));
            for (String pan : names) {
                int bit = MetamodelGenerator.annotationBits.indexOf(pan);
                if (bit>=0) {
                    Assert.assertTrue("Missing packed annotation " + pan, (pa&(1<<bit)) > 0);
                    anames.remove(pan);
                }
            }
            @SuppressWarnings("unchecked")
            Map<String,List<String>> anns = (Map<String,List<String>>)decl.get(MetamodelGenerator.KEY_ANNOTATIONS);
            if (!anames.isEmpty()) {
                Assert.assertNotNull("Missing annotations from " + decl, anns);
                for (String n : anames) {
                    Assert.assertNotNull("Missing annotation '" + n + "' from " + decl, anns.get(n));
                }
            }
        }
    }

    private static boolean pointyBracketsEven(String s) {
        int open = 0, close = 0;
        for (char c : s.toCharArray()) {
            if (c=='<') open++;
            else if (c=='>') close++;
        }
        return open == close;
    }

    static String packageName(Map<String,Object> map) {
        String p = (String)map.get(MetamodelGenerator.KEY_PACKAGE);
        if ("$".equals(p)) {
            return "ceylon.language";
        }
        return p;
    }

    @SuppressWarnings("unchecked")
    public static Map<String,Object> getPrivateMethod(String name, Map<String,Object> container) {
        Map<String,Object> methods = (Map<String,Object>)container.get(MetamodelGenerator.KEY_METHODS);
        for (String n : methods.keySet()) {
            if (n.startsWith(name)) {
                return (Map<String,Object>)methods.get(n);
            }
        }
        return null;
    }

    public static void deleteRecursively(File f) {
        if (f.isDirectory()) {
            for (File sub : f.listFiles()) {
                deleteRecursively(sub);
            }
        }
        f.delete();
    }

}
