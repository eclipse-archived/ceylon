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
@nomodel
shared class TypeAliasWithTypeParametersClass<T>(){}

@nomodel
shared interface TypeAliasWithTypeParametersInterface<T>{}

alias TypeAliasWithTypeParameters1 => TypeAliasWithTypeParametersClass<Integer> & TypeAliasWithTypeParametersInterface<String>;
alias TypeAliasWithTypeParameters2<T> => TypeAliasWithTypeParametersClass<T> | TypeAliasWithTypeParametersInterface<T>;
alias TypeAliasWithTypeParameters3<T,V> => TypeAliasWithTypeParametersClass<T>;
alias TypeAliasWithTypeParameters4<T,V> => TypeAliasWithTypeParametersInterface<V>;

TypeAliasWithTypeParameters1 typeAliasWithTypeParametersMethod1(TypeAliasWithTypeParameters1 f){
    return f;
}

TypeAliasWithTypeParameters2<Integer> typeAliasWithTypeParametersMethod2(TypeAliasWithTypeParameters2<Integer> f){
    return f;
}

TypeAliasWithTypeParameters3<T,V> typeAliasWithTypeParametersMethod3<T,V>(TypeAliasWithTypeParameters3<T,V> f){
    return f;
}

TypeAliasWithTypeParameters4<T,V> typeAliasWithTypeParametersMethod4<T,V>(TypeAliasWithTypeParameters4<T,V> f){
    return f;
}