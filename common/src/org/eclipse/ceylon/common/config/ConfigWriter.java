/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ceylon.common.FileUtil;

/**
 * Serializes {@link CeylonConfig} instances to streams and files.
 * 
 * All serialization uses the UTF-8 character encoding.
 */
public class ConfigWriter {

    public static ConfigWriter instance() {
        return new ConfigWriter();
    }
    
    /**
     * Write the given configuration to the given file. Updating the existing file if
     * it exists or otherwise creating a new file.
     */
    public void write(CeylonConfig config, File destination) throws IOException {
        OutputStream out = null;
        if (destination.isFile()) {
            write(config, destination, destination);
        } else {
            try {
                // First create any parent directories if necessary
                File parentDir = destination.getAbsoluteFile().getParentFile();
                if (!parentDir.exists()) {
                    FileUtil.mkdirs(parentDir);
                }
                // Now create the file itself
                out = new FileOutputStream(destination);
                write(config, out);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) { }
                }
            }
        }
    }

    /**
     * Reads config from the given source file, updating it using the given 
     * configuration and writing in to the destination file. 
     */
    public void write(CeylonConfig config, File source, File destination) throws IOException {
        boolean overwriteSource = destination.getCanonicalFile().equals(source.getCanonicalFile());
        if (source.isFile()) {
            InputStream in = null;
            OutputStream out = null;
            File tmpFile = null;
            boolean ok = false;
            try {
                try {
                    in = new FileInputStream(source);
                    if (overwriteSource) {
                        // Send the output to a temporary file first
                        tmpFile = File.createTempFile(source.getName(), ".tmp", source.getAbsoluteFile().getParentFile());
                        out = new FileOutputStream(tmpFile);
                    } else {
                        // First create any parent directories if necessary
                        File parentDir = destination.getAbsoluteFile().getParentFile();
                        if (!parentDir.exists()) {
                            FileUtil.mkdirs(parentDir);
                        }
                        // Now create the file itself
                        out = new FileOutputStream(destination);
                    }
                    write(config, in, out);
                    ok = true;
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) { }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) { }
                    }
                }
                if (ok) {
                    File sourceBackup = new File(source.getAbsoluteFile().getParentFile(), source.getName() + "~");
                    Files.deleteIfExists(sourceBackup.toPath());
                    Files.move(source.toPath(), sourceBackup.toPath());
                    Files.move(tmpFile.toPath(), source.toPath());
                }
            } finally {
                if (tmpFile != null) {
                    Files.deleteIfExists(tmpFile.toPath());
                }
            }
        } else {
            throw new FileNotFoundException("Couldn't open source configuration file");
        }
    }

    /**
     * Reads config from the given source file, updating it using the given 
     * configuration and writing in to the given output.
     */
    public void write(CeylonConfig config, File source, OutputStream out) throws IOException {
        if (source.isFile()) {
            InputStream in = null;
            try {
                in = new FileInputStream(source);
                write(config, in, out);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) { }
                }
            }
        } else {
            throw new FileNotFoundException("Couldn't open source configuration file");
        }
    }

    public void write(CeylonConfig config, InputStream in, File destination) throws IOException {
        OutputStream out = null;
        try {
            // First create any parent directories if necessary
            File parentDir = destination.getAbsoluteFile().getParentFile();
            if (!parentDir.exists()) {
                FileUtil.mkdirs(parentDir);
            }
            // Now create the file itself
            out = new FileOutputStream(destination);
            write(config, in, out);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) { }
            }
        }
    }

    /**
     * Reads config from the given input, updating it using the given 
     * configuration and writing in to the given output.
     */
    public void write(CeylonConfig orgconfig, InputStream in, OutputStream out) throws IOException {
        final CeylonConfig config = orgconfig.copy();
        final Writer writer = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("UTF-8")));
        ConfigReader reader = new ConfigReader(in, new ImprovedConfigReaderListenerAdapter(new ImprovedConfigReaderListener() {
            private boolean skipToNewline = false;

            @Override
            public void setup() throws IOException {
                // Ignoring setup
            }
            
            @Override
            public void onSection(String section, String text) throws IOException {
                if (config.isSectionDefined(section)) {
                    writer.write(text);
                    skipToNewline = false;
                } else {
                    skipToNewline = true;
                }
            }

            @Override
            public void onSectionEnd(String section) throws IOException {
                writeOptions(writer, config, section);
            }

            @Override
            public void onOption(String name, String value, String text) throws IOException {
                if (config.isOptionDefined(name)) {
                    String[] newValues = config.getOptionValues(name);
                    if (value.equals(newValues[0])) {
                        // The value hasn't changed, we'll write the option *exactly* as it was
                        writer.write(text);
                    } else {
                        // The value has changed, we will write a new option
                        CeylonConfig.Key k = new CeylonConfig.Key(name);
                        writeOptionValue(writer, k.getOptionName(), newValues[0]);
                    }
                    removeOptionValue(name);
                    skipToNewline = false;
                } else {
                    skipToNewline = true;
                }
            }

            @Override
            public void onComment(String text) throws IOException {
                if (skipToNewline) {
                    skipToNewline = !text.contains("\n");
                } else {
                    writer.write(text);
                }
            }

            @Override
            public void onWhitespace(String text) throws IOException {
                if (skipToNewline) {
                    skipToNewline = !text.contains("\n");
                } else {
                    writer.write(text);
                }
            }

            @Override
            public void cleanup() throws IOException {
                // Ignoring cleanup
            }

            private void removeOptionValue(String name) {
                String[] values = config.getOptionValues(name);
                if (values.length > 1) {
                    values = Arrays.copyOfRange(values, 1, values.length);
                    config.setOptionValues(name, values);
                } else {
                    config.removeOption(name);
                }
            }
            
        }));
        reader.process();
        writer.flush();
        // Now write what's left of the configuration to the output
        writeSections(writer, config, out);
        writer.flush();
    }

    /**
     * Write the given configuration to the given output stream.
     */
    public void write(CeylonConfig orgconfig, OutputStream out) throws IOException {
        final CeylonConfig config = orgconfig.copy();
        final Writer writer = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("UTF-8")));
        writeSections(writer, config, out);
        writer.flush();
    }

    protected void writeSections(Writer writer, CeylonConfig config, OutputStream out) throws IOException {
        String[] sections = config.getSectionNames(null);
        Arrays.sort(sections);
        for (String section : sections) {
            if (config.getOptionNames(section).length > 0) {
                writer.write(System.lineSeparator());
                writeSection(writer, section);
                writeOptions(writer, config, section);
            }
        }
    }

    protected void writeSection(Writer writer, String section) throws IOException {
        String[] names = section.split("\\.");
        if (names.length > 1) {
            StringBuffer pre = new StringBuffer();
            for (int i = 0; i < names.length - 1; i++) {
                if (i > 0) {
                    pre.append(".");
                }
                pre.append(names[i]);
            }
            String post = names[names.length - 1];
            writeCompoundSection(writer, pre.toString(), post);
        } else {
            writeSimpleSection(writer, section);
        }
    }

    protected void writeSimpleSection(Writer writer, String section) throws IOException {
        writer.write("[");
        writer.write(section);
        writer.write("]");
        writer.write(System.lineSeparator());
    }

    protected void writeCompoundSection(Writer writer, String pre, String post) throws IOException {
        writer.write("[");
        writer.write(pre);
        writer.write(" \"");
        writer.write(post);
        writer.write("\"");
        writer.write("]");
        writer.write(System.lineSeparator());
    }

    protected void writeOptions(Writer writer, CeylonConfig config, String section) throws IOException {
        String[] names = config.getOptionNames(section);
        if (names != null) {
            for (int i=0; i < names.length; i++) {
                String name = names[i];
                writeOption(writer, config, section, name);
                config.removeOption(section + "." + name);
                writer.write(System.lineSeparator());
            }
        }
    }
    
    protected void writeOption(Writer writer, CeylonConfig config, String section, String name) throws IOException {
        String[] values = config.getOptionValues(section + "." + name);
        if (values != null) {
            for (int i=0; i < values.length; i++) {
                String value = values[i];
                writeOptionValue(writer, name, value);
                if (i < (values.length - 1)) {
                    writer.write(System.lineSeparator());
                }
            }
        }
    }

    protected void writeOptionValue(Writer writer, String name, String value) throws IOException {
        writer.write(name);
        writer.write("=");
        writer.write(quote(value));
    }

    public static String escape(String value) {
        value = value.replace("\\", "\\\\");
        value = value.replace("\"", "\\\"");
        value = value.replace("\t", "\\t");
        value = value.replace("\n", "\\n");
        return value;
    }

    public static String quote(String value) {
        value = escape(value);
        boolean needsQuotes = value.contains(";") || value.contains("#") || value.endsWith(" ");
        if (needsQuotes) {
            return "\"" + value + "\"";
        } else {
            return value;
        }
    }
}

interface ImprovedConfigReaderListener extends ConfigReaderListener {
    public void onSectionEnd(String section) throws IOException;
}

// This adapter class improves on the standard ConfigReaderListener interface
// by adding an onSectionEnd() event which will be triggered at the end of
// each configuration section. It tries to be smart about this by considering
// whitespace and comments on the last option line to be still part of the
// last section while considering all whitespace and comments before a section
// line to be part of the new section
class ImprovedConfigReaderListenerAdapter implements ConfigReaderListener {
    private ImprovedConfigReaderListener listener;
    
    private String currentSection;
    private boolean skipToNewline;
    private ArrayList<Text> buffer;
    
    interface Text {
        String getText();
    }
    
    class Comment implements Text {
        private String text;
        public Comment(String text) {
            this.text = text;
        }
        @Override
        public String getText() {
            return text;
        }
    }
    
    class Whitespace implements Text {
        private String text;
        public Whitespace(String text) {
            this.text = text;
        }
        @Override
        public String getText() {
            return text;
        }
    }
    
    public ImprovedConfigReaderListenerAdapter(ImprovedConfigReaderListener listener) {
        this.listener = listener;
        this.currentSection = null;
        this.skipToNewline = false;
        this.buffer = new ArrayList<Text>();
    }

    @Override
    public void setup() throws IOException {
        // Ignoring setup
    }

