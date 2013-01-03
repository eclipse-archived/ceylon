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

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.tools.help.model.DescribedSection;
import com.redhat.ceylon.tools.help.model.Doc;
import com.redhat.ceylon.tools.help.model.Option;
import com.redhat.ceylon.tools.help.model.OptionsSection;
import com.redhat.ceylon.tools.help.model.SubtoolVisitor;
import com.redhat.ceylon.tools.help.model.SummarySection;
import com.redhat.ceylon.tools.help.model.SynopsesSection;
import com.redhat.ceylon.tools.help.model.Synopsis;
import com.redhat.ceylon.tools.help.model.Visitor;

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
