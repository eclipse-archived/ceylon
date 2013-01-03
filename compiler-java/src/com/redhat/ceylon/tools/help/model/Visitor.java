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
package com.redhat.ceylon.tools.help.model;

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.OptionModel;

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
    
    public void visitSynopsisSubtool(SubtoolVisitor.ToolModelAndSubtoolModel option);
    
    public void endSynopsis(Synopsis synopsis);
    
    public void endSynopses(SynopsesSection synopsesSection);

    public void visitDescription(DescribedSection descriptionSection);

    



}
