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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.ToolModel;


public class Doc implements Documentation {

    private ToolModel<?> toolModel;
    
    private String version;
    
    private String invocation;
    
    private SummarySection summary;
    
    private SynopsesSection synopses;
    
    private DescribedSection description;
    
    private OptionsSection options;
    
    private List<DescribedSection> additionalSections = Collections.emptyList();

    public ToolModel<?> getToolModel() {
        return toolModel;
    }

    public void setToolModel(ToolModel<?> toolModel) {
        this.toolModel = toolModel;
    }

    public String getName() {
        return getToolModel().getName();
    }
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInvocation() {
        return invocation;
    }

    public void setInvocation(String invocation) {
        this.invocation = invocation;
    }


    public List<Documentation> getSections() {
        ArrayList<Documentation> result = new ArrayList<Documentation>(additionalSections.size() + 4);
        if (summary != null) {
            result.add(summary);
        }
        result.add(synopses);
        if (description != null) {
            result.add(description);
        }
        result.add(options);
        result.addAll(additionalSections);
        return result;
    }

    public SummarySection getSummary() {
        return summary;
    }

    public void setSummary(SummarySection summary) {
        this.summary = summary;
    }

    public SynopsesSection getSynopses() {
        return synopses;
    }

    public void setSynopses(SynopsesSection synopses) {
        this.synopses = synopses;
    }

    public DescribedSection getDescription() {
        return description;
    }

    public void setDescription(DescribedSection description) {
        this.description = description;
    }

    public OptionsSection getOptions() {
        return options;
    }

    public void setOptions(OptionsSection options) {
        this.options = options;
    }

    public List<DescribedSection> getAdditionalSections() {
        return additionalSections;
    }

    public void setAdditionalSections(List<DescribedSection> additionalSections) {
        this.additionalSections = additionalSections;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.start(this);
        if (summary != null) {
            summary.accept(visitor);
        }
        if (synopses != null) {
            synopses.accept(visitor);
        }
        if (description != null) {
            description.accept(visitor);
        }
        if (options != null) {
            options.accept(visitor);
        }
        for (DescribedSection section : additionalSections) {
            section.accept(visitor);
        }
        visitor.end(this);
    }

    
}
