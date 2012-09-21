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
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Subtool;
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
    
    private Tool action;
    
    private String file;
    
    @OptionArgument(argumentName="file")
    @Description("The file to operate on. Default: `./.ceylon/config`)")
    public void setFile(String file) {
        this.file = file;
    }
    
    @Subtool(argumentName="action",
            classes={Get.class, Set.class, Unset.class, RenameSection.class, RemoveSection.class, Keystore.class})
    public void setAction(Tool action) {
        this.action = action;
    }

    @Summary("Gets a value in a config file")
    public class Get implements Tool {
    
        @Override
        public void run() {
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
        }
    }
    
    @Summary("Sets a value in a config file")
    public class Set implements Tool {
        @Override
        public void run() {
        }
    }
    
    @Summary("Unsets a value in a config file")
    public class Unset implements Tool {
        @Override
        public void run() {
        }
    }
    
    @Summary("Renames a section in a config file")
    public class RenameSection implements Tool {
        @Override
        public void run() {
        }
    }
    
    @Summary("Removes a section from a config file")
    public class RemoveSection implements Tool {
        @Override
        public void run() {
        }
    }
    
    @Summary("Modifies keystores")
    public class Keystore implements Tool {
        private Tool tool;

        private String storePassword;
        
        @OptionArgument
        @Option
        public void setStorePassword(String storePassword) {
            this.storePassword = storePassword;
        }
        
        @Summary("Gets a password in a keystore")
        public class GetPassword implements Tool {
            
            private String alias;
            
            @Argument(argumentName="alias", multiplicity="1", order=1)
            public void setAlias(String alias) {
                this.alias = alias;
            }
            
            @Override
            public void run() throws Exception {
                // TODO Auto-generated method stub
                
            }
        }
        
        @Summary("Sets a password in a keystore")
        public class SetPassword implements Tool {
            
            private String alias;
            
            private String password;
            
            @Argument(argumentName="alias", multiplicity="1", order=1)
            public void setAlias(String alias) {
                this.alias = alias;
            }
            
            @Argument(argumentName="password", multiplicity="?", order=2)
            public void setPassword(String password) {
                this.password = password;
            }
            
            @Override
            public void run() throws Exception {
                // TODO Auto-generated method stub
                
            }
        }
        
        @Summary("Unsets a password in a keystore")
        public class UnsetPassword implements Tool {
            
            private String alias;
            
            @Argument(argumentName="alias", multiplicity="1", order=1)
            public void setAlias(String alias) {
                this.alias = alias;
            }
            
            @Override
            public void run() throws Exception {
                // TODO Auto-generated method stub
                
            }
        }
        
        @Subtool(argumentName="action", classes={GetPassword.class, SetPassword.class, UnsetPassword.class})
        public void setAction(Tool action) {
            this.tool = tool;
        }
        @Override
        public void run() throws Exception {
            tool.run();
        }
    }
    
    @Override
    public void run() throws Exception {
        action.run();
    }

    public static void main(String[] args) throws Exception {
        CeylonConfigTool x = new CeylonConfigTool();
        x.action = x.new Get();
        x.run();
    }
    
}
