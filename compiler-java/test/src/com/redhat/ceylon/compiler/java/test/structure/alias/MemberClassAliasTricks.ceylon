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
shared class MemberClassAliasTricks_Foo(Integer a = 1, Integer b = 2){
    
    shared class MemberClassAliasToToplevel(Integer a, Integer b) => MemberClassAliasTricks_Foo(a,b);
    shared class MemberClassAliasToToplevel2(Integer a, Integer b) => MemberClassAliasToToplevel(a,b);

    shared class Member(Integer a = 1, Integer b = 2){
        shared class MemberClassAliasToEnclosingMemberClass(Integer a, Integer b) => Member(a,b);

        void test(){
            value m1 = MemberClassAliasToEnclosingMemberClass(1,2);
        }
    }

    void test(){
        value m1 = MemberClassAliasToToplevel(1,2);
        value m2 = MemberClassAliasToToplevel2(1,2);
        value m3 = Member(1,2).MemberClassAliasToEnclosingMemberClass(3,4);
    }
}

@noanno
void memberClassAliasTricksMethod(){
    value foo1 = MemberClassAliasTricks_Foo(1,2).MemberClassAliasToToplevel(3,4);
    value foo2 = MemberClassAliasTricks_Foo(1,2).MemberClassAliasToToplevel2(3,4);
}