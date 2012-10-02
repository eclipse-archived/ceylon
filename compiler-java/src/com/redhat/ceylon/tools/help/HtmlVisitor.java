package com.redhat.ceylon.tools.help;

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.OptionModel.ArgumentType;
import com.redhat.ceylon.tools.help.model.DescribedSection;
import com.redhat.ceylon.tools.help.model.Doc;
import com.redhat.ceylon.tools.help.model.Option;
import com.redhat.ceylon.tools.help.model.OptionsSection;
import com.redhat.ceylon.tools.help.model.SynopsesSection;
import com.redhat.ceylon.tools.help.model.Synopsis;
import com.redhat.ceylon.tools.help.model.Synopsis.NameAndSubtool;
import com.redhat.ceylon.tools.help.model.Visitor;

public class HtmlVisitor implements Visitor {

    private final Html html;
    private boolean hadFirstArgument;
    private boolean hadOptions;
    
    HtmlVisitor(Appendable out) {
        this.html = new Html(out);
    }
    
    Html getHtml() {
        return html;
    }
    
    @Override
    public void start(Doc doc) {
        html.open("html", "head");
        html.open("title").text(doc.getInvocation()).close("title").text("\n");
        html.tag("link rel='stylesheet'type='text/css' href='doc-tool.css'").text("\n");
        html.close("head").text("\n");
        html.open("body").text("\n");
        html.open("h1").text(doc.getInvocation()).close("h1");
        html.text("\n");
    }

    @Override
    public void end(Doc doc) {
        html.close("body", "html");
    }

    @Override
    public void visitAdditionalSection(DescribedSection describedSection) {
        describedSection(describedSection);
    }

    private void describedSection(DescribedSection describedSection) {
        html.open("div class='section'");
        html.text("\n");
        html.markdown(describedSection.getDescription());
        html.close("div").text("\n\n");
    }

    @Override
    public void startOptions(OptionsSection optionsSection) {
        html.open("div class='section section-options'").text("\n");
        html.open("h2").text(optionsSection.getTitle()).close("h2").text("\n");
        html.open("dl");
    }

    @Override
    public void visitOption(Option option) {
        String longName = option.getLongName();
        String shortName = option.getShortName();
        String argumentName = option.getArgumentName();
        html.open("dt class='option'");
        html.open("code id='long" + longName + "'").text(longName);
        if (argumentName != null) {
            html.text("=").text(argumentName);
        }
        html.close("code");
        if (shortName != null) {
            html.text(", ");
            html.open("code id='short" + shortName +"'").text(shortName);
            if (argumentName != null) {
                html.text(" ").text(argumentName);
            }
            html.close("code");
        }
        html.close("dt").text("\n");
        html.open("dd class='option-description'");
        html.markdown(option.getDescription());
        html.close("dd").text("\n");
    }

    @Override
    public void endOptions(OptionsSection optionsSection) {
        html.close("dl").text("\n");
        html.close("div").text("\n\n");
    }

    @Override
    public void visitSummary(DescribedSection summarySection) {
        describedSection(summarySection);
    }

    @Override
    public void startSynopses(SynopsesSection synopsesSection) {
        html.open("div class='section section-synopsis'").text("\n");
        html.open("h2").text(synopsesSection.getTitle()).close("h2").text("\n");        
    }
    
    private void longOptionSynopsis(String string) {
        html.link(string, "#long" + string);
    }
    
    private void shortOptionSynopsis(String string) {
        html.link(string, "#short" + string);
    }

    private void argumentSynopsis(String name) {
        html.link(name, "#arg" + name);
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
        html.open("div class='synopsis'", "code");
    }

    @Override
    public void endSynopsis(Synopsis synopsis) {
        html.close("code", "div").text("\n");   
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
        html.text("<");
        argumentSynopsis(name);
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
            longOptionSynopsis("--" + option.getLongName());
            if (option.getArgumentType() == ArgumentType.REQUIRED) {
                html.text("=");
                html.text(multiplicity(argument, argument.getName()));
            } else if (option.getArgumentType() == ArgumentType.OPTIONAL) {
                html.text("[=");
                html.text(multiplicity(argument, argument.getName()));
                html.text("]");
            }
        } else {
            shortOptionSynopsis("-" + option.getShortName());
            if (option.getArgumentType() != ArgumentType.NOT_ALLOWED) {
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
        html.close("div").text("\n\n");
    }

    @Override
    public void visitDescription(DescribedSection descriptionSection) {
        describedSection(descriptionSection);
    }

    @Override
    public void visitSynopsisSubtool(NameAndSubtool option) {
        // TODO Auto-generated method stub
        
    }

}
