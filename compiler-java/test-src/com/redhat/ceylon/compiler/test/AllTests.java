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
package com.redhat.ceylon.compiler.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.compiler.test.expression.ExpressionTest;
import com.redhat.ceylon.compiler.test.interop.InteropTest;
import com.redhat.ceylon.compiler.test.issues.IssuesTest;
import com.redhat.ceylon.compiler.test.issues.PackageIssuesTest;
import com.redhat.ceylon.compiler.test.misc.MiscTest;
import com.redhat.ceylon.compiler.test.model.AnnotationsTest;
import com.redhat.ceylon.compiler.test.model.ModelLoaderTest;
import com.redhat.ceylon.compiler.test.model.TypeParserTest;
import com.redhat.ceylon.compiler.test.recovery.RecoveryTest;
import com.redhat.ceylon.compiler.test.statement.StatementTest;
import com.redhat.ceylon.compiler.test.structure.StructureTest;

@RunWith(Suite.class) 
@SuiteClasses({
    AnnotationsTest.class,
    ExpressionTest.class,
    InteropTest.class,
    IssuesTest.class,
	MiscTest.class,
    ModelLoaderTest.class,
    PackageIssuesTest.class,
    RecoveryTest.class,
    StatementTest.class,
	StructureTest.class,
	TypeParserTest.class
})
public class AllTests {
}
