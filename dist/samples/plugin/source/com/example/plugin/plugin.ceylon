/*
 * Copyright 2011 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.redhat.ceylon.common.tool {
    CeylonBaseTool,
    summary,
    description,
    description__SETTER,
    argument__SETTER,
    option__SETTER,
    optionArgument__SETTER }
import java.lang { JString=String }
import java.util { JList=List }

summary("Just a test tool")
description("""And a somewhat larger description,
               that can span multiple lines.
               """)
shared class CeylonExamplePluginTool() extends CeylonBaseTool() {
    
    description__SETTER("A simple string option, use `--string-option=foobar`")
    optionArgument__SETTER shared variable JString? stringOption = null;

    description__SETTER("A simple flag, use `--simple-flag`")
    option__SETTER shared variable Boolean simpleFlag = false;
    
    argument__SETTER{multiplicity="*";} shared variable JList<JString>? arguments = null;
    
    shared actual void run() {
        print("Example plugin executed correctly!");
        if (simpleFlag) {
            print("The option --simple-flag is set.");
        }
        if (exists v=stringOption) {
            print("The argument for option --string-option is '``v``'");
        }
        if (exists v=arguments) {
            print("The following arguments were passed: ``v``");
        }
        if (exists v=verbose) {
            print("Verbose mode was activated and set to: '``v``'");
        }
        if (exists v=cwd) {
            print("The current working directory is set to: '``v``'");
        }
        if (!simpleFlag && !stringOption exists && !arguments exists && !verbose exists && !cwd exists) {
            print("No options or arguments were passed, try typing `ceylon example-plugin --help`");
        }
    }
    
}

shared void run() {
    print("This is a Ceylon tool plugin, it shouldn't be run as a Ceylon program!");
}

