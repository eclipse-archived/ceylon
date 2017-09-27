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
package org.eclipse.ceylon.tools.test;

import org.eclipse.ceylon.tools.bashcompletion.BashCompletionToolTests;
import org.eclipse.ceylon.tools.help.DocToolToolTests;
import org.eclipse.ceylon.tools.help.HelpToolTests;
import org.eclipse.ceylon.tools.help.MarkdownTests;
import org.eclipse.ceylon.tools.help.PlaintextTests;
import org.eclipse.ceylon.tools.new_.NewProjectToolTests;
import org.eclipse.ceylon.tools.new_.TemplateTests;
import org.eclipse.ceylon.tools.version.VersionToolTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    BashCompletionToolTests.class,
    ClasspathToolTests.class,
    CompilerToolTests.class,
    CopyToolTests.class,
    DocToolTests.class,
    DocToolToolTests.class,
    FatJarToolTests.class,
    HelpToolTests.class,
    ImportJarToolTests.class,
    InfoToolTests.class,
    JigsawToolTests.class,
    MarkdownTests.class,
    MavenExportToolTests.class,
    NewProjectToolTests.class,
    PlaintextTests.class,
    PluginToolTests.class,
    SrcToolTests.class,
    TemplateTests.class,
    VersionToolTests.class,
    WarToolTests.class,
})
public class CompilerToolsTests {

}
