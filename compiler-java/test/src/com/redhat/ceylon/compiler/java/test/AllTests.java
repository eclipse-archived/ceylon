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
package com.redhat.ceylon.compiler.java.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.ant.AntToolTests;
import com.redhat.ceylon.ceylondoc.test.CeylonDocToolTests;
import com.redhat.ceylon.compiler.java.codegen.NamingTests;
import com.redhat.ceylon.compiler.java.test.annotations.AnnotationsTests;
import com.redhat.ceylon.compiler.java.test.bc.BcTests;
import com.redhat.ceylon.compiler.java.test.cargeneration.CarGenerationTests;
import com.redhat.ceylon.compiler.java.test.cmr.CMRTests;
import com.redhat.ceylon.compiler.java.test.cmr.CMRHTTPTests;
import com.redhat.ceylon.compiler.java.test.expression.ExpressionTests;
import com.redhat.ceylon.compiler.java.test.expression.ExpressionTests2;
import com.redhat.ceylon.compiler.java.test.expression.comprehensions.ComprehensionTests;
import com.redhat.ceylon.compiler.java.test.expression.ref.StaticRefTests;
import com.redhat.ceylon.compiler.java.test.fordebug.SourcePositionsTests;
import com.redhat.ceylon.compiler.java.test.fordebug.TraceTests;
import com.redhat.ceylon.compiler.java.test.interop.InteropTests;
import com.redhat.ceylon.compiler.java.test.issues.IssuesTests_0000_0499;
import com.redhat.ceylon.compiler.java.test.issues.IssuesTests_0500_0999;
import com.redhat.ceylon.compiler.java.test.issues.IssuesTests_1000_1499;
import com.redhat.ceylon.compiler.java.test.issues.IssuesTests_1500_1999;
import com.redhat.ceylon.compiler.java.test.issues.IssuesTests_2000_2499;
import com.redhat.ceylon.compiler.java.test.issues.PackageIssuesTests;
import com.redhat.ceylon.compiler.java.test.language.LanguageSuite;
import com.redhat.ceylon.compiler.java.test.languagesatisfaction.LanguageSatisfactionSuite;
import com.redhat.ceylon.compiler.java.test.metamodel.MetamodelTests;
import com.redhat.ceylon.compiler.java.test.misc.MiscTests;
import com.redhat.ceylon.compiler.java.test.model.ModelLoaderTests;
import com.redhat.ceylon.compiler.java.test.model.TypeParserTests;
import com.redhat.ceylon.compiler.java.test.model.ValueTypeTests;
import com.redhat.ceylon.compiler.java.test.nativecode.NativeTests;
import com.redhat.ceylon.compiler.java.test.quoting.QuotingTests;
import com.redhat.ceylon.compiler.java.test.recovery.RecoveryTests;
import com.redhat.ceylon.compiler.java.test.reporting.ReportingTests;
import com.redhat.ceylon.compiler.java.test.runtime.RuntimeSuite;
import com.redhat.ceylon.compiler.java.test.statement.OptimizationTests;
import com.redhat.ceylon.compiler.java.test.statement.StatementTests;
import com.redhat.ceylon.compiler.java.test.statement.TryCatchTests;
import com.redhat.ceylon.compiler.java.test.structure.ConstructorTests;
import com.redhat.ceylon.compiler.java.test.structure.StructureTests;
import com.redhat.ceylon.compiler.java.test.structure.StructureTests2;
import com.redhat.ceylon.compiler.java.test.structure.StructureTests3;
import com.redhat.ceylon.tools.TopLevelToolTests;
import com.redhat.ceylon.tools.test.CompilerToolsTests;

@RunWith(Suite.class) 
@SuiteClasses({
    ExpressionTests.class,
    ExpressionTests2.class,
    StructureTests.class,
    StructureTests2.class,
    StructureTests3.class,
    CMRHTTPTests.class,
    IssuesTests_0000_0499.class,
    IssuesTests_0500_0999.class,
    IssuesTests_1000_1499.class,
    IssuesTests_1500_1999.class,
    IssuesTests_2000_2499.class,
    MiscTests.class,
    CeylonDocToolTests.class,
    CompilerToolsTests.class,
    
    NamingTests.class,
    ConstructorTests.class,
    AnnotationsTests.class,
    InteropTests.class,
    ComprehensionTests.class,
    ModelLoaderTests.class,
    ValueTypeTests.class,
    PackageIssuesTests.class,
    RecoveryTests.class,
    StatementTests.class,
    OptimizationTests.class,
    TryCatchTests.class,
    TypeParserTests.class,
    QuotingTests.class,
    CMRTests.class,
    RuntimeSuite.class,
    MetamodelTests.class,
    LanguageSatisfactionSuite.class,
    LanguageSuite.class,
    BcTests.class,
    ComprehensionTests.class,
    StaticRefTests.class,
    TopLevelToolTests.class,
    AntToolTests.class,
    TraceTests.class,
    CarGenerationTests.class,
    ReportingTests.class,
    SourcePositionsTests.class,
    NativeTests.class,
})
public class AllTests {
}
