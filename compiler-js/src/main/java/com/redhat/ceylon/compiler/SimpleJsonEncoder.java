package com.redhat.ceylon.compiler;

import java.util.List;
import java.util.Map;

/** A dead simple JSON encoder. Encoders maps into objects; knows how to encode lists and maps,
 * and turns anything else into a properly encoded string.
 * The top-level object must always be a Map.
 * 
 * @author Enrique Zamudio
 */
public class SimpleJsonEncoder {

    public String encode(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        encodeMap(map, sb);
        return sb.toString();
    }

    public void encodeString(String s, StringBuilder sb) {
        sb.append('"');
        sb.append(s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"").replaceAll("\n", "\\\\n"));
        sb.append('"');
    }
    public void encodeNumber(Number n, StringBuilder sb) {
        sb.append(n.toString());
    }

    @SuppressWarnings("unchecked")
    public void encodeList(List<Object> list, StringBuilder sb) {
        sb.append('[');
        boolean first = true;
        for (Object item : list) {
            if (!first) {
                sb.append(',');
            }
            if (item instanceof List) {
                encodeList((List<Object>)item, sb);
            } else if (item instanceof Map) {
                encodeMap((Map<String, Object>)item, sb);
            } else if (item instanceof Number) {
                encodeNumber((Number)item, sb);
            } else {
                encodeString(item.toString(), sb);
            }
            first=false;
        }
        sb.append(']');
    }

    @SuppressWarnings("unchecked")
    public void encodeMap(Map<String, Object> map, StringBuilder sb) {
        sb.append('{');
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(',');
            }
            encodeString(entry.getKey(), sb);
            sb.append(':');
            if (entry.getValue() instanceof List) {
                encodeList((List<Object>)entry.getValue(), sb);
            } else if (entry.getValue() instanceof Map) {
                encodeMap((Map<String, Object>)entry.getValue(), sb);
            } else if (entry.getValue() instanceof Number) {
                encodeNumber((Number)entry.getValue(), sb);
            } else {
                encodeString(entry.getValue().toString(), sb);
            }
            first = false;
        }
        sb.append('}');
    }

}
