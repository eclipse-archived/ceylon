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

@noanno
void destructuringLet([String, Float, Integer] tuple, String->Object entry) {
    value x1 = let ([s, f, i]=tuple) s.size + f*i;
    value y2 = let ([String s, Float f, Integer i]=tuple) s.size + f*i;
    value e1 = let (k->v=entry) k+v.string;
    value f2 = let (String k->Object v=entry) k+v.string;
}

@noanno
void variadicDestructuring([String, String, String*] strings, 
        [Integer, Float, String] tup, 
        [Float+] floats) {
    value [x, y, *rest] = strings;
    value [i, *pair] = tup;
    value [Float ff, String ss] = pair;
    value [z, *zs] = floats;
    
}

@noanno
void destructureTupleInEntry(String->[Float,Float] entry) {
    value s->[x, y] = entry;
    value z = let (s_->[x_, y_] = entry) x_*y_;
}

@noanno
void destructureNestedTuple([String,[Integer,Float],String->String] tuple) {
    value [s, [i, f], k -> v] = tuple;
    value x = let ([s_, [i_,f_], k_ -> v_] = tuple) k_+v_;
}

@noanno
void destructureInFor({[String, Float, String->String]*} iter) {
    for ([x, y, s1->s2] in iter) {
        String s = x;
        Float f = y;
        String->String e = s1->s2;
    }
    for ([String x, Float y, String s1 -> String s2] in iter) {
        String s = x;
        Float f = y;
        String->String e = s1->s2;
    }
}

@noanno
void destructureInForComprehensions({[String, Float[], String->String]*} iter, {[String, Float[], String->String]?*} iter2) {
    value xs = { for ([x, y, s1->s2] in iter) [s1->s2, y, x] };
    value ys = { for ([String x, Float[] y, String s1 -> String s2] in iter) [s1->s2, y, x] };
    value xys = { for ([x1, y1, sk1->sv1] in iter) for ([x2, y2, sk2->sv2] in iter) [x1, y2, sk1->sv2] };
    value xsif1 = { for (tup in iter2) if (exists [x, y, s1->s2]=tup) [s1->s2, y, x] };
    value xsif2 = { for ([x, y, s1->s2] in iter) if (nonempty [y1, *restys] = y) [s1->s2, y1, x] };
    value xsif3 = { for (tup in iter2) if (exists [x, y, s1->s2]=tup, nonempty [y1, *restys] = y) [s1->s2, y1, x] };
}

@noanno
void destructureIf([Float, Integer]? maybePair, String[] names, <String->Object>? maybeEntry) {
    if (exists [x, i] = maybePair) {
        Float c = x;
        Integer j = i;
    }
    if (exists k->v = maybeEntry) {
        String key = k;
        Object item = v;
    }
    if (nonempty [name, *rest] = names) {
        String n = name;
        String[] ns = rest;
    }
    if (exists [x, i] = maybePair, nonempty [name, *rest] = names) {
        Float c = x;
        Integer j = i;
        String n = name;
        String[] ns = rest;
    }
    if (exists [x, i] = maybePair, nonempty [name, *rest] = names) {
        Float c = x;
        Integer j = i;
        String n = name;
        String[] ns = rest;
    } else {
        // Do nothing, just need an else block
    }
}

@noanno
void destructureAssert([Float, Integer]? maybePair, String[] names, <String->Object>? maybeEntry) {
    assert (exists [x, i] = maybePair);
    Float c = x;
    Integer j = i;
    assert (exists k->v = maybeEntry);
    String key = k;
    Object item = v;
    assert (nonempty [name, *rest] = names);
    String n = name;
    String[] ns = rest;
    assert (exists [x2, i2] = maybePair, nonempty [name2, *rest2] = names);
    Float c2 = x2;
    Integer j2 = i2;
    String n2 = name2;
    String[] ns2 = rest2;
}

@noanno
void destructureWhile() {
    variable [Float, Integer]? maybePair = [1.0, 2];
    while (exists [x, i] = maybePair) {
        Float c = x;
        Integer j = i;
        maybePair = null;
    }
    variable <String->Object>? maybeEntry = ""->2;
    while (exists k->v = maybeEntry) {
        String key = k;
        Object item = v;
        maybeEntry = null;
    }
    variable String[] names = ["Enrique", "Tako"];
    while (nonempty [name, *rest] = names) {
        String n = name;
        String[] ns = rest;
        names = rest;
    }
    variable [Float, Integer]? maybePair2 = [1.0, 2];
    variable String[] names2 = ["Enrique", "Tako"];
    if (exists [x, i] = maybePair2, nonempty [name, *rest] = names2) {
        Float c = x;
        Integer j = i;
        String n = name;
        String[] ns = rest;
        maybePair = null;
    }
}
