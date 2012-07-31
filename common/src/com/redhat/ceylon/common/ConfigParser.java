package com.redhat.ceylon.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PushbackReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;

public class ConfigParser {
    private File configFile;
    private File currentDir;
    private CeylonConfig config;
    private InputStream in;
    private LineNumberReader counterdr;
    private PushbackReader reader;
    private String section;
    
    public static final String PROP_CEYLON_CONFIG_FILE = "ceylon.config";
    
    private enum Token { section, option, assign, comment, eol, error, eof };
    
    public static CeylonConfig loadDefaultConfig() throws IOException {
        File configFile;
        String configFilename = System.getProperty(PROP_CEYLON_CONFIG_FILE);
        if (configFilename != null) {
            configFile = new File(configFilename);
        } else {
            configFile = new File(new File(System.getProperty("user.home"), ".ceylon"), "config");
        }
        return (new ConfigParser(configFile)).parse();
    }
    
    public static CeylonConfig loadLocalConfig(File dir) throws IOException {
        File userConfig1 = (new File(CeylonConfig.getDefaultUserDir(), "config")).getCanonicalFile().getAbsoluteFile();
        File userConfig2 = (new File(CeylonConfig.getUserDir(), "config")).getCanonicalFile().getAbsoluteFile();
        dir = dir.getCanonicalFile().getAbsoluteFile();
        while (dir != null) {
            File configFile = new File(dir, ".ceylon/config");
            if (configFile.equals(userConfig1) || configFile.equals(userConfig2)) {
                // We stop if we reach $HOME/.ceylon/config or whatever is defined by -Dceylon.config
                break;
            }
            if (configFile.isFile()) {
                return (new ConfigParser(configFile)).parse();
            }
            dir = dir.getParentFile();
        }
        // We didn't find any local config file, just return an empty CeylonConfig
        return new CeylonConfig();
    }
    
    public static CeylonConfig loadConfigFromFile(File configFile) throws IOException {
        return (new ConfigParser(configFile)).parse();
    }
    
    public static CeylonConfig loadConfigFromStream(InputStream stream, File currentDir) throws IOException {
        return (new ConfigParser(stream, currentDir)).parse();
    }
    
    private ConfigParser(File configFile) {
        this.configFile = configFile;
        this.currentDir = configFile.getParentFile();
    }
    
    private ConfigParser(InputStream in, File currentDir) {
        this.in = in;
        this.currentDir = currentDir;
    }
    
