
package org.eclipse.ceylon.common.tools.help;

import org.eclipse.ceylon.common.tool.ArgumentModel;
import org.eclipse.ceylon.common.tool.OptionModel;
import org.eclipse.ceylon.common.tools.help.model.DescribedSection;
import org.eclipse.ceylon.common.tools.help.model.Doc;
import org.eclipse.ceylon.common.tools.help.model.Option;
import org.eclipse.ceylon.common.tools.help.model.OptionsSection;
import org.eclipse.ceylon.common.tools.help.model.SubtoolVisitor;
import org.eclipse.ceylon.common.tools.help.model.SummarySection;
import org.eclipse.ceylon.common.tools.help.model.SynopsesSection;
import org.eclipse.ceylon.common.tools.help.model.Synopsis;
import org.eclipse.ceylon.common.tools.help.model.Visitor;

public class SynopsisOnlyVisitor implements Visitor {

    private final Visitor visitor;

    public SynopsisOnlyVisitor(Visitor visitor) {
        super();
        this.visitor = visitor;
    }

    public void start(Doc doc) {
        visitor.start(doc);
    }

    public void end(Doc doc) {
        visitor.end(doc);
    }
    
    public void startSynopsis(Synopsis synopsis) {
        visitor.startSynopsis(synopsis);
    }

    public void visitSynopsisOption(OptionModel<?> option) {
        visitor.visitSynopsisOption(option);
    }

    public void visitSynopsisArgument(ArgumentModel<?> option) {
        visitor.visitSynopsisArgument(option);
    }

    public void visitSynopsisSubtool(SubtoolVisitor.ToolModelAndSubtoolModel option) {
        visitor.visitSynopsisSubtool(option);
    }

    public void endSynopsis(Synopsis synopsis) {
        visitor.endSynopsis(synopsis);
    }

    public void visitAdditionalSection(DescribedSection describedSection) {
        // Only visiting synopsis
    }

    public void startOptions(OptionsSection optionsSection) {
        // Only visiting synopsis
    }

    public void visitOption(Option option) {
        // Only visiting synopsis
    }

    public void endOptions(OptionsSection optionsSection) {
        // Only visiting synopsis
    }

    public void visitSummary(SummarySection summarySection) {
        // Only visiting synopsis
    }

    public void startSynopses(SynopsesSection synopsesSection) {
        // Only visiting synopsis
    }

    public void endSynopses(SynopsesSection synopsesSection) {
        // Only visiting synopsis
    }

    public void visitDescription(DescribedSection descriptionSection) {
        // Only visiting synopsis
    }
    
    
    
}
