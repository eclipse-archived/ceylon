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
package com.redhat.ceylon.common.tools.help;

import java.util.ResourceBundle;

import org.tautua.markdownpapers.ast.Node;

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.OptionModel.ArgumentType;
import com.redhat.ceylon.common.tools.help.model.DescribedSection;
import com.redhat.ceylon.common.tools.help.model.Doc;
import com.redhat.ceylon.common.tools.help.model.Option;
import com.redhat.ceylon.common.tools.help.model.OptionsSection;
import com.redhat.ceylon.common.tools.help.model.SubtoolVisitor;
import com.redhat.ceylon.common.tools.help.model.SummarySection;
import com.redhat.ceylon.common.tools.help.model.SynopsesSection;
import com.redhat.ceylon.common.tools.help.model.Synopsis;
import com.redhat.ceylon.common.tools.help.model.Visitor;
import com.redhat.ceylon.common.tools.help.model.DescribedSection.Role;
import com.redhat.ceylon.common.tool.ToolModel;

public class HtmlVisitor implements Visitor {

    private final Html html;
    private boolean hadFirstArgument;
    private boolean hadOptions;
    private Doc doc;
    private int optionsDepth = 0;
    private final boolean omitDoctype;
    
    HtmlVisitor(Appendable out, boolean omitDoctype) {
        this.html = new Html(out);
        this.omitDoctype = omitDoctype;
    }
    
    AbstractMl getHtml() {
        return html;
    }
    
    @Override
    public void start(Doc doc) {
        this.doc = doc;
        ResourceBundle bundle = CeylonHelpToolMessages.RESOURCE_BUNDLE;
        if (!omitDoctype) {
            html.doctype("html").text("\n");
        }
        html.open("html", "head");
        html.tag("meta charset='UTF-8'").text("\n");
        html.open("title").text(doc.getInvocation()).close("title").text("\n");
        html.tag("link rel='stylesheet' type='text/css' href='bootstrap.min.css'").text("\n");
        html.tag("link rel='stylesheet' type='text/css' href='doc-tool.css'").text("\n");
        html.close("head").text("\n");
        html.open("body").text("\n");
        html.open("div class='navbar navbar-inverse navbar-static-top'").text("\n");
        html.open("div class='navbar-inner'").text("\n");
        html.open("a class='tool-header' href='index.html'").text("\n");
        html.open("i class='tool-logo'").close("i").text("\n");
        html.open("span class='tool-label'").text(bundle.getString("index.title")).close("span").text("\n");
        html.open("span", "code class='tool-name'").text(doc.getInvocation()).close("code", "span").text("\n");
        html.open("span class='tool-version'").text(doc.getVersion()).close("span").text("\n");
        html.close("a").text("\n");
        
        html.open("ul class='nav pull-right'");
        html.tag("li class='divider-vertical'");
        html.open("li id='infoDropdown' class='dropdown'");
        html.open("a href='#' title='Show keyboard shortcuts [Shortcut: ?]' role='button' class='dropdown-toggle' data-toggle='dropdown'");
        html.open("i class='icon-info'").close("i");
        html.close("a");
        html.open("ul id='info-dropdown-panel' class='dropdown-menu'");
        html.open("h4").text("Keyboard Shortcuts").close("h4");
        html.open("li class='divider'").close("li");
        html.open("div id='info-common-shortcuts'");
        shortcutInfo(html, "?", "Open this information panel");
        shortcutInfo(html, "i", "Jump to tool index");
        shortcutInfo(html, "s", "Jump to synopsis");
        shortcutInfo(html, "d", "Jump to description");
        shortcutInfo(html, "o", "Jump to options");
        html.close("div", "ul", "li", "ul");      

        html.close("div").text("\n");
        html.close("div").text("\n");
    }

    private static void shortcutInfo(AbstractMl html, String key, String description) {
        html.open("div id='"+key+"'").open("span class='key badge'").text(key).close("span");
        html.open("span class='info muted'").text(description).close("span", "div");
    }
    
    @Override
    public void end(Doc doc) {
        html.close("div");
        html.open("script type='text/javascript' src='jquery-1.8.2.min.js'").close("script");
        html.open("script type='text/javascript' src='bootstrap.min.js'").close("script");
        html.open("script type='text/javascript' src='doc-tool.js'").close("script");
        html.open("script type='text/javascript'").unescaped("init();").close("script");
        html.close("body", "html");
    }

    private static void addTableStart(AbstractMl html, String section, String title, int cols) {
        addTableStart(html, section, Markdown.markdown("##" + title), cols);
    }
    
    private static void addTableStart(AbstractMl html, String sectionId, Node title, int cols) {
        html.open("table class='table table-condensed table-bordered'").text("\n");
        html.open("thead").text("\n");
        html.open("tr class='table-header' title='Click for expand/collapse'");
        html.open("td colspan='" + cols + "'" + (sectionId != null ? " id='" + sectionId + "'" : ""));
        html.open("i class='icon-expand'").close("i");
        html.markdown(title).close("td", "tr").text("\n");
        html.close("thead").text("\n");
        html.open("tbody").text("\n");
    }

    private static void addTableEnd(AbstractMl html) {
        html.close("tbody", "table");
    }
    
    @Override
    public void visitAdditionalSection(DescribedSection describedSection) {
        describedSection(0, describedSection);
    }

    private void describedSection(int depth, DescribedSection describedSection) {
        String sectionId = null;
        if (depth == 0) {
            if (describedSection.getRole() == Role.DESCRIPTION) {
                html.open("div class='section section-description'").text("\n");
                sectionId = "section-description";
            } else {
                html.open("div class='section'").text("\n");
            }
            addTableStart(html, sectionId, describedSection.getTitle(), 1);
            html.open("tr", "td").markdown(describedSection.getDescription());
        } else {
            if (describedSection.getAbout() instanceof ToolModel) {
                html.open("div id='" + idSubtool((ToolModel<?>)describedSection.getAbout()) + "'");
            } else {
                html.open("div");
            }
            html.markdown(describedSection.getTitle());
            html.markdown(describedSection.getDescription());
        }
        for (DescribedSection subsection : describedSection.getSubsections()) {
            describedSection(depth+1, subsection);
        }
        
        if (depth != 0) {
            html.close("div");
        } else {
            html.close("td", "tr");
            addTableEnd(html);
            html.close("div").text("\n");
        }
    }

    @Override
    public void startOptions(OptionsSection optionsSection) {
        if (optionsDepth == 0) {
            html.open("div class='section section-options'").text("\n");
            addTableStart(html, "section-options", optionsSection.getTitle(), 2);
        } else {
            html.open("tr class='table-header'", "td colspan='2'");
            html.markdown(optionsSection.getTitle());
            html.close("td", "tr");
        }
        optionsDepth++;
    }

    private String idLongOption(OptionModel<?> option) {
        // TODO Could be ambiguous need to use whole path from root
        return "option--" + option.getLongName();
    }
    
    private String idShortOption(OptionModel<?> option) {
        // TODO Could be ambiguous need to use whole path from root
        return "option-" + option.getShortName();
    }

    private String idSubtool(ToolModel<?> model) {
        // TODO Could be ambiguous need to use whole path from root
        return "subtool-" + model.getName();
    }
    
    @Override
    public void visitOption(Option option) {
        String longName = option.getLongName();
        String shortName = option.getShortName();
        String argumentName = option.getArgumentName();
        ArgumentType argumentType = option.getOption().getArgumentType();
        html.open("tr");
        html.open("td class='span3' id='" + idLongOption(option.getOption()) + "'", "code").text(longName);
        if (argumentType == ArgumentType.OPTIONAL) {
            html.text("[");
        }
        if (argumentType != ArgumentType.NOT_ALLOWED) {
            html.text("=").text(argumentName);
        }
        if (argumentType == ArgumentType.OPTIONAL) {
            html.text("]");
        }
        html.close("code");
        if (shortName != null) {
            html.text(", ");
            html.open("code id='" + idShortOption(option.getOption()) + "'").text(shortName);
            if (argumentType == ArgumentType.REQUIRED) {
                html.text(" ");
                html.text(argumentName);
            }
            html.close("code");
        }
        html.close("td").text("\n");
        html.open("td class='option-description'");
        html.markdown(option.getDescription());
        html.close("td").text("\n");
        html.close("tr");
    }

    @Override
    public void endOptions(OptionsSection optionsSection) {
        optionsDepth--;
        if (optionsDepth == 0) {
            addTableEnd(html);
            html.close("div");
        }
    }

    @Override
    public void visitSummary(SummarySection summarySection) {
        html.open("div class='sub-navbar'").text("\n");
        html.open("div class='sub-navbar-inner'");
        html.open("div", "code class='sub-navbar-tool'").text(doc.getInvocation()).close("code", "div").text("\n");
        html.open("div class='sub-navbar-summary'").markdown(Markdown.markdown(summarySection.getSummary())).close("div").text("\n");
        html.close("div");
        html.open("div class='sub-navbar-menu'").text("\n");
        addShortcutKey(html, "index.html", "Jump to tool index", "I", "ndex");
        addShortcutKey(html, "#section-synopsis", "Jump to tool synopsis", "S", "ynopsis");
        addShortcutKey(html, "#section-description", "Jump to tool description", "D", "escription");
        addShortcutKey(html, "#section-options", "Jump to tool options", "O", "ptions");
        html.close("div", "div").text("\n");

        html.open("div class='container-fluid'").text("\n");
    }

    private static void addShortcutKey(AbstractMl html, String url, String title, String key, String rest) {
        html.open("a href='" + url + "'");
        html.open("span title='" + title + " [Shortcut: "+key+"]'");
        html.open("span class='accesskey'").text(key).close("span").text(rest).close("span", "a");
    }
    
    
    @Override
    public void startSynopses(SynopsesSection synopsesSection) {
        html.open("div class='section section-synopsis'").text("\n");
        addTableStart(html, "section-synopsis", synopsesSection.getTitle(), 1);
    }
    
    private void longOptionSynopsis(OptionModel<?> option) {
        String string = "--" + option.getLongName();
        html.link(string, "#" + idLongOption(option));
    }
    
    private void shortOptionSynopsis(OptionModel<?> option) {
        String string = "-" + option.getShortName();
        html.link(string, "#" + idShortOption(option));
    }

    private void argumentSynopsis(String name) {
        html.link(name, "#arg" + name);
    }
    
    private void subtoolSynopsis(SubtoolVisitor.ToolModelAndSubtoolModel nast) {
        String name = nast.getName();
        if (nast.getModel().getSubtoolModel() == null) {
            html.link(name, "#" + idSubtool(nast.getModel()));
        } else {
            html.text(name);
        }
    }

    private String multiplicity(ArgumentModel<?> argument, String name) {
        name = "<" + name + ">";
        if (argument.getMultiplicity().isMultivalued()) {
            name += "...";
        }
        return name;
    }

    @Override
    public void startSynopsis(Synopsis synopsis) {
        hadFirstArgument = false;
        hadOptions = false;
        html.open("tr", "td");
        html.open("div class='synopsis'", "code");
        html.text(synopsis.getInvocation() + " ");
    }

    @Override
    public void endSynopsis(Synopsis synopsis) {
        html.close("code", "div").text("\n");   
        html.close("td", "tr");
    }

    @Override
    public void visitSynopsisArgument(ArgumentModel<?> argument) {
        if (!hadFirstArgument) {
            html.text(" [--]");
            hadFirstArgument = true;
        }
        
        html.text(" ");
        String name = argument.getName();
        if (!argument.getMultiplicity().isRequired()) {
            html.text("[");
        }
        // don't generate a # link because arguments have no name or id
        html.text("<").text(name);
        if (argument.getMultiplicity().isMultivalued()) {
            html.text("...");
        }
        html.text(">");
        if (!argument.getMultiplicity().isRequired()) {
            html.text("]");
        }
    }

    @Override
    public void visitSynopsisOption(OptionModel<?> option) {
        hadOptions = true;
        html.text(" ");
        final ArgumentModel<?> argument = option.getArgument();
        if (!argument.getMultiplicity().isRequired()) {
            html.text("[");
        }
        if (option.getLongName() != null) {
            longOptionSynopsis(option);
            if (option.getArgumentType() == ArgumentType.REQUIRED) {
                html.text("=");
                html.text(multiplicity(argument, argument.getName()));
            } else if (option.getArgumentType() == ArgumentType.OPTIONAL) {
                html.text("[=");
                html.text(multiplicity(argument, argument.getName()));
                html.text("]");
            }
        } else {
            shortOptionSynopsis(option);
            if (option.getArgumentType() == ArgumentType.REQUIRED) {
                html.text(" ");
                html.text(multiplicity(argument, argument.getName()));
            }
        }
        if (!argument.getMultiplicity().isRequired()) {
            html.text("]");
        }
    }

    @Override
    public void endSynopses(SynopsesSection synopsesSection) {
        addTableEnd(html);
        html.close("div").text("\n\n");
    }

    @Override
    public void visitDescription(DescribedSection descriptionSection) {
        describedSection(0, descriptionSection);
    }

    @Override
    public void visitSynopsisSubtool(SubtoolVisitor.ToolModelAndSubtoolModel option) {
        html.text(" ");
        subtoolSynopsis(option);
            
    }

}