    private CeylonConfig parse() throws IOException {
        config = new CeylonConfig();
        section = null;
        if (configFile == null || configFile.isFile()) {
            try {
                if (configFile != null) {
                    in = new FileInputStream(configFile);
                }
                counterdr = new LineNumberReader(new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8"))));
                reader = new PushbackReader(counterdr);
                Token tok;
                while ((tok = peekToken()) != Token.eof) {
                    switch (tok) {
                    case section:
                        handleSection();
                        break;
                    case option:
                        if (section != null) {
                            handleOption();
                        } else {
                            throw new InvalidPropertiesFormatException("Option without section in configuration file at line " + (counterdr.getLineNumber() + 1));
                        }
                        break;
                    case comment:
                    case eol:
                        skipToNextLine();
                        continue;
                    default:
                        throw new InvalidPropertiesFormatException("Unexpected token in configuration file at line " + (counterdr.getLineNumber() + 1));
                    }
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } else {
            throw new FileNotFoundException("Couldn't open configuration file");
        }

        return config;
    }

    private void handleSection() throws IOException {
        expect('[');
        section = readName(true).toLowerCase();
        if (!section.matches("[\\p{L}\\p{Nd}]+(\\.[\\p{L}\\p{Nd}]+)*")) {
            throw new InvalidPropertiesFormatException("Invalid section name in configuration file at line " + (counterdr.getLineNumber() + 1));
        }
        skipWhitespace(false);
        if (peek() == '\"') {
            String subSection = readString();
            expect('"');
            section += "." + subSection;
            skipWhitespace(false);
        }
        expect(']');
    }
    
    private void handleOption() throws IOException {
        String option = readName(false);
        String value;
        skipWhitespace(false);
        Token tok = peekToken();
        if (tok == Token.assign) {
            expect('=');
            value = readValue();
        } else if (tok == Token.error) {
            throw new InvalidPropertiesFormatException("Unexpected token in configuration file at line " + (counterdr.getLineNumber() + 1));
        } else {
            value = "true";
        }
        
        // Special "variable" to get the current directory for this config file
        if (value.startsWith("${DIR}")) {
            value = currentDir.getCanonicalPath() + value.substring(6);
        }
        
        String optName = section + "." + option;
        String[] oldval = config.getOptionValues(optName);
        if (oldval == null) {
            config.setOption(optName, value);
        } else {
            String[] newVal = Arrays.copyOf(oldval, oldval.length + 1);
            newVal[oldval.length] = value;
            config.setOptionValues(optName, newVal);
        }
    }

    private String readName(boolean forSection) throws IOException {
        StringBuilder str = new StringBuilder();
        int c;
        while ((c = reader.read()) != -1) {
            if ((!forSection && isOptionNameChar(c)) || (forSection && isSectionNameChar(c))) {
                str.append((char)c);
            } else {
                reader.unread(c);
                break;
            }
        }
        return str.toString();
    }

    private String readString() throws IOException {
        StringBuilder str = new StringBuilder();
        gobble('\"');
        int c;
        while ((c = reader.read()) != -1) {
            if (c == '"') {
                reader.unread(c);
                break;
            } else if (c == '\\') {
                int c2 = reader.read();
                if (c2 == '\\') {
                    // Do nothing
                } else if (c2 == '\"') {
                    c = c2;
                } else {
                    throw new InvalidPropertiesFormatException("Illegal escape character in configuration file at line " + (counterdr.getLineNumber() + 1));
                }
            }
            str.append((char)c);
        }
        return str.toString();
    }

    private String readValue() throws IOException {
        StringBuilder str = new StringBuilder();
        skipWhitespace(false);
        boolean hasQuote = gobble('\"');
        int c;
        while ((c = reader.read()) != -1) {
            if (c == '"') {
                reader.unread(c);
                break;
            } else if (isNewLineChar(c)) {
                break;
            } else if (isCommentChar(c) && !hasQuote) {
                break;
            } else if (c == '\\') {
                int c2 = reader.read();
                if (c2 == '\\') {
                    // Do nothing
                } else if (c2 == '\"') {
                    c = c2;
                } else if (c2 == 't') {
                    c = '\t';
                } else if (c2 == 'n') {
                    c = '\n';
                } else if (isNewLineChar(c2)) {
                    skipNewLine(c2);
                    c = '\n';
                } else {
                    throw new InvalidPropertiesFormatException("Illegal escape character in configuration file at line " + (counterdr.getLineNumber() + 1));
                }
            }
            str.append((char)c);
        }
        if (hasQuote) {
            expect('\"');
            return str.toString();
        } else {
            return str.toString().trim();
        }
    }

    private void expect(int expected) throws IOException {
        int c;
        if ((c = reader.read()) != expected) {
            throw new InvalidPropertiesFormatException("Unexpected token in configuration file at line " + (counterdr.getLineNumber() + 1) + ", expected '" + Character.valueOf((char)expected) + "' but got '" + Character.valueOf((char)c) + "'");
        }
    }
    
    private void skipWhitespace(boolean multiline) throws IOException {
        int c;
        while ((c = reader.read()) != -1) {
            if (!Character.isWhitespace(c) || (!multiline && isNewLineChar(c))) {
                reader.unread(c);
                break;
            }
        }
    }
    
    private void skipToNextLine() throws IOException {
        int c;
        while ((c = reader.read()) != -1) {
            if (isNewLineChar(c)) {
                skipNewLine(c);
                break;
            }
        }
    }

    private Token peekToken() throws IOException {
        skipWhitespace(true);
        int c = peek();
        if (isCommentChar(c)) {
            return Token.comment;
        } else if (c == '[') {
            return Token.section;
        } else if (c == '=') {
            return Token.assign;
        } else if (isNewLineChar(c)) {
            return Token.eol;
        } else if (isOptionNameChar(c)) {
            return Token.option;
        } else if (c == -1) {
            return Token.eof;
        } else {
            return Token.error;
        }
    }
    
    private int peek() throws IOException {
        int c = reader.read();
        reader.unread(c);
        return c;
    }
    
    private boolean gobble(int chr) throws IOException {
        int c = reader.read();
        if (c != chr) {
            reader.unread(c);
            return false;
        }
        return true;
    }
    
    private void skipNewLine(int c) throws IOException {
        if (c == '\r') {
            c = reader.read();
            if (c != '\n') {
                reader.unread(c);
            }
        }
    }
    
    private boolean isOptionNameChar(int c) {
        return Character.isLetterOrDigit(c) || c == '-';
    }
    
    private boolean isSectionNameChar(int c) {
        return isOptionNameChar(c) || c == '.';
    }
    
    private boolean isCommentChar(int c) {
        return c == ';' || c == '#';
    }
    
    private boolean isNewLineChar(int c) {
        return c == '\n' || c == '\r';
    }
}
