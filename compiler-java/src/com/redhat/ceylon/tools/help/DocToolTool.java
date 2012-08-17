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

import javax.annotation.PostConstruct;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.NoSuchToolException;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Plugin;
import com.redhat.ceylon.common.tool.PluginModel;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.common.tool.WordWrap;

@Summary("Generates documentation about a tool")
@Description("Extracts tool metadata and generates documentation in the directory " +
		"named by the `--output` option.")
@RemainingSections(
"## See Also\n\n" +
"* `ceylon help` For generating help about ceylon tools at the command line\n" +
"* `ceylon doc` For generating API documentation about ceylon modules\n"
)
public class DocToolTool extends AbstractDoc implements Plugin {

    static final String ALL_TOOLS = "+all";
    
    public static enum Format {
        html(".html") {
            @Override
            HtmlOutput newOutput(DocToolTool tool, Writer writer) {
                return new HtmlOutput(new Html(writer));
            }

            @Override
            URL[] supportingResources() {
                return new URL[]{getClass().getResource("resources/doc-tool.css")};
            }
        }, 
        txt(".txt") {
            @Override
            PlainOutput newOutput(DocToolTool tool, Writer writer) {
                return new PlainOutput(new WordWrap(writer, tool.width));
            }

            @Override
            URL[] supportingResources() {
                return null;
            }
        };
        private final String extension;
        private Format(String extension) {
            this.extension = extension;
        }
        abstract Output newOutput(DocToolTool tool, Writer file);
        abstract URL[] supportingResources();
    }
    
    private List<String> tools;
    private File dir = new File(".");
    private Format format = Format.html;
    private int width = 80;
    private boolean index = false;

    @Argument(argumentName="tool", multiplicity="+")
    @Description("The tool(s) to generate the documentation for. The special " +
    		"value `" + ALL_TOOLS + "` generates documentation about all known tools.")
    public void setTool(List<String> tools) {
        this.tools = tools;
    }
    
    @Option
    @Description("Generate an `index.html` file when `--format=html`")
    public void setIndex(boolean index) {
        this.index = index;
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
    
    @OptionArgument(argumentName="cols")
    @Description("The line length to use for word wrapping " +
    		"when `--format=txt` " +
            "(default: 80)")
    public void setWidth(int width) {
        if (width <= 0) {
            throw new IllegalArgumentException();
        }
        this.width = width;
    }
    
    @PostConstruct
    public void init() {
        if (index &&  format != Format.html) {
            throw new IllegalStateException("--index is only supported when --format=html");
        }
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
        if (tools.contains(ALL_TOOLS)) {
            models = new ArrayList<>();
            for (String toolName : toolLoader.getToolNames()) {
                models.add(loadModel(toolName));
            }
        } else {
            models = new ArrayList<>(tools.size());
            for (String toolName : tools) {
                models.add(loadModel(toolName));
            }
        }
        return models;
    }

    private ToolDocumentation<?> loadModel(String toolName) {
        final PluginModel<?> model = toolLoader.loadToolModel(toolName);
        if (model != null) {
            return new ToolDocumentation<>(model);
        } 
        final WordWrap wrap = new WordWrap();
        Tools.printToolSuggestions(toolLoader, wrap, toolName);
        wrap.flush();
        throw new NoSuchToolException(toolName);
    }

    private void generateDoc(List<ToolDocumentation<?>> models)
            throws IOException {
        for (ToolDocumentation<?> model : models) {
            File out = new File(dir, Tools.progName() + "-" + model.getName() + format.extension);
            try (FileWriter writer = new FileWriter(out)) {
                Output htmlOutput = format.newOutput(this, writer);
                printToolHelp(htmlOutput, model);
            }
        }
        if (index && format == Format.html) {
            generateIndexHtml(models);
        }
    }

    private void generateIndexHtml(List<ToolDocumentation<?>> models) throws IOException {
        File indexFile = new File(dir, "index" + format.extension);
        try (FileWriter writer = new FileWriter(indexFile)) {
            HtmlOutput htmlOutput = (HtmlOutput)Format.html.newOutput(this, writer);
            Html html = htmlOutput.getHtml();
            String title = Tools.progName() + " tools";
            htmlOutput.title(title);
            html.open("h1").text(title).close("h1");
            html.open("dl");
            for (ToolDocumentation<?> model : models) {
                html.open("dt").open("code");
                html.text(Tools.progName() +" " + model.getName());
                html.close("code", "dt");
                html.open("dd").text(model.getSummary()).close("dd");
            }
            html.close("dl");
            html.close("body", "html");
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
