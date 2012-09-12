/*
 * Copyright 2012 Red Hat inc. and third party contributors as noted
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.common.config;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;

@Summary("Manages Ceylon configuration files")
@Description(
"Can be used to list, update and remove settings in Ceylon's configuration files."
)
@RemainingSections(
"##EXAMPLE\n" +
"\n" +
"The following would list the settings active from within the current folder:\n" +
"\n" +
"    ceylon config list"
)
public class CeylonConfigTool implements Tool {
    
    private String action;
    
    @Argument(argumentName="action", multiplicity="1")
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public void run() {
        if ("list".equals(action)) {
            CeylonConfig config = CeylonConfig.get();
            for (String sectionName : config.getSectionNames(null)) {
                String[] names = config.getOptionNames(sectionName);
                if (names != null && names.length > 0) {
                    System.out.println("[" + sectionName + "]");
                    for (String optionName : names) {
                        String[] values = config.getOptionValues(sectionName + "." + optionName);
                        if (values != null) {
                            for (String value : values) {
                                System.out.println(optionName + "=" + value);
                            }
                        }
                    }
                    System.out.println();
                }
            }
        } else {
            System.err.println("Unknown action '" + action + "'");
        }
    }

    public static void main(String[] args) {
        CeylonConfigTool x = new CeylonConfigTool();
        x.action = "list";
        x.run();
    }
    
}
