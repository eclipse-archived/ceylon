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
void bug1543(){
    variable [Integer+] chain = [1];
    for (i in 0:100000) {
        chain = chain.withLeading(i);
    }
}

shared interface My1543Sequential<out Element>
        satisfies List<Element> {
    // defined as shared default {Element|Other+} withLeading<Other>(Other head)
    // works with [Other|Element+] -> Sequence
    // fails with [Other,Element*] -> Tuple<Object,Other,Sequential<Element>>
    shared actual formal [Other,Element*] withLeading<Other>(Other head);
    //shared actual formal [Other|Element+] follow<Other>(Other head);
    shared formal [Other,Element*] withLeading2<Other>(Other head);
    shared formal [Integer,Element*] a;
    shared formal [Integer,Element*] m();
}

shared interface My1543Sequence<out Element> satisfies My1543Sequential<Element> {
    shared actual formal [Other,Element+] withLeading<Other>(Other head);
}

shared interface My1543Empty 
        satisfies My1543Sequential<Nothing> {
    // always Tuple<Other, Other, Sequential<Other>> 
    // non-widening now Tuple<Other, Other, Sequential<Other>>
    shared actual [Other] withLeading<Other>(Other head) => nothing;
    shared actual [Other] withLeading2<Other>(Other head) => nothing;
    shared actual [Integer] a => nothing;
    shared actual [Integer] m() => nothing;
}
