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

import java.io.File;
import java.io.IOException;

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
    
    private File file;
    
    @OptionArgument(argumentName="file")
    @Description("The file to operate on.")
    public void setFile(File file) {
        this.file = file;
    }
    
    @Subtool(argumentName="action",
            classes={List.class, Get.class, Set.class, Unset.class, RenameSection.class, RemoveSection.class, Keystore.class})
    public void setAction(Tool action) {
        this.action = action;
    }

    private CeylonConfig getConfig() throws IOException {
        if (file != null) {
            return ConfigParser.loadConfigFromFile(file);
        } else {
            return CeylonConfig.get();
        }
    }
    
    @Description("Lists configuration values")
    public class List implements Tool {
    
        @Override
        public void run() throws IOException {
            CeylonConfig config = getConfig();
            System.out.print(config.toString());
        }
    }

    @Description("Get the value defined for `<key>` in the config file")
    public class Get implements Tool {
    
        private String key;
        
        @Argument(argumentName="key", multiplicity="1")
        public void setKey(String key) {
            this.key = key;
        }
        
        @Override
        public void run() throws IOException {
            CeylonConfig config = getConfig();
            String[] values = config.getOptionValues(key);
            if (values != null) {
                for (String value : values) {
                    System.out.println(ConfigWriter.escape(value));
                }
            }
        }
    }
    
    @Description("Set the value of the `<key>` to `<value>` in the config file")
    public class Set implements Tool {
        @Argument(argumentName="key", multiplicity="1", order=1)
        public void setKey(String key) {
            
        }
        
        @Argument(argumentName="value", multiplicity="1", order=2)
        public void setValue(String value) {
            
        }
        
        @Override
        public void run() {
        }
    }
    
    @Description("Unsets the value of the `<key>` in the config file")
    public class Unset implements Tool {
        
        @Argument(argumentName="key", multiplicity="1")
        public void setKey(String key) {
            
        }
        @Override
        public void run() {
        }
    }
    
    @Description("Renames the section `<old-name>` in the config file to `<new-name>`")
    public class RenameSection implements Tool {
        
        @Argument(argumentName="old-name", multiplicity="1", order=1)
        public void setOldName(String oldName) {
            
        }
        
        @Argument(argumentName="new-name", multiplicity="1", order=2)
        public void setNewName(String newName) {
            
        }
        
        @Override
        public void run() {
        }
    }
    
    @Description("Removes the named `<section>` from the config file")
    public class RemoveSection implements Tool {
        @Argument(argumentName="name", multiplicity="1", order=1)
        public void setSection(String section) {
            
        }
        
        @Override
        public void run() {
        }
    }
    
    @Description("Modifies keystores")
    public class Keystore implements Tool {
        private Tool tool;

        private String storePassword;
        
        @OptionArgument
        @Option
        @Description("The password for accessing the keystore")
        public void setStorePassword(String storePassword) {
            this.storePassword = storePassword;
        }
        
        @Description("Gets the password for `<alias>` in the keystore")
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
        
        @Description("Sets the password for `<alias>` in the keystore. " +
        		"The program will issue a password prompt if `<password>` is omitted.")
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
        
        @Description("Unsets the password for `<alias>` in the keystore, " +
        		"removing the alias and its corresponding password.")
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
