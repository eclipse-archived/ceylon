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
shared class MemberTypeAliasClass(){
    
    shared class TypeAliasClass(){}
    shared interface TypeAliasInterface{}
    
    shared alias TypeAlias1 => TypeAliasClass & TypeAliasInterface;
    shared alias TypeAlias2 => TypeAliasClass | TypeAliasInterface;
    shared alias TypeAlias3 => TypeAliasClass;
    shared alias TypeAlias4 => TypeAliasInterface;
    
    TypeAlias1 classAliasMethod1(TypeAlias1 f){
        return f;
    }
    
    TypeAlias2 classAliasMethod2(TypeAlias2 f){
        return f;
    }
    
    TypeAlias3 classAliasMethod3(TypeAlias3 f){
        return f;
    }
    
    TypeAlias4 classAliasMethod4(TypeAlias4 f){
        return f;
    }
}

shared interface MemberTypeAliasInterface{
    
    shared class TypeAliasClass(){}
    shared interface TypeAliasInterface{}
    
    shared alias TypeAlias1 => TypeAliasClass & TypeAliasInterface;
    shared alias TypeAlias2 => TypeAliasClass | TypeAliasInterface;
    shared alias TypeAlias3 => TypeAliasClass;
    shared alias TypeAlias4 => TypeAliasInterface;
    
    TypeAlias1 classAliasMethod1(TypeAlias1 f){
        return f;
    }
    
    TypeAlias2 classAliasMethod2(TypeAlias2 f){
        return f;
    }
    
    TypeAlias3 classAliasMethod3(TypeAlias3 f){
        return f;
    }
    
    TypeAlias4 classAliasMethod4(TypeAlias4 f){
        return f;
    }
}