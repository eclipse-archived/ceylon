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
import java.lang { JDeprecated = Deprecated, jdeprecated = deprecated }
import java.lang.annotation { documented }
import ceylon.language.meta.declaration { ... }

annotation final class Bar(a) satisfies OptionalAnnotation<Bar, ClassDeclaration> {
    shared String a;
    void method(){}
}

annotation Bar bar(String a) => Bar(a);

bar("a")
deprecated
annoOnType
class Foo() {
    
}

void test(AnnoOnMethod onMethod, AnnoOnField onField, AnnoOnConstructor onConstructor, AnnoOnType onType,
    AnnoOnPackage onPackage,
    // can only be tested on Java 8 
    // AnnoOnTypeUse onTypeUse, AnnoOnTypeParameter onTypeParameter, 
    AnnoOnAnnotationType onAnnotationType,
    AnnoOnLocalVariable onLocalVariable, AnnoOnParameter onParameter){
    
    ConstrainedAnnotation<AnnoOnMethod,AnnoOnMethod?,FunctionDeclaration> onMethodAnno = onMethod;
    ConstrainedAnnotation<AnnoOnConstructor,AnnoOnConstructor?,ConstructorDeclaration> onConstructorAnno = onConstructor;
    ConstrainedAnnotation<AnnoOnType,AnnoOnType?,ClassOrInterfaceDeclaration|AliasDeclaration> onTypeAnno = onType;
    ConstrainedAnnotation<AnnoOnField,AnnoOnField?,ValueDeclaration> onFieldAnno = onField;

    ConstrainedAnnotation<AnnoOnPackage,AnnoOnPackage?,Nothing> onPackageAnno = onPackage;
    // can only be tested on Java 8
    /*
    ConstrainedAnnotation<AnnoOnTypeUse,AnnoOnTypeUse?,Nothing> onTypeUseAnno = onTypeUse;
    ConstrainedAnnotation<AnnoOnTypeParameter,AnnoOnTypeParameter?,Nothing> onTypeParameterAnno = onTypeParameter;
    */
    
    ConstrainedAnnotation<AnnoOnAnnotationType,AnnoOnAnnotationType?,ClassOrInterfaceDeclaration> onAnnotationTypeAnno = onAnnotationType;
    ConstrainedAnnotation<AnnoOnLocalVariable,AnnoOnLocalVariable?,ValueDeclaration> onLocalVariableAnno = onLocalVariable;
    ConstrainedAnnotation<AnnoOnParameter,AnnoOnParameter?,FunctionOrValueDeclaration> onParameterAnno = onParameter;
}

void run(){
    assert(`class Foo`.annotations<Annotation>().size == 3);
    assert(`class Foo`.annotations<DeprecationAnnotation>().size == 1);
    assert(`class Foo`.annotations<Bar>().size == 1);
    assert(`class Foo`.annotations<AnnoOnType>().size == 1);
    
    // Now sealed so we can't instantiate it
    //DeprecationAnnotation("a");
    deprecated("a");
    Bar("a");
    bar("a");
    //AnnoOnType(); type cannot be instantiated: 'AnnoOnType' is not a class OK
    // FIXME: cannot instantiate
    //annoOnType();

    assert(exists bar = `class Foo`.annotations<Bar>()[0]);
    assert(exists annoOnType = `class Foo`.annotations<AnnoOnType>()[0]);
    Object barObj = bar;
    Object annoOnTypeObj = annoOnType;
    
    assert(barObj is Annotation);
    assert(barObj is Bar);
    assert(barObj is ConstrainedAnnotation<Bar,Bar?,ClassDeclaration>);
    assert(!barObj is ConstrainedAnnotation<Bar,Bar?,FunctionDeclaration>);
    assert(!barObj is ConstrainedAnnotation<Bar,Bar?,Annotated>);

    assert(annoOnTypeObj is Annotation);
    assert(annoOnTypeObj is AnnoOnType);
    assert(annoOnTypeObj is ConstrainedAnnotation<AnnoOnType,AnnoOnType?,ClassOrInterfaceDeclaration>);
    assert(annoOnTypeObj is ConstrainedAnnotation<AnnoOnType,AnnoOnType?,ClassDeclaration>);
    assert(!annoOnTypeObj is ConstrainedAnnotation<AnnoOnType,AnnoOnType?,FunctionDeclaration>);
    assert(!annoOnTypeObj is ConstrainedAnnotation<AnnoOnType,AnnoOnType?,Annotated>);
    
    print(barObj);
}