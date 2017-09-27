import java.util { JHashMap = HashMap }
import java.lang { JString = String }

@noanno
shared void bug7112() {
    value javaHashMap = JHashMap<JString, Float>();
    javaHashMap.put(JString("hello"), 5.0);
    javaHashMap.forEach((jstr, num) => print(jstr));

    value javaHashMap1 = JHashMap<JString, Float>();

    javaHashMap.entrySet().stream().forEach((entry) => javaHashMap1.put(entry.key, entry.\ivalue));
    javaHashMap1.clear();
    javaHashMap.entrySet().stream().forEach((entry) {
        return javaHashMap1.put(entry.key, entry.\ivalue);
    });
}
