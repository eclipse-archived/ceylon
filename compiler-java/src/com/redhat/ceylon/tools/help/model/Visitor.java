package com.redhat.ceylon.tools.help.model;

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.SubtoolModel;
import com.redhat.ceylon.tools.help.model.Synopsis.NameAndSubtool;

public interface Visitor {

    public void start(Doc doc);

    public void end(Doc doc);

    public void visitAdditionalSection(DescribedSection describedSection);

    public void startOptions(OptionsSection optionsSection);

    public void visitOption(Option option);
    
    public void endOptions(OptionsSection optionsSection);
    
    public void visitSummary(SummarySection summarySection);

    public void startSynopses(SynopsesSection synopsesSection);
    
    public void startSynopsis(Synopsis synopsis);
    
    public void visitSynopsisOption(OptionModel<?> option);
    
    public void visitSynopsisArgument(ArgumentModel<?> option);
    
    public void visitSynopsisSubtool(NameAndSubtool option);
    
    public void endSynopsis(Synopsis synopsis);
    
    public void endSynopses(SynopsesSection synopsesSection);

    public void visitDescription(DescribedSection descriptionSection);

    



}
