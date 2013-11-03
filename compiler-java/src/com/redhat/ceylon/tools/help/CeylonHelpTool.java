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

import java.util.Arrays;
import java.util.HashSet;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.WordWrap;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.tools.help.model.Doc;
import com.redhat.ceylon.tools.help.model.Visitor;

/**
 * A plugin which provides help about other plugins 
 * @author tom
 */
@Summary("Displays help information about other Ceylon tools")
@Description(
"If a `<tool>` is given, displays help about that ceylon tool on the standard output.\n\n" +
"If no `<tool>` is given, displays the synopsis of the top level `ceylon` command. "
)
@RemainingSections(
"## SEE ALSO\n\n" +
"* `ceylon doc-tool` for generating documentation about ceylon tools\n"
)
public class CeylonHelpTool implements Tool {

    private Appendable out;
    private boolean includeHidden;
    private ToolLoader toolLoader;
    private DocBuilder docBuilder;
    private ToolModel<?> tool;
    private boolean synopsis = false;
    private String options = null;
    
    public final void setToolLoader(ToolLoader toolLoader) {
        this.toolLoader = toolLoader;
        this.docBuilder = new DocBuilder(toolLoader);
    }
    
    @Option
    public void setIncludeHidden(boolean includeHidden) {
        this.includeHidden = includeHidden;
    }
    
    @Hidden
    @Option
    @Description("Used to generate a synopsis when another tool was invoked incorrectly")
    public void setSynopsis(boolean synopsis) {
        this.synopsis = synopsis;
    }
    
    @Hidden
    @Option
    @OptionArgument
    @Description("Used to generate doc on a given option (or options)")
    public void setOptions(String options) {
        this.options = options;
    }
    
    @Argument(argumentName="tool", multiplicity="?")
    public void setTool(ToolModel<?> tool) {
        this.tool = tool;
    }
    
    public void setOut(Appendable out) {
        this.out = out;
    }

    @Override
    public void initialize() {
    }
    
    @Override
    public void run() {
        docBuilder.setIncludeHidden(includeHidden);
        Doc doc;
        if (tool != null) {
            doc = docBuilder.buildDoc(tool);
        } else {
            final ToolModel<CeylonTool> root = toolLoader.loadToolModel("");
            doc = docBuilder.buildDoc(root, true);
        }
        final WordWrap wrap = getWrap();
        Visitor plain = new PlainVisitor(wrap);
        if (synopsis) {
            plain = new SynopsisOnlyVisitor(plain);
        } else if (options != null) {
            plain = new OptionsOnlyVisitor(plain, 
                    new HashSet<String>(Arrays.asList(options.trim().split("\\s*,\\s*"))));
        }
        doc.accept(plain);
        wrap.flush();
    }

    private WordWrap getWrap() {
        return new WordWrap(out != null ? out 
                : synopsis || options != null ? System.err 
                : System.out);
    }
}
