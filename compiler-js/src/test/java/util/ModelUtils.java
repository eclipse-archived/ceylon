package util;

import java.util.List;
import java.util.Map;

import org.junit.Assert;

public class ModelUtils {

    /** Asserts that very specified key is in the map and that it equals the corresponding value. */
    public static void checkMap(Map<String, Object> map, String... keysValues) {
        for (int i = 0; i < keysValues.length; i+=2) {
            Assert.assertEquals(keysValues[i+1], (String)map.get(keysValues[i]));
        }
    }

    /** Asserts that a method contains the expected parameter at the expected position with optional default value.
     * @return The type map of the parameter. */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> checkParam(Map<String, Object> method, int pos, String name, String type, String defValue, boolean sequenced) {
        List<Map<String, Object>> params = (List<Map<String, Object>>)method.get("params");
        Assert.assertNotNull(params);
        Assert.assertTrue(params.size() > pos);
        Map<String, Object> parm = params.get(pos);
        checkMap(parm, "mt", "param", "name", name);
        if (defValue == null) {
            Assert.assertNull("Param " + name + " of method " + method.get("name") + " shouldn't have default value",
                    parm.get("def"));
        } else {
            Assert.assertEquals("Default value of param " + name + " of method name " + method.get("name"),
                    defValue, parm.get("def"));
        }
        Map<String, Object> tmap = (Map<String, Object>)parm.get("type");
        Assert.assertNotNull(tmap);
        if (sequenced) {
            Assert.assertEquals("Param " + name + " of method " + method.get("name") + " should be sequenced",
                    "1", parm.get("seq"));
            Assert.assertEquals("Sequenced parameter should be last", params.size()-1, pos);
            Assert.assertEquals("ceylon.language.Iterable", String.format("%s.%s", tmap.get("pkg"), tmap.get("name")));
            List<Map<String, Object>> pts = (List<Map<String, Object>>)tmap.get("tparams");
            checkTypeParameters(pts, type);
        } else {
            Assert.assertNull("Param " + name + " of method " + method.get("name") + " should not be sequenced",
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
        Map<String, Object> tmap = (Map<String, Object>)map.get("type");
        if (tmap == null) {
            tmap = map;
        }
        int sep = name.indexOf('<');
        String typeParams = null;
        if (sep > 0) {
            typeParams = name.substring(sep+1, name.length()-1);
            name = name.substring(0, sep);
        }
        Assert.assertEquals(name, String.format("%s.%s", tmap.get("pkg"), tmap.get("name")));
        if (typeParams != null) {
            List<Map<String, Object>> tparms = (List<Map<String, Object>>)tmap.get("tparams");
            Assert.assertFalse("Type parameters shouldn't be empty", tparms.isEmpty());
            checkTypeParameters(tparms, typeParams);
        }
    }

    @SuppressWarnings("unchecked")
    public static void checkTypeParameters(List<Map<String, Object>> map, String name) {
        int comma = name.indexOf(',');
        if (comma > 0) {
            while (comma > 0 && !pointyBracketsEven(name.substring(0, comma))) {
                comma = name.indexOf(',', comma+1);
            }
            if (comma > 0) {
                String left = name.substring(0, comma);
                checkTypeParameters(map, left);
                left = name.substring(comma+1);
                checkTypeParameters(map, left);
                return;
            }
            
        }
        int lt = name.indexOf('<');
        if (lt > 0) {
            String plain = name.substring(0, lt);
            String parms = name.substring(lt+1, name.length()-1);
            //Type with parameters
            for (Map<String, Object> tp : map) {
                if (plain.equals(String.format("%s.%s", tp.get("pkg"), tp.get("name")))) {
                    checkTypeParameters((List<Map<String, Object>>)tp.get("tparams"), parms);
                    return;
                }
            }
            Assert.assertTrue("Missing parameter type " + name, false);
        } else {
            //Simple type
            for (Map<String, Object> tp : map) {
                if (name.equals(String.format("%s.%s", tp.get("pkg"), tp.get("name")))) {
                    return;
                }
            }
            Assert.assertTrue("Missing parameter type " + name, false);
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
}
