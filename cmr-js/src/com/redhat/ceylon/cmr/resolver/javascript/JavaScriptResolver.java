package com.redhat.ceylon.cmr.resolver.javascript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import net.minidev.json.JSONValue;

public class JavaScriptResolver {

	// trick to ensure JSONValue is loaded when this class is instantiated, for optional deps to work
	public JavaScriptResolver(){
		JSONValue.escape("foo");
	}
	
	public Map<String,Object> readModel(File jsFile) throws IOException {
		return readJsonModel(jsFile);
	}
	
    /** Find the metamodel declaration in a js file, parse it as a Map and return it. 
     * @throws IOException */
    public static Map<String,Object> readJsonModel(File jsFile) throws IOException {
    	
        // IMPORTANT
        // This method NEEDS to be able to return the meta model of any previous file formats!!!
        // It MUST stay backward compatible
        try (BufferedReader reader = new BufferedReader(new FileReader(jsFile))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if ((line.startsWith("ex$.$CCMM$=")
                        || line.startsWith("var $CCMM$=")
                        || line.startsWith("var $$METAMODEL$$=")
                        || line.startsWith("var $$metamodel$$=")) && line.endsWith("};")) {
                    line = line.substring(line.indexOf("{"), line.length()-1);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> rv = (Map<String,Object>) JSONValue.parse(line);
                    return rv;
                }
            }
            return null;
        }
    }

}
