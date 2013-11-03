/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
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

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.CeylonBaseTool;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
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
public class CeylonDocToolTool extends CeylonBaseTool {

    
    public static enum Format {
        html(".html") {
            @Override
            HtmlVisitor newOutput(CeylonDocToolTool tool, Writer writer) {
                return new HtmlVisitor(writer, tool.omitDoctype);
            }

            @Override
            URL[] supportingResources() {
                return new URL[]{getClass().getResource("resources/doc-tool.css"),
                        getClass().getResource("resources/bootstrap.min.css"),
                        getClass().getResource("resources/bootstrap.min.js"),
                        getClass().getResource("resources/jquery-1.8.2.min.js"),
                        getClass().getResource("resources/NOTICE.txt"),
                        getClass().getResource("resources/doc-tool.js"),
                        getClass().getResource("resources/ceylondoc-icons.png"),
                        getClass().getResource("resources/ceylondoc-logo.png")};
            }
        },
        docbook(".xml") {
            @Override
            DocBookVisitor newOutput(CeylonDocToolTool tool, Writer writer) {
                return new DocBookVisitor(writer, tool.omitDoctype);
            }

            @Override
            URL[] supportingResources() {
                return null;
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
    private boolean omitDoctype;
        
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
    
    @Option
    @Description("Omit the doctype when outputting XML-based formats")
    public void setOmitDoctype(boolean omitDoctype) {
        this.omitDoctype = omitDoctype;
    }
    
    @OptionArgument(shortName='o', argumentName="dir")
    @Description("Directory to generate the output files in " +
            "(default: The current directory)")
    public void setOutput(File dir) {
        this.dir = dir;
    }
    
    @OptionArgument(argumentName="format")
    @Description("The format to generate the documentation in " +
            "(allowable values: `html`, `docbook` or `txt`, default: `html`)")
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
    
    @Override
    public void initialize() {
        setSystemProperties();
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
                Visitor visitor = format.newOutput(this, writer);
                doc.accept(visitor);
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
            AbstractMl html = htmlOutput.getHtml();
            indexHeader(html, bundle.getString("index.title"), bundle.getString("index.overview"));
            
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
                generateToolList(porcelain, html, bundle.getString("index.porcelain.tools"));
            }
            if (!plumbing.isEmpty()) {
                generateToolList(plumbing, html, bundle.getString("index.plumbing.tools"));
            }
            indexFooter(html);
        }
    }

    private void indexHeader(AbstractMl html, String title, String overview) {
        html.doctype("html").text("\n");
        html.open("html", "head").text("\n");
        html.tag("meta charset='UTF-8'").text("\n");
        html.open("title").text(title).close("title").text("\n");
        html.tag("link rel='stylesheet' type='text/css' href='bootstrap.min.css'").text("\n");
        html.tag("link rel='stylesheet' type='text/css' href='doc-tool.css'").text("\n");
        html.close("head").text("\n");
        html.open("body").text("\n");
        html.open("div class='navbar navbar-inverse navbar-static-top'").text("\n");
        html.open("div class='navbar-inner'").text("\n");
        html.open("a class='tool-header' href='index.html'").text("\n");
        html.open("i class='tool-logo'").close("i").text("\n");
        html.open("span class='tool-label'").text(title).close("span").text("\n");
        html.open("span class='tool-name'").text(overview).close("span").text("\n");
        html.close("a").text("\n");
        html.close("div").text("\n");
        html.close("div").text("\n");
        html.tag("div class='tool-description'").text("\n");
        html.open("div class='container-fluid'").text("\n");
    }
    
    private void indexFooter(AbstractMl html) {
        html.close("div", "body", "html");
    }
    
    private void generateToolList(List<Doc> docs, AbstractMl html, String title) {
        html.open("table class='table table-condensed table-bordered table-hover'").text("\n");
        html.open("thead").text("\n");
        html.open("tr class='table-header'");
        html.open("td colspan='2'").text(title).close("td");
        html.close("tr").text("\n");
        html.close("thead").text("\n");

        html.open("tbody");
        for (Doc doc : docs) {
            html.open("tr");
            html.open("td class='span3'", "a class='link' href='" + filename(doc) + "'");
            html.open("code").text(Tools.progName() + " " + doc.getName()).close("code");
            html.close("a", "td").text("\n");
            html.open("td", "p");
            html.text(doc.getSummary().getSummary());
            html.close("p", "td");
            html.close("tr").text("\n");
        }
        html.close("tbody").text("\n");
        html.close("table").text("\n");
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
