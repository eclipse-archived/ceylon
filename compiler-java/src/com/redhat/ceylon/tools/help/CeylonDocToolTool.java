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
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.common.tool.WordWrap;
import com.redhat.ceylon.tools.help.model.Doc;
import com.redhat.ceylon.tools.help.model.Visitor;


@Summary("Generates documentation about a tool")
@Description(
"Generates documentation about the named `<tool>`s in the directory " +
"named by the `--output` option.")
@RemainingSections(
"## See Also\n\n" +
"* `ceylon help` For generating help about ceylon tools at the command line\n" +
"* `ceylon doc` For generating API documentation about ceylon modules\n"
)
public class CeylonDocToolTool implements Tool {

    
    public static enum Format {
        html(".html") {
            @Override
            HtmlVisitor newOutput(CeylonDocToolTool tool, Writer writer) {
                return new HtmlVisitor(writer);
            }

            @Override
            URL[] supportingResources() {
                return new URL[]{getClass().getResource("resources/doc-tool.css")};
            }
        }, 
        txt(".txt") {
            @Override
            PlainVisitor newOutput(CeylonDocToolTool tool, Writer writer) {
                return new PlainVisitor(new WordWrap(writer, tool.width));
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
        abstract Visitor newOutput(CeylonDocToolTool tool, Writer file);
        abstract URL[] supportingResources();
    }
    
    private List<ToolModel<?>> tools;
    private File dir = new File(".");
    private Format format = Format.html;
    private int width = 80;
    private boolean index = false;
    private boolean allPlumbing;
    private boolean allPorcelain;
    protected ToolLoader toolLoader;
    private DocBuilder docBuilder;
        
    public final void setToolLoader(ToolLoader toolLoader) {
        this.toolLoader = toolLoader;
        this.docBuilder = new DocBuilder(toolLoader);
    }

    @Argument(argumentName="tool", multiplicity="*")
    public void setTool(List<ToolModel<?>> tools) {
        this.tools = tools;
    }
    
    @Option
    @Description("Generate documentation about all low level tools, in " +
    		"addition to the tools named by the `<tool>` argument")
    public void setAllPlumbing(boolean allPlumbing) {
        this.allPlumbing = allPlumbing;
    }
    
    @Option
    @Description("Generate documentation about all high level tools, in " +
            "addition to the tools named by the `<tool>` argument")
    public void setAllPorcelain(boolean allPorcelain) {
        this.allPorcelain = allPorcelain;
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
        if (!allPlumbing && !allPorcelain && (tools == null || tools.isEmpty())) {
            throw new IllegalStateException("No tools to process");
        }
        if (index &&  format != Format.html) {
            throw new IllegalStateException("--index is only supported when --format=html");
        }
    }
    
    @Override
    public void run() throws IOException {
        List<Doc> models = loadModels();
        prepareDirectory();
        generateDoc(models);
        copyResources();
    }

    private List<Doc> loadModels() {
        List<Doc> models = new ArrayList<>();
        if (allPlumbing) {
            for (String toolName : toolLoader.getToolNames()) {
                ToolModel<?> model = toolLoader.loadToolModel(toolName);
                if (model.isPlumbing()) {
                    models.add(docBuilder.buildDoc(model));
                }
            }
        }
        if (allPorcelain) {
            for (String toolName : toolLoader.getToolNames()) {
                ToolModel<?> model = toolLoader.loadToolModel(toolName);
                if (model.isPorcelain()) {
                    models.add(docBuilder.buildDoc(model));
                }
            }
        }
        
        if (tools != null) {
            for (ToolModel<?> toolModel : tools) {
                models.add(docBuilder.buildDoc(toolModel));
            }
        }
        
        return models;
    }

    private void generateDoc(List<Doc> docs)
            throws IOException {
        for (Doc doc : docs) {
            File out = new File(dir, filename(doc));
            try (FileWriter writer = new FileWriter(out)) {
                Visitor htmlOutput = format.newOutput(this, writer);
                doc.accept(htmlOutput);
            }
        }
        if (index && format == Format.html) {
            generateIndexHtml(docs);
        }
    }

    private String filename(Doc doc) {
        return Tools.progName() + "-" + doc.getName() + format.extension;
    }

    private void generateIndexHtml(List<Doc> docs) throws IOException {
        File indexFile = new File(dir, "index" + format.extension);
        ResourceBundle bundle = ResourceBundle.getBundle("com.redhat.ceylon.tools.help.resources.sections");
        try (FileWriter writer = new FileWriter(indexFile)) {
            HtmlVisitor htmlOutput = (HtmlVisitor)Format.html.newOutput(this, writer);
            Html html = htmlOutput.getHtml();
            indexHeader(html, bundle.getString("index.title"));
            
            List<Doc> porcelain = new ArrayList<>();
            List<Doc> plumbing = new ArrayList<>();
            for (Doc model : docs) {
                if (model.getToolModel().isPorcelain()) {
                    porcelain.add(model);
                } else if (model.getToolModel().isPlumbing()) {
                    plumbing.add(model);
                }
            }
            if (!porcelain.isEmpty()) {
                html.open("p").text(bundle.getString("index.porcelain.tools")).close("p");
                generateToolList(porcelain, html);
            }
            if (!plumbing.isEmpty()) {
                html.open("p").text(bundle.getString("index.plumbing.tools")).close("p");
                generateToolList(plumbing, html);
            }
            indexFooter(html);
        }
    }

    private void indexHeader(Html html, String title) {
        html.open("html", "head");
        html.open("title").text(title).close("title").text("\n");
        html.tag("link rel='stylesheet'type='text/css' href='doc-tool.css'").text("\n");
        html.close("head").text("\n");
        html.open("body").text("\n");
        html.open("h1").text(title).close("h1");
    }
    
    private void indexFooter(Html html) {
        html.close("body", "html");
    }

    private void generateToolList(List<Doc> docs, Html html) {
        html.open("dl");
        for (Doc doc : docs) {
            html.open("dt", "code", "a href='" + filename(doc) + "'");
            html.text(Tools.progName() +" " + doc.getName());
            html.close("a", "code", "dt");
            html.open("dd").markdown(doc.getSummary().getDescription()).close("dd");
        }
        html.close("dl");
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
