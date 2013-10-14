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

import com.redhat.ceylon.tools.new_.NewProjectToolTest;
import com.redhat.ceylon.tools.new_.TemplateTest;
import com.redhat.ceylon.tools.test.ImportJarToolTest;
import com.redhat.ceylon.tools.version.VersionToolTest;
import com.redhat.ceylon.tools.bashcompletion.BashCompletionToolTest;
import com.redhat.ceylon.tools.help.DocToolToolTest;
import com.redhat.ceylon.tools.help.HelpToolTest;
import com.redhat.ceylon.tools.help.MarkdownTest;
import com.redhat.ceylon.tools.help.PlaintextTest;

@RunWith(Suite.class)
@SuiteClasses({
    BashCompletionToolTest.class,
    CompilerToolTest.class,
    DocToolTest.class,
    ImportJarToolTest.class,
    PlaintextTest.class,
    MarkdownTest.class,
    HelpToolTest.class,
    DocToolToolTest.class,
    NewProjectToolTest.class,
    TemplateTest.class,
    InfoToolTest.class,
    SrcToolTest.class,
    VersionToolTest.class
})
public class CompilerToolsTests {

}
