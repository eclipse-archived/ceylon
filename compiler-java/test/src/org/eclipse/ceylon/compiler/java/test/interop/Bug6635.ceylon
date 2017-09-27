import java.util { Arrays { asList }, Map { Entry }, ... }
import java.util.stream { Collectors { ... } }

alias JavaEntry6635 => Entry<String, String>;
alias JavaMapEntry6635 => Map.Entry<String, String>;//runs without this alias

shared void run6635() {
    
    Map<String, String> results = Arrays.asList("a").stream()
            .map((i) => AbstractMap.SimpleEntry<String, String>(i, "b"))
            .collect(Collectors.toMap<JavaEntry6635, String, String>(JavaMapEntry6635.key, JavaMapEntry6635.\ivalue));
    print(results);
}