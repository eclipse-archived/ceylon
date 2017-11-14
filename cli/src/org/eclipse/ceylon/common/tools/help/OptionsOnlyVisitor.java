/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools.help;

import java.util.Set;

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

public class OptionsOnlyVisitor implements Visitor {

    private final Visitor visitor;
    private final Set<String> longOptionNames;

    public OptionsOnlyVisitor(Visitor visitor, Set<String> longOptionNames) {
        super();
        this.visitor = visitor;
        this.longOptionNames = longOptionNames;
    }

    public void start(Doc doc) {
        visitor.start(doc);
    }

    public void end(Doc doc) {
        visitor.end(doc);
    }

    public void visitOption(Option option) {
        if (longOptionNames == null || longOptionNames.contains(option.getOption().getLongName())) {
            visitor.visitOption(option);
        }
    }
    
    public void visitAdditionalSection(DescribedSection describedSection) {
        // Only visiting synopsis
    }

    public void startOptions(OptionsSection optionsSection) {
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

    public void startSynopsis(Synopsis synopsis) {
        // Only visiting synopsis
    }

    public void visitSynopsisOption(OptionModel<?> option) {
        // Only visiting synopsis
    }

    public void visitSynopsisArgument(ArgumentModel<?> option) {
        // Only visiting synopsis
    }

    public void visitSynopsisSubtool(SubtoolVisitor.ToolModelAndSubtoolModel option) {
        // Only visiting synopsis
    }

    public void endSynopsis(Synopsis synopsis) {
        // Only visiting synopsis
    }

    public void endSynopses(SynopsesSection synopsesSection) {
        // Only visiting synopsis
    }

    public void visitDescription(DescribedSection descriptionSection) {
        // Only visiting synopsis
    }
    
    
    
}
