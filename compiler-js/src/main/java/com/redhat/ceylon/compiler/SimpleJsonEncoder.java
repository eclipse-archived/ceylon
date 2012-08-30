package com.redhat.ceylon.compiler;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/** A dead simple JSON encoder. Encoders maps into objects; knows how to encode lists and maps,
 * and turns anything else into a properly encoded string.
 * The top-level object must always be a Map.
 * 
 * @author Enrique Zamudio
 */
public class SimpleJsonEncoder {

    public void encode(Map<String, Object> map, Writer out) throws IOException {
        encodeMap(map, out);
    }

    public void encodeString(String s, Writer out) throws IOException {
        out.write('"');
        out.write(s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"").replaceAll("\n", "\\\\n"));
        out.write('"');
    }
    public void encodeNumber(Number n, Writer out) throws IOException {
        out.write(n.toString());
    }

    @SuppressWarnings("unchecked")
    public void encodeList(List<Object> list, Writer out) throws IOException {
        out.write('[');
        boolean first = true;
        for (Object item : list) {
            if (!first) {
                out.write(',');
            }
            if (item instanceof List) {
                encodeList((List<Object>)item, out);
            } else if (item instanceof Map) {
                encodeMap((Map<String, Object>)item, out);
            } else if (item instanceof Number) {
                encodeNumber((Number)item, out);
            } else {
                encodeString(item.toString(), out);
            }
            first=false;
        }
        out.write(']');
    }

    @SuppressWarnings("unchecked")
    public void encodeMap(Map<String, Object> map, Writer out) throws IOException {
        out.write('{');
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                out.write(',');
            }
            encodeString(entry.getKey(), out);
            out.write(':');
            if (entry.getValue() instanceof List) {
                encodeList((List<Object>)entry.getValue(), out);
            } else if (entry.getValue() instanceof Map) {
                encodeMap((Map<String, Object>)entry.getValue(), out);
            } else if (entry.getValue() instanceof Number) {
                encodeNumber((Number)entry.getValue(), out);
            } else {
                encodeString(entry.getValue().toString(), out);
            }
            first = false;
        }
        out.write('}');
    }

}
