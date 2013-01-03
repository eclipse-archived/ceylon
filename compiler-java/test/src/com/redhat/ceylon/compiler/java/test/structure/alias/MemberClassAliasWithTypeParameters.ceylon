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
shared class MemberClassAliasWithTypeParameters_Foo<C>(C c){
    shared class Member<T>(T t){}
    
    shared class MemberClassAlias1<S>(S s) => Member<S>(s);
    shared class MemberClassAlias2(Integer s) => Member<Integer>(s);
    shared class MemberClassAlias3(C s) => Member<C>(s);

    void test(){
        value m1 = MemberClassAlias1<Integer>(1);
        value m2 = MemberClassAlias2(1);
        value m3 = MemberClassAlias3(c);
    }
}

@nomodel
void memberClassAliasWithTypeParametersMethod(){
    value foo1 = MemberClassAliasWithTypeParameters_Foo<Integer>(1).MemberClassAlias1<Integer>(1);
    value foo2 = MemberClassAliasWithTypeParameters_Foo<Integer>(1).MemberClassAlias2(1);
    value foo3 = MemberClassAliasWithTypeParameters_Foo<Integer>(1).MemberClassAlias3(1);
}