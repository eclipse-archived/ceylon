package com.redhat.ceylon.tools.help;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.NoSuchToolException;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Plugin;
import com.redhat.ceylon.common.tool.PluginModel;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.common.tool.WordWrap;

@Summary("Generates documentation about a tool")
@Description("Extracts tool metadata and generates documentation in a directory.")
public class DocToolTool extends AbstractDoc implements Plugin {

    public static enum Format {
        html(".html") {
            @Override
            public Output newOutput(Writer writer) {
                return new HtmlOutput(new Html(writer));
            }

            @Override
            public URL[] supportingResources() {
                return new URL[]{getClass().getResource("doc-tool.css")};
            }
        }, 
        txt(".txt") {
            @Override
            public Output newOutput(Writer writer) {
                return new PlainOutput(new WordWrap(writer));
            }

            @Override
            public URL[] supportingResources() {
                return null;
            }
        };
        private final String extension;
        private Format(String extension) {
            this.extension = extension;
        }
        public abstract Output newOutput(Writer file);
        public abstract URL[] supportingResources();
    }
    
    private List<String> tools;
    private File dir = new File(".");
    private Format format = Format.html;
    

    @Argument(argumentName="tool", multiplicity="+")
    @Description("The tool(s) to generate the documentation for")
    public void setTool(List<String> tools) {
        this.tools = tools;
    }
    
    @OptionArgument(shortName='o', argumentName="dir")
    @Description("Directory to generate the output files in " +
    		"(default: The current directory)")
    public void setOutput(File dir) {
        this.dir = dir;
    }
    
    @OptionArgument(argumentName="format")
    @Description("The format to generate the documentation in " +
    		"(allowable values: `html` or `txt`, default: `html`)")
    public void setFormat(Format format) {
        this.format = format;
    }
    
    @Override
    public void run() throws IOException {
        List<ToolDocumentation<?>> models = loadModels();
        prepareDirectory();
        generateDoc(models);
        copyResources();
    }

    private List<ToolDocumentation<?>> loadModels() {
        List<ToolDocumentation<?>> models = new ArrayList<>(tools.size());
        for (String command : tools) {
            final PluginModel<?> model = toolLoader.loadToolModel(command);
            if (model != null) {
                ToolDocumentation<?> docModel = new ToolDocumentation<>(model);
                models.add(docModel);
            } else {
                final WordWrap wrap = new WordWrap();
                Tools.printToolSuggestions(toolLoader, wrap, command);
                wrap.flush();
                throw new NoSuchToolException(command);
            }
        }
        return models;
    }

    private void generateDoc(List<ToolDocumentation<?>> models)
            throws IOException {
        for (ToolDocumentation<?> model : models) {
            File out = new File(dir, model.getName() + format.extension);
            try (FileWriter writer = new FileWriter(out)) {
                Output htmlOutput = format.newOutput(writer);
                printToolHelp(htmlOutput, model);
            }
        }
    }

    private void prepareDirectory() {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.isDirectory()) {
            throw new RuntimeException(dir + " is not a directory");
        }
    }

    private void copyResources() throws IOException {
        URL[] resources = format.supportingResources();
        if (resources != null) {
            for (URL resource : resources) {
                copyResource(resource, dir);
            }
        }
    }

    private void copyResource(URL resource, File dir) throws IOException {
        File toFile = new File(dir, new File(resource.getPath()).getName());
        byte[] buf = new byte[1024];
        try  (InputStream in = resource.openStream()) {
            try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(toFile))) {
                int read = in.read(buf);
                while (read != -1) {
                    out.write(buf, 0, read);
                    read = in.read(buf);
                }
            }
        }
    }

}
