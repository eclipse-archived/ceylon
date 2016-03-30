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
package com.redhat.ceylon.tools.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.tools.new_.NewProjectToolTests;
import com.redhat.ceylon.tools.new_.TemplateTests;
import com.redhat.ceylon.tools.test.ImportJarToolTests;
import com.redhat.ceylon.tools.version.VersionToolTests;
import com.redhat.ceylon.tools.bashcompletion.BashCompletionToolTests;
import com.redhat.ceylon.tools.help.DocToolToolTests;
import com.redhat.ceylon.tools.help.HelpToolTests;
import com.redhat.ceylon.tools.help.MarkdownTests;
import com.redhat.ceylon.tools.help.PlaintextTests;

@RunWith(Suite.class)
@SuiteClasses({
    BashCompletionToolTests.class,
    CompilerToolTests.class,
    DocToolTests.class,
    ImportJarToolTests.class,
    PlaintextTests.class,
    MarkdownTests.class,
    HelpToolTests.class,
    DocToolToolTests.class,
    NewProjectToolTests.class,
    TemplateTests.class,
    InfoToolTests.class,
    SrcToolTests.class,
    VersionToolTests.class,
    PluginToolTests.class,
    CopyToolTests.class,
    JigsawToolTests.class
})
public class CompilerToolsTests {

}
