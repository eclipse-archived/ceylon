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
void tupleVar([Integer, Float, String] tuple) {
    value [i1, f1, s1] = tuple;
    value [i2, Float f2, s2] = tuple;
    value [Integer i3, Float f3, String s3] = tuple;
}

@noanno
void tupleLiteral() {
    value [i1, f1, s1] = [0, 1.0, "foo"];
    value [i2, Float f2, s2] = [0, 1.0, "foo"];
    value [Integer i3, Float f3, String s3] = [0, 1.0, "foo"];
}

@noanno
void tupleGeneric() {
    class Foo<T>() {}
    class FooSub<T>() extends Foo<T>() {}
    value [f1] = [FooSub<Integer>()];
    value [FooSub<Integer> f2] = [FooSub<Integer>()];
    value [Foo<Integer> f3] = [FooSub<Integer>()];
}

@noanno
void entryVar(Integer->String entry) {
    value i1->s1 = entry;
    value Integer i2->s2 = entry;
    value Integer i3->String s3 = entry;
}

@noanno
void entryLiteral() {
    value i1->s1 = 0->"foo";
    value Integer i2->s2 = 0->"foo";
    value Integer i3->String s3 = 0->"foo";
}

@noanno
void entryGeneric() {
    class Foo<T>() {}
    class FooSub<T>() extends Foo<T>() {}
    value i1->f1 = 0->FooSub<Integer>();
    value Integer i2->FooSub<Integer> f2 = 0->FooSub<Integer>();
    value Integer i3->Foo<Integer> f3 = 0->FooSub<Integer>();
}
