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
This bug1385_1<This, Other>(This actual, Other expected) 
    given Other satisfies This {
    return actual;
}
@noanno
Other bug1385_2<This, Other>(This actual, Other expected) 
        given Other satisfies This {
    return expected;
}
@noanno
This bug1385_swapped_1<This, Other>(This actual, Other expected) 
        given This satisfies Other {
    return actual;
}
@noanno
Other bug1385_swapped_2<This, Other>(This actual, Other expected) 
        given This satisfies Other {
    return expected;
}
@noanno
This bug1385_constrained_1<This, Other>(This actual, Other expected)
        given This satisfies List<Character>
        given Other satisfies This {
    return actual;
}
@noanno
Other bug1385_constrained_2<This, Other>(This actual, Other expected)
        given This satisfies List<Character>
        given Other satisfies This {
    return expected;
}
@noanno
This bug1385_constrained2_1<This, Other>(This actual, Other expected)
        given This satisfies [String, Integer*]
        given Other satisfies This {
    return actual;
}
@noanno
Other bug1385_constrained2_2<This, Other>(This actual, Other expected)
        given This satisfies [String, Integer*]
        given Other satisfies This {
    return expected;
}

@noanno
void bug1385_test() {
    value a1 = bug1385_1(3, "foo");
    value a2 = bug1385_2(3, "foo");
    value b1 = bug1385_swapped_1(3, "foo");
    value b2 = bug1385_swapped_2(3, "foo");
    value c1 = bug1385_constrained_1(['c'], "foo");
    value c2 = bug1385_constrained_2(['c'], "foo");
    value d1 = bug1385_constrained2_1(["this"], ["other", 1]);
    value d2 = bug1385_constrained2_2(["this"], ["other", 1]);
}
