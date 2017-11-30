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
package org.eclipse.ceylon.compiler.java.test;

import org.eclipse.ceylon.ant.AntToolTests;
import org.eclipse.ceylon.ceylondoc.test.CeylonDocToolTests;
import org.eclipse.ceylon.compiler.java.codegen.NamingTests;
import org.eclipse.ceylon.compiler.java.test.annotations.AnnotationsTests;
import org.eclipse.ceylon.compiler.java.test.bc.BcTests;
import org.eclipse.ceylon.compiler.java.test.cargeneration.CarGenerationTests;
import org.eclipse.ceylon.compiler.java.test.cmr.CMRHTTPTests;
import org.eclipse.ceylon.compiler.java.test.cmr.CMRTests;
import org.eclipse.ceylon.compiler.java.test.compat.CompatTests;
import org.eclipse.ceylon.compiler.java.test.expression.ExpressionTests;
import org.eclipse.ceylon.compiler.java.test.expression.ExpressionTests2;
import org.eclipse.ceylon.compiler.java.test.expression.comprehensions.ComprehensionTests;
import org.eclipse.ceylon.compiler.java.test.expression.ref.StaticRefTests;
import org.eclipse.ceylon.compiler.java.test.fordebug.SourcePositionsTests;
import org.eclipse.ceylon.compiler.java.test.fordebug.TraceTests;
import org.eclipse.ceylon.compiler.java.test.interop.InteropTests;
import org.eclipse.ceylon.compiler.java.test.issues.IssuesTests_0000_0499;
import org.eclipse.ceylon.compiler.java.test.issues.IssuesTests_0500_0999;
import org.eclipse.ceylon.compiler.java.test.issues.IssuesTests_1000_1499;
import org.eclipse.ceylon.compiler.java.test.issues.IssuesTests_1500_1999;
import org.eclipse.ceylon.compiler.java.test.issues.IssuesTests_2000_2499;
import org.eclipse.ceylon.compiler.java.test.issues.IssuesTests_5500_5999;
import org.eclipse.ceylon.compiler.java.test.issues.IssuesTests_6000_6499;
import org.eclipse.ceylon.compiler.java.test.issues.PackageIssuesTests;
import org.eclipse.ceylon.compiler.java.test.language.LanguageSuite;
import org.eclipse.ceylon.compiler.java.test.languagesatisfaction.LanguageSatisfactionSuite;
import org.eclipse.ceylon.compiler.java.test.metamodel.MetamodelTests;
import org.eclipse.ceylon.compiler.java.test.misc.MiscTests;
import org.eclipse.ceylon.compiler.java.test.model.ModelLoaderTests;
import org.eclipse.ceylon.compiler.java.test.model.TypeParserTests;
import org.eclipse.ceylon.compiler.java.test.model.ValueTypeTests;
import org.eclipse.ceylon.compiler.java.test.nativecode.NativeTests;
import org.eclipse.ceylon.compiler.java.test.quoting.QuotingTests;
import org.eclipse.ceylon.compiler.java.test.recovery.RecoveryTests;
import org.eclipse.ceylon.compiler.java.test.reporting.ReportingTests;
import org.eclipse.ceylon.compiler.java.test.runtime.RuntimeSuite;
import org.eclipse.ceylon.compiler.java.test.statement.OptimizationTests;
import org.eclipse.ceylon.compiler.java.test.statement.StatementTests;
import org.eclipse.ceylon.compiler.java.test.statement.TryCatchTests;
import org.eclipse.ceylon.compiler.java.test.structure.ConstructorTests;
import org.eclipse.ceylon.compiler.java.test.structure.SerializableTests;
import org.eclipse.ceylon.compiler.java.test.structure.StaticTests;
import org.eclipse.ceylon.compiler.java.test.structure.StructureTests;
import org.eclipse.ceylon.compiler.java.test.structure.StructureTests2;
import org.eclipse.ceylon.compiler.java.test.structure.StructureTests3;
import org.eclipse.ceylon.compiler.java.test.structure.ee.EeTests;
import org.eclipse.ceylon.launcher.test.BootstrapTests;
import org.eclipse.ceylon.tools.TopLevelToolTests;
import org.eclipse.ceylon.tools.test.CompilerToolsTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class) 
@SuiteClasses({
    ExpressionTests.class,
    ExpressionTests2.class,
    StructureTests.class,
    StructureTests2.class,
    StructureTests3.class,
    SerializableTests.class,
    CMRHTTPTests.class,
    IssuesTests_0000_0499.class,
    IssuesTests_0500_0999.class,
    IssuesTests_1000_1499.class,
    IssuesTests_1500_1999.class,
    IssuesTests_2000_2499.class,
    IssuesTests_5500_5999.class,
    IssuesTests_6000_6499.class,
    MiscTests.class,
    CeylonDocToolTests.class,
    CompilerToolsTests.class,

    NamingTests.class,
    StaticTests.class,
    ConstructorTests.class,
    AnnotationsTests.class,
    InteropTests.class,
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
    CompatTests.class,
    ComprehensionTests.class,
    StaticRefTests.class,
    AntToolTests.class,
    TraceTests.class,
    CarGenerationTests.class,
    ReportingTests.class,
    SourcePositionsTests.class,
    NativeTests.class,
    BootstrapTests.class,
    EeTests.class,
    TopLevelToolTests.class,
    // Unable to run due to OOMs
    // IntegrationTests.class,
})
public class AllCompilerTests {
}
