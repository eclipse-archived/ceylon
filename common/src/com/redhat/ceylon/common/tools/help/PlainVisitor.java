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
import com.redhat.ceylon.common.tool.WordWrap;

public class PlainVisitor implements Visitor {

    private int numOptions;
    protected final WordWrap out;
    protected String ceylonName;
    boolean hadFirstArgument = false;
    private boolean hadOptions;
    private boolean suboptions;

    public PlainVisitor(WordWrap wrap) {
        super();
        this.out = wrap;
    }

    private void markdown(Node doc) {
        PlaintextMarkdownVisitor markdownVisitor = new PlaintextMarkdownVisitor(out);
        doc.accept(markdownVisitor);
    }

    @Override
    public void start(Doc doc) {
        ceylonName = doc.getInvocation();
    }

    @Override
    public void end(Doc doc) {
        out.flush();
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
        int indent = out.getIndentFirstLine();
        String invocation = synopsis.getInvocation();
        out.setIndentRestLines(indent + invocation.length() + 1);
        out.append(invocation);
        hadFirstArgument = false;
        hadOptions = false;
    }
    
    @Override
    public void endSynopsis(Synopsis synopsis) {
        out.newline();
    }

    @Override
    public void visitSynopsisArgument(ArgumentModel<?> argument) {
        if (hadOptions && !hadFirstArgument) {
            out.append(" [--]");
            hadFirstArgument = true;
        }
        
        out.append(" ");
        String name = argument.getName();
        if (!argument.getMultiplicity().isRequired()) {
            out.append("[");
        }
        out.append("<" + name);
        if (argument.getMultiplicity().isMultivalued()) {
            out.append("...");
        }
        out.append(">");
        if (!argument.getMultiplicity().isRequired()) {
            out.append("]");
        }
    }

    @Override
    public void visitSynopsisSubtool(SubtoolVisitor.ToolModelAndSubtoolModel option) {
        out.append(" " + option.getModel().getName());
    }
    
    @Override
    public void visitSynopsisOption(OptionModel<?> option) {
        hadOptions = true;
        out.append(" ");
        final ArgumentModel<?> argument = option.getArgument();
        if (!argument.getMultiplicity().isRequired()) {
            out.append("[");
        }
        if (option.getLongName() != null) {
            out.append("--" + option.getLongName());
            if (option.getArgumentType() == ArgumentType.REQUIRED) {
                out.append("=");
                out.append(multiplicity(argument, argument.getName()));
            } else if (option.getArgumentType() == ArgumentType.OPTIONAL) {
                out.append("[=");
                out.append(multiplicity(argument, argument.getName()));
                out.append("]");
            }
        } else {
            out.append("-" + option.getShortName());
            if (option.getArgumentType() == ArgumentType.REQUIRED) {
                out.append(" ");
                out.append(multiplicity(argument, argument.getName()));
            }
        }
        if (!argument.getMultiplicity().isRequired()) {
            out.append("]");
        }
    }
    
    
    @Override
    public void startSynopses(SynopsesSection synopsesSection) {
        out.append(synopsesSection.getTitle().toUpperCase()).newline().newline();
        out.setIndent(8);
    }
    
    @Override
    public void endSynopses(SynopsesSection synopsesSection) {
        out.setIndent(0);
        out.newline();
        out.newline();   
    }
    
    @Override
    public void startOptions(OptionsSection optionsSection) {
        if (suboptions) {
            out.setIndent(3);
        }
        markdown(optionsSection.getTitle());
        out.setIndent(8);
        suboptions = true;
    }

    @Override
    public void visitOption(Option option) {
        String shortName = option.getShortName();
        String longName = option.getLongName();
        String argumentName = option.getArgumentName();
        ArgumentType argumentType = option.getOption().getArgumentType();
        numOptions++;
        out.append(longName);
        if (argumentType == ArgumentType.OPTIONAL) {
            out.append("[");
        }
        if (argumentType != ArgumentType.NOT_ALLOWED) {
            out.append("=<" + argumentName + ">");
        }
        if (argumentType == ArgumentType.OPTIONAL) {
            out.append("]");
        }
        if (shortName != null) {
            out.append(", ");
            out.append(shortName);
            if (argumentType == ArgumentType.REQUIRED) {
                out.append(" ");
                out.append("<" + argumentName + ">");
            }
        }
        
        out.setIndent(12);
        out.newline();
        markdown(option.getDescription());    
        out.newline();
        out.setIndent(8);
    
    }

    @Override
    public void endOptions(OptionsSection optionsSection) {
        if(numOptions == 0) {
            out.append(ceylonName+" has no options").newline();
        }
        out.setIndent(0);
        out.newline();
    }
    
    @Override
    public void visitAdditionalSection(DescribedSection describedSection) {
        describedSection(describedSection);    
    }

    private void describedSection(DescribedSection describedSection) {
        markdown(describedSection.getTitle());
        markdown(describedSection.getDescription());
        
        for (DescribedSection subsection : describedSection.getSubsections()) {
            describedSection(subsection);
        }
        
        out.setIndent(0);
        out.newline();
    }

    @Override
    public void visitSummary(SummarySection summarySection) {
        markdown(summarySection.getTitle());
        markdown(Markdown.markdown("`" + ceylonName + "` - " + summarySection.getSummary()));
        out.setIndent(0);
        out.newline();
    }

    @Override
    public void visitDescription(DescribedSection descriptionSection) {
        describedSection(descriptionSection);
    }


}
