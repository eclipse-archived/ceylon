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
import ceylon.language.meta { type }

void testInlineObjects(){
    inlineObject<String>();
}

void inlineObject<T>(Object param = object {
        shared Integer a = 2;
        shared void f() {}
    }, Object p = 1, Object l = 1, Boolean check = false){
    value local = object {
        shared Integer a = 2;
        shared void f() {}
    };
    
    if(check){
        assert(!type(param).typeOf(p));
        assert(type(param).typeOf(param));
        assert(!type(local).typeOf(l));
        assert(type(local).typeOf(local));
        return;
    }
    
    value paramDecl = type(param).declaration;
    assert(paramDecl.anonymous,
           paramDecl.container == `function inlineObject`);
    
    value localDecl = type(local).declaration;
    assert(localDecl.anonymous,
        localDecl.container == `function inlineObject`);

    value inlineObjectTopLevelDecl = type(inlineObjectTopLevel).declaration;
    assert(inlineObjectTopLevelDecl.anonymous,
           inlineObjectTopLevelDecl.container == `package`);

    value inClass = InlineObjectInDefaultClassParam<String>();
    value inClassParamDecl = type(inClass.param).declaration;
    assert(inClassParamDecl.anonymous,
           inClassParamDecl.container == `class InlineObjectInDefaultClassParam`);

    value inClassAttributeDecl = type(inClass.attribute).declaration;
    assert(inClassAttributeDecl.anonymous,
           inClassAttributeDecl.container == `class InlineObjectInDefaultClassParam`);

    value inClassMethodDefaultParamDecl = type(inClass.method()).declaration;
    assert(inClassMethodDefaultParamDecl.anonymous,
           inClassMethodDefaultParamDecl.container == `function InlineObjectInDefaultClassParam.method`);

    value inSuperClass = InlineObjectInSuperClassParam();
    value inSuperClassParamDecl = type(inSuperClass.param).declaration;
    assert(inSuperClassParamDecl.anonymous,
           inSuperClassParamDecl.container == `class InlineObjectInSuperClassParam`);

    value inClass2 = InlineObjectInDefaultClassParam<Integer>();
    inClass2.check(inClass.param, inClass.attribute, inClass.method());
    inlineObject<Integer>{ p = param; l = local; check = true; };
}

Object inlineObjectTopLevel = object {
    shared Integer a = 2;
    shared void f() {}
};

class InlineObjectInDefaultClassParam<T>(shared Object param = object {
        shared Integer a = 2;
        shared T f() { return nothing; }
    }) {
    shared Object attribute = object {
        shared Integer a = 2;
        shared void f() {}
    };
    shared Object method(Object param = object {
            shared Integer a = 2;
            shared void f() {}
        }){
        return param;
    }
    shared void check(Object p, Object a, Object mp){
        // make sure that two instantiations of InlineObjectInDefaultClassParam objects with different type parameters
        // are incompatible
        assert(!type(param).typeOf(p));
        assert(type(param).typeOf(param));
        assert(!type(attribute).typeOf(a));
        assert(type(attribute).typeOf(attribute));
        assert(!type(method()).typeOf(mp));
        assert(type(method()).typeOf(method()));
    }
}

class InlineObjectInSuperClassParam() extends InlineObjectInDefaultClassParam<String>(object {
    shared Integer a = 2;
    shared void f() {}
}) {}
