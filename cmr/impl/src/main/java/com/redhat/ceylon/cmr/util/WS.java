package com.redhat.ceylon.cmr.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class WS {
    
    public static class Parser {

        private XMLStreamReader reader;

        public Parser(XMLStreamReader reader) {
            this.reader = reader;
        }

        public boolean moveToOptionalOpenTag(String name){
            nextTag();
            return isOpenTag(name);
        }
        
        public boolean moveToOptionalOpenTag() {
            nextTag();
            return isOpenTag();
        }
        
        public boolean isOpenTag() {
            int code = reader.getEventType();
            return code == XMLStreamReader.START_ELEMENT;
        }
        
        public boolean isOpenTag(String name) {
            return (isOpenTag()
                        && tagName().equals(name));
        }

        public void moveToOpenTag(String name) {
            nextTag();
            if(!isOpenTag(name))
                throw new RuntimeException("Expected open tag "+name+" but got "+reader.getEventType());
        }

        public void checkCloseTag() {
            int code = reader.getEventType();
            if(code != XMLStreamReader.END_ELEMENT)
                throw new RuntimeException("Expected close tag but got "+code);
        }
        
        public void nextTag(){
            try {
                reader.nextTag();
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }

        public String contents() {
            try {
                String contents = reader.getElementText();
                return contents;
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }

        public String tagName() {
            return reader.getName().getLocalPart();
        }
    }

    public interface XMLHandler {
        public void onOK(Parser parser);
    }
    
    public static void getXML(String url, XMLHandler handler){
        getXML(url, null, handler);
    }
    
    public static void getXML(String url, Param[] params, XMLHandler handler){
        try{
            if(params != null)
                url += toQueryString(params);
            URL endpoint = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
            connection.addRequestProperty("Accept", "application/xml");
            connection.connect();
            try{
                if(connection.getResponseCode() == 200){
                    InputStream is = connection.getInputStream();
                    try{
                        XMLInputFactory factory = XMLInputFactory.newFactory();
                        XMLStreamReader reader = factory.createXMLStreamReader(is);
                        try{
                            Parser p = new Parser(reader);
                            handler.onOK(p);
                        }finally{
                            reader.close();
                        }
                    }finally{
                        is.close();
                    }
                }
            }finally{
                connection.disconnect();
            }
        }catch(XMLStreamException x){
            throw new RuntimeException(x);
        }catch(IOException x){
            throw new RuntimeException(x);
        }
    }

    public static List<Link> collectLinks(HttpURLConnection con) {
        List<String> linkHeaders = con.getHeaderFields().get("Link");
        List<Link> ret = new LinkedList<Link>();
        for(String linkHeader : linkHeaders){
            // split it at ","
            String[] links = linkHeader.split(",");
            for(String link : links){
                ret.add(parseLink(link));
            }
        }
        return ret;
    }

    public static String getLink(List<Link> links, String rel){
        for (Link link : links) {
            if(rel.equals(link.params.get("rel")))
                return link.url;
        }
        return null;
    }
    
    public static class Link {
        public String url;
        public Map<String, String> params = new HashMap<String, String>();

        public Link(String url) {
            this.url = url;
        }
    }
    
    private static Link parseLink(String link) {
        // <url> (; name = val)*
        String[] parts = link.split(";");
        // first part will be the url
        String url = parts[0].trim();
        if(!url.startsWith("<")
                || !url.endsWith(">"))
            throw new RuntimeException("Invalid link header: "+link);
        url = url.substring(1, url.length()-1);
        if(url.isEmpty())
            throw new RuntimeException("Invalid link header: "+link);
        
        Link ret = new Link(url);
        for(int i=1;i<parts.length;i++){
            // split at =
            String part = parts[i];
            int eq = part.indexOf('=');
            if(eq == -1)
                throw new RuntimeException("Invalid link header: "+link);
            String name = part.substring(0, eq).trim();
            String val = part.substring(eq+1).trim();
            if(name.isEmpty() || val.isEmpty())
                throw new RuntimeException("Invalid link header: "+link);
            
            if(val.startsWith("\"")){
                if(!val.endsWith("\""))
                    throw new RuntimeException("Invalid link header: "+link);
                // no trimming: it's quoted
                val = val.substring(1, val.length()-1);
                if(val.isEmpty())
                    throw new RuntimeException("Invalid link header: "+link);
            }
            ret.params.put(name, val);
        }
        return ret;
    }

    public static class Param{
        public String name;
        public String[] values;
        
        public Param(String name, String[] values) {
            this.name = name;
            this.values = values;
        }

        public void toString(StringBuilder b) {
            if(values.length == 1){
                b.append(encodeURLQueryParam(name)).append("=").append(encodeURLQueryParam(values[0]));
                return;
            }
            if(values.length == 0){
                b.append(encodeURLQueryParam(name)).append("=");
            }
            for (int i = 0; i < values.length; i++) {
                if(i != 0)
                    b.append("&");
                b.append(encodeURLQueryParam(name)).append("=").append(encodeURLQueryParam(values[i]));
            }
        }
    }
    
    public static Param param(String name, String... values) {
        return new Param(name, values);
    }

    private static String encodeURLQueryParam(String name) {
        // this encoding is good enough for query param parts
        try {
            return URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // can't happen
            throw new RuntimeException(e);
        }
    }

    private static String toQueryString(Param[] params){
        StringBuilder b = new StringBuilder("?");
        for (int i = 0; i < params.length; i++) {
            if(i != 0)
                b.append("&");
            Param param = params[i];
            param.toString(b);
        }
        return b.toString();
    }
    
    public static Param[] params(Param... params) {
        return params;
    }
}
