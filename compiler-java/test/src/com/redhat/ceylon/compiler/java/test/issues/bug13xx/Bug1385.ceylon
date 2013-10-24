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
@noanno
void bug1385<This, Other>(This actual, Other expected) 
    given Other satisfies This {
}
@noanno
void bug1385_swapped<This, Other>(This actual, Other expected) 
        given This satisfies Other {
}
@noanno
void bug1385_constrained<This, Other>(This actual, Other expected)
        given This satisfies List<Character>
        given Other satisfies This {
}
@noanno
class Bug1385_constrained2<This, Other>(This actual, Other expected)
        given This satisfies [String, Integer*]
        given Other satisfies This {
}

@noanno
void bug1385_test() {
    bug1385(3, "foo");
    bug1385_swapped(3, "foo");
    bug1385_constrained(['c'], "foo");
    bug1385_constrained2(["this"], ["other", 1]);
}
