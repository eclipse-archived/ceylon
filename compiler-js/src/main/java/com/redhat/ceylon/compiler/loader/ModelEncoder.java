package com.redhat.ceylon.compiler.loader;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;

public class ModelEncoder {

    private final Map<String,Object> model;

    public ModelEncoder(Map<String,Object> model) {
        this.model = model;
    }

    public void encode(Writer writer) throws IOException {
        final JSONStyle style = new JSONStyle() {
            @Override
            public boolean mustProtectKey(String s) {
                return s.isEmpty() || s.indexOf('-') >= 0 || s.indexOf('.') >= 0;
            }
        };
        JSONObject.writeJSON(model, writer, style);
    }

}