    @Override
    public void onSection(String section, String text) throws IOException {
        if (currentSection != null) {
            listener.onSectionEnd(currentSection);
        }
        flushBuffer();
        currentSection = section;
        listener.onSection(section, text);
        skipToNewline = true;
    }

    @Override
    public void onOption(String name, String value, String text) throws IOException {
        flushBuffer();
        listener.onOption(name, value, text);
        skipToNewline = true;
    }

    @Override
    public void onComment(String text) throws IOException {
        if (skipToNewline) {
            listener.onComment(text);
            skipToNewline = !text.contains("\n");
        } else {
            buffer.add(new Comment(text));
        }
    }

    @Override
    public void onWhitespace(String text) throws IOException {
        if (skipToNewline) {
            listener.onWhitespace(text);
            skipToNewline = !text.contains("\n");
        } else {
            buffer.add(new Whitespace(text));
        }
    }

    @Override
    public void cleanup() throws IOException {
        if (currentSection != null) {
            listener.onSectionEnd(currentSection);
        }
        flushBuffer();
    }
    
    private void flushBuffer() throws IOException {
        for (Text t : buffer) {
            if (t instanceof Comment) {
                listener.onComment(t.getText());
            } else if (t instanceof Whitespace) {
                listener.onWhitespace(t.getText());
            }
        }
        buffer.clear();
    }
}