package com.redhat.ceylon.tools.help.model;

public interface Visitor {

    public void start(Doc doc);

    public void end(Doc doc);

    public void visitAdditionalSection(DescribedSection describedSection);

    public void startOptions(OptionsSection optionsSection);

    public void visitOption(Option option);
    
    public void endOptions(OptionsSection optionsSection);
    
    public void visitSummary(DescribedSection summarySection);

    public void startSynopses(SynopsesSection synopsesSection);
    
    public void visitSynopsis(Synopsis synopsis);
    
    public void endSynopses(SynopsesSection synopsesSection);

    public void visitDescription(DescribedSection descriptionSection);

    



}
