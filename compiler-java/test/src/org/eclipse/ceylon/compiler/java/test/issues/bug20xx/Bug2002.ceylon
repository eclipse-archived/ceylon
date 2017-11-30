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
[<Head|Tail>*] bug2002Builder1
        <Head, Tail>
        (Head head, [Tail*]() tail)()
        given Tail satisfies Anything
    => tail().withLeading(head);

[<String|Integer>*]() bug2002BuilderTest1 =
    bug2002Builder1("A",
        bug2002Builder1(1,
            () => []));

[<Head|Tail>*] bug2002Builder2
        <Head, Tail>
        (Head head, [Tail*]() tail)()
        given Tail satisfies Boolean
    => tail().withLeading(head);

[<Boolean>*]() bug2002BuilderTest2 =
    bug2002Builder2(true,
        () => []);
