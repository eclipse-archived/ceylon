package com.redhat.ceylon.cmr.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.redhat.ceylon.common.tools.ModuleSpec;

public class Distribution {

    private Map<String, List<ModuleSpec>> byVersion;

    private Distribution(LinkedHashMap<String, List<ModuleSpec>> byVersion) {
        this.byVersion = byVersion;
    }
    
    private static class Handler extends DefaultHandler {
        LinkedHashMap<String, List<ModuleSpec>> byVersion;
        ArrayList<ModuleSpec> currentList = null;
        String currentVersion = null;

        @Override
        public void startDocument() throws SAXException {
            byVersion = new LinkedHashMap<String, List<ModuleSpec>>();
        }

        @Override
        public void endDocument() throws SAXException {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            String name = qName;
            if ("dist".equals(name)) {
                currentVersion = atts.getValue("version");
                currentList = new ArrayList<>();
                byVersion.put(currentVersion, currentList);
            } else if ("module".equals(name)) {
                String modName = atts.getValue("name");
                // Currently all dist modules have the same version number as
                // the dist itself.
                // When that's no longer true, add support for <module name=... version=.../>
                ModuleSpec modSpec = new ModuleSpec(modName, currentVersion);
                currentList.add(modSpec);
            } else if ("dist-versions".equals(name)) {
                // root element -- ignore
            } else {
                throw new RuntimeException();
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            String name = qName;
            if ("dist".equals(name)) {
                currentList = null;
            } else if ("module".equals(name)) {
                
            } else if ("dist-versions".equals(name)) {
                // root element -- ignore
            } 
        }
        
    }
    
    public static Distribution getDistribution() {
        InputStream stream = Distribution.class.getResourceAsStream("/com/redhat/ceylon/cmr/api/dist-versions.xml");
        if (stream == null) {
            throw new RuntimeException("dist-versions.xml not found");
        }
        try {
            try (InputStreamReader reader = new InputStreamReader(stream, "UTF-8")) {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser p = spf.newSAXParser();
                Handler handler = new Handler();
                p.parse(stream, handler);
                return new Distribution(handler.byVersion);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Get the available versions
     */
    public Collection<String> getDistVersions() {
        return byVersion.keySet();
    }
    
    /** 
     * Return the modules which comprise the Ceylon distribution 
     * for a given version of ceylon
     */
    public List<ModuleSpec> getDistModules(String distVersion) {
        return byVersion.get(distVersion);
    }
    
    /**
     * Get the module spec for the dist module with the given name for a given 
     * version of the dist.
     * @param distVersion
     * @param moduleName
     * @return
     */
    public ModuleSpec getDistModule(String distVersion, String moduleName) {
        List<ModuleSpec> mods = byVersion.get(distVersion);
        if (mods != null) {
            for (ModuleSpec s : mods) {
                if (moduleName.equals(s.getName())) {
                    return s;
                }
            }
        }
        return null;
    }
}
