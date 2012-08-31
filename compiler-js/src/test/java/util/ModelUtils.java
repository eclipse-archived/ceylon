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
            List<Map<String, Object>> pts = (List<Map<String, Object>>)tmap.get("paramtypes");
            Assert.assertNotNull("Sequenced param of " + method.get("name") + " should have parameter type", tmap);
            Assert.assertFalse(tmap.isEmpty());
            tmap = pts.get(0);
            Assert.assertEquals("typeparam", tmap.get("mt"));
            Assert.assertEquals("Wrong param type for sequenced param of " + method.get("name"),
                    type, String.format("%s.%s", tmap.get("pkg"), tmap.get("name")));
        } else {
            Assert.assertNull("Param " + name + " of method " + method.get("name") + " should not be sequenced",
                    parm.get("seq"));
            Assert.assertEquals("Wrong type for param " + name + " of method " + method.get("name"),
                    type, String.format("%s.%s", tmap.get("pkg"), tmap.get("name")));
        }
        return tmap;
    }

    /** Check that the map either contains the specified type or is the specified type. */
    @SuppressWarnings("unchecked")
    public static void checkType(Map<String, Object> map, String name, Object parameterTypes) {
        Map<String, Object> tmap = (Map<String, Object>)map.get("type");
        if (tmap == null) {
            tmap = map;
        }
        Assert.assertEquals(name, String.format("%s.%s", tmap.get("pkg"), tmap.get("name")));
    }

}
