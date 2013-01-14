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

shared class InteropVariadicTest() {

    T box<T>(T t){ return t; }

    @test
    shared void testVarargsByte() {
        VariadicJava java = VariadicJava();
        java.testVarargsByte3(1, box(2), 3);
        java.testVarargsByte3(*[1, box(2), 3]);
        value seq = [1, box(2), 3];
        java.testVarargsByte3(*seq);

        java.testVarargsByte0();
        java.testVarargsByte0(*{});
        value empty = {};
        java.testVarargsByte0(*empty);
    }

    @test
    shared void testVarargsShort() {
        VariadicJava java = VariadicJava();
        java.testVarargsShort3(1, box(2), 3);
        java.testVarargsShort3(*[1, box(2), 3]);
        value seq = [1, box(2), 3];
        java.testVarargsShort3(*seq);

        java.testVarargsShort0();
        java.testVarargsShort0(*{});
        value empty = {};
        java.testVarargsShort0(*empty);
    }

    @test
    shared void testVarargsInt() {
        VariadicJava java = VariadicJava();
        java.testVarargsInt3(1, box(2), 3);
        java.testVarargsInt3(*[1, box(2), 3]);
        value seq = [1, box(2), 3];
        java.testVarargsInt3(*seq);

        java.testVarargsInt0();
        java.testVarargsInt0(*{});
        value empty = {};
        java.testVarargsInt0(*empty);
    }

    @test
    shared void testVarargsLong() {
        VariadicJava java = VariadicJava();
        java.testVarargsLong3(1, box(2), 3);
        java.testVarargsLong3(*[1, box(2), 3]);
        value seq = [1, box(2), 3];
        java.testVarargsLong3(*seq);

        java.testVarargsLong0();
        java.testVarargsLong0(*{});
        value empty = {};
        java.testVarargsLong0(*empty);
    }

    @test
    shared void testVarargsFloat() {
        VariadicJava java = VariadicJava();
        java.testVarargsFloat3(1.0, box(2.0), 3.0);
        java.testVarargsFloat3(*[1.0, box(2.0), 3.0]);
        value seq = [1.0, box(2.0), 3.0];
        java.testVarargsFloat3(*seq);

        java.testVarargsFloat0();
        java.testVarargsFloat0(*{});
        value empty = {};
        java.testVarargsFloat0(*empty);
    }

    @test
    shared void testVarargsDouble() {
        VariadicJava java = VariadicJava();
        java.testVarargsDouble3(1.0, box(2.0), 3.0);
        java.testVarargsDouble3(*[1.0, box(2.0), 3.0]);
        value seq = [1.0, box(2.0), 3.0];
        java.testVarargsDouble3(*seq);

        java.testVarargsDouble0();
        java.testVarargsDouble0(*{});
        value empty = {};
        java.testVarargsDouble0(*empty);
    }

    @test
    shared void testVarargsBoolean() {
        VariadicJava java = VariadicJava();
        java.testVarargsBoolean3(true, box(false), false);
        java.testVarargsBoolean3(*[true, box(false), false]);
        value seq = [true, box(false), false];
        java.testVarargsBoolean3(*seq);

        java.testVarargsBoolean0();
        java.testVarargsBoolean0(*{});
        value empty = {};
        java.testVarargsBoolean0(*empty);
    }

    @test
    shared void testVarargsChar() {
        VariadicJava java = VariadicJava();
        java.testVarargsChar3(`a`, box(`b`), `c`);
        java.testVarargsChar3(*[`a`, box(`b`), `c`]);
        value seq = [`a`, box(`b`), `c`];
        java.testVarargsChar3(*seq);

        java.testVarargsChar0();
        java.testVarargsChar0(*{});
        value empty = {};
        java.testVarargsChar0(*empty);
    }

    @test
    shared void testVarargsCeylonInteger() {
        VariadicJava java = VariadicJava();
        java.testVarargsCeylonInteger3(1, box(2), 3);
        java.testVarargsCeylonInteger3(*[1, box(2), 3]);
        value seq = [1, box(2), 3];
        java.testVarargsCeylonInteger3(*seq);

        java.testVarargsCeylonInteger0();
        java.testVarargsCeylonInteger0(*{});
        value empty = {};
        java.testVarargsCeylonInteger0(*empty);
    }

    @test
    shared void testVarargsObject() {
        VariadicJava java = VariadicJava();
        java.testVarargsObject3(1, box(2), 3);
        java.testVarargsObject3(*[1, box(2), 3]);
        value seq = [1, box(2), 3];
        java.testVarargsObject3(*seq);

        java.testVarargsObject0();
        java.testVarargsObject0(*{});
        value empty = {};
        java.testVarargsObject0(*empty);
    }


}