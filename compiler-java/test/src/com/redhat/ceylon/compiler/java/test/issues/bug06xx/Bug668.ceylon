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
shared interface Bug668_X<out Element, out Null>
        given Null satisfies Nothing {
    shared formal Null|Element first;
}

@nomodel
shared interface Bug668_Y<out Element> 
        satisfies Bug668_X<Element,Nothing> {
    shared actual default Element? first {
        return null;
    }

}

@nomodel
void bug668_method<Null>(Null n) given Null satisfies Nothing { 
    object obj satisfies Bug668_Y<Bottom> & Bug668_X<Bottom,Nothing> {}
}
