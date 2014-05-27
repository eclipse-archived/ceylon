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

public class DocBookVisitor implements Visitor {

    private final DocBook docbook;
    private boolean hadFirstArgument;
    private boolean hadOptions;
    private Doc doc;
    private int optionsDepth = 0;
    private final boolean omitDoctype;
    private int openVariablelist;
    
    DocBookVisitor(Appendable out, boolean omitDoctype) {
        this.docbook = new DocBook(out);
        this.omitDoctype = omitDoctype;
    }
    
    AbstractMl getDocbook() {
        return docbook;
    }
    
    @Override
    public void start(Doc doc) {
        this.doc = doc;
        ResourceBundle bundle = CeylonHelpToolMessages.RESOURCE_BUNDLE;
        if (!omitDoctype) {
            docbook.doctype(
            		"refentry PUBLIC \"-//OASIS//DTD DocBook XML V4.1.2//EN\"\n"+
                    "\"http://www.oasis-open.org/docbook/xml/4.1.2/docbookx.dtd\"").text("\n");
        }
        
        docbook.open("refentry").text("\n");
        docbook.open("refmeta").text("\n");
        docbook.open("refentrytitle").text(doc.getInvocation()).close("refentrytitle").text("\n");
        docbook.open("manvolnum").text("1").close("manvolnum").text("\n");
        docbook.close("refmeta").text("\n");
    }

    @Override
    public void end(Doc doc) {
        docbook.close("refentry");
    }

    
    @Override
    public void visitAdditionalSection(DescribedSection describedSection) {
        describedSection(0, describedSection);
    }

    private void describedSection(int depth, DescribedSection describedSection) {
        /*
         <refsect1>
            <title>Description</title>
            <para></para>
          </refsect1>
        */
        
        if (depth <= 2) {
            docbook.open("refsect"+(depth+1)).text("\n");
            docbook.markdown(describedSection.getTitle()).text("\n");
            docbook.markdown(describedSection.getDescription());
        } else {
            throw new RuntimeException("No refsect4");
        }
        for (DescribedSection subsection : describedSection.getSubsections()) {
            describedSection(depth+1, subsection);
        }
        
        docbook.close("refsect"+(depth+1)).text("\n");
    }

    @Override
    public void startOptions(OptionsSection optionsSection) {
        /*
         <refsect1>
           <title>Description</title>
           <para></para>
         </refsect1>
         */
        if (optionsDepth <= 2) {
            if (optionsDepth > 0) {
                docbook.close("variablelist");
                this.openVariablelist--;
            }
            docbook.open("refsect" + (optionsDepth + 1)).text("\n");
            docbook.markdown(optionsSection.getTitle()).text("\n");
            docbook.open("variablelist").text("\n");
            this.openVariablelist++;
        } else {
            throw new RuntimeException();
        }
        optionsDepth++;
    }
    
    @Override
    public void visitOption(Option option) {
        String longName = option.getLongName();
        String shortName = option.getShortName();
        String argumentName = option.getArgumentName();
        ArgumentType argumentType = option.getOption().getArgumentType();
        docbook.open("varlistentry").text("\n");
        docbook.open("term").text(longName);
        if (argumentType == ArgumentType.OPTIONAL) {
            docbook.text("[");
        }
        if (argumentType != ArgumentType.NOT_ALLOWED) {
            docbook.text("=").open("replaceable").text(argumentName).close("replaceable");
        }
        if (argumentType == ArgumentType.OPTIONAL) {
            docbook.text("]");
        }
        docbook.close("term").text("\n");
        
        if (shortName != null) {
            docbook.open("term");
            docbook.text(shortName);
            if (argumentType == ArgumentType.REQUIRED) {
                docbook.text(" ");
                docbook.open("replaceable").text(argumentName).close("replaceable");
            }
            docbook.close("term").text("\n");
        }
        
        docbook.open("listitem").text("\n");
        docbook.markdown(option.getDescription());
        docbook.close("listitem").text("\n");
        docbook.close("varlistentry").text("\n");
    }

    @Override
    public void endOptions(OptionsSection optionsSection) {
        optionsDepth--;
        if (optionsDepth < 3) {
            if (this.openVariablelist > 0) {
                docbook.close("variablelist").text("\n");
                this.openVariablelist--;
            }
            //docbook.close("variablelist").text("\n");
            docbook.close("refsect" + (optionsDepth + 1)).text("\n");
        }
    }

    @Override
    public void visitSummary(SummarySection summarySection) {
        docbook.open("refnamediv").text("\n");
        docbook.open("refname").text(doc.getInvocation()).close("refname").text("\n");
        docbook.open("refpurpose").text(doc.getSummary().getSummary()).close("refpurpose").text("\n");
        docbook.close("refnamediv").text("\n");
    }

    
    @Override
    public void startSynopses(SynopsesSection synopsesSection) {
        /*
        <refsynopsisdiv>
        <cmdsynopsis>
          <command>ceylon compile</command>
          <arg choice="opt">--out=REPO</arg>
          <arg choice="opt" rep="repeat">--src=DIR</arg>
          <arg choice="req" rep="repeat">module</arg>
        </cmdsynopsis>
      </refsynopsisdiv>
      */
        
        docbook.open("refsynopsisdiv").text("\n");
    }
    
    private void longOptionSynopsis(OptionModel<?> option) {
        docbook.text("--" + option.getLongName());
        
    }
    
    private void shortOptionSynopsis(OptionModel<?> option) {
        docbook.text("-" + option.getShortName());
    }

    private void argumentSynopsis(String name) {
        docbook.text(name);
    }
    
    private void subtoolSynopsis(SubtoolVisitor.ToolModelAndSubtoolModel nast) {
        String name = nast.getName();
        docbook.open("arg choice=\"plain\"").text(name).close("arg").text("\n");
    }

    private void multiplicity(ArgumentModel<?> argument, String name) {
        docbook.open("replaceable").text(name).close("replaceable");
        if (argument.getMultiplicity().isMultivalued()) {
            docbook.text("...");
        }
    }

    @Override
    public void startSynopsis(Synopsis synopsis) {
        hadFirstArgument = false;
        hadOptions = false;
        docbook.open("cmdsynopsis").text("\n");
        docbook.open("command").text(doc.getInvocation()).close("command").text("\n");
    }

    @Override
    public void endSynopsis(Synopsis synopsis) {
        docbook.close("cmdsynopsis").text("\n");
    }

    @Override
    public void visitSynopsisArgument(ArgumentModel<?> argument) {
        //<arg choice="opt">--out=REPO</arg>
        if (!hadFirstArgument) {
            docbook.open("arg choice=\"opt\"").text("--").close("arg").text("\n");
            hadFirstArgument = true;
        }
        
        String argTag = "arg";
        if (argument.getMultiplicity().isRequired()) {
            argTag += " choice=\"req\"";
        } else {
            argTag += " choice=\"opt\"";
        }
        
        if (argument.getMultiplicity().isMultivalued()) {
            argTag += " rep=\"repeat\"";
        }
        docbook.open(argTag).text(argument.getName()).close("arg").text("\n");
    }

    @Override
    public void visitSynopsisOption(OptionModel<?> option) {
        // TODO Should probably be using req=plain
        
        hadOptions = true;
        String argTag = "arg";
        final ArgumentModel<?> argument = option.getArgument();
        if (argument.getMultiplicity().isRequired()) {
            argTag += " choice=\"req\"";
        } else {
            argTag += " choice=\"opt\"";
        }
        
        docbook.open(argTag);
        
        if (option.getLongName() != null) {
            longOptionSynopsis(option);
            if (option.getArgumentType() == ArgumentType.REQUIRED) {
                docbook.text("=");
                multiplicity(argument, argument.getName());
            } else if (option.getArgumentType() == ArgumentType.OPTIONAL) {
                docbook.text("[=");
                multiplicity(argument, argument.getName());
                docbook.text("]");
            }
        } else {
            shortOptionSynopsis(option);
            if (option.getArgumentType() == ArgumentType.REQUIRED) {
                multiplicity(argument, argument.getName());
            }
        }
        
        docbook.close("arg").text("\n");
    }

    @Override
    public void endSynopses(SynopsesSection synopsesSection) {
        docbook.close("refsynopsisdiv").text("\n");
    }

    @Override
    public void visitDescription(DescribedSection descriptionSection) {
        describedSection(0, descriptionSection);
    }

    @Override
    public void visitSynopsisSubtool(SubtoolVisitor.ToolModelAndSubtoolModel option) {
        subtoolSynopsis(option);    
    }

}
