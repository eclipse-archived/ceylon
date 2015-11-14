package com.redhat.ceylon.compiler.js.loader;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;

public class ModelEncoder {

    public static void encodeModel(Map<String,Object> model, Writer writer) throws IOException {
        final JSONStyle style = new JSONStyle() {
            @Override
            public boolean mustProtectKey(String s) {
                return s.isEmpty() || s.indexOf('-') >= 0 || s.indexOf('.') >= 0;
            }
        };
        JSONObject.writeJSON(model, writer, style);
    }

    public static void encodeDocs(List<String> docs, Writer writer) throws IOException {
        final JSONStyle style = new JSONStyle() {
            @Override
            public boolean mustProtectKey(String s) {
                return s.isEmpty() || s.indexOf('-') >= 0 || s.indexOf('.') >= 0;
            }
        };
        JSONArray.writeJSONString(docs, writer, style);
    }

}
