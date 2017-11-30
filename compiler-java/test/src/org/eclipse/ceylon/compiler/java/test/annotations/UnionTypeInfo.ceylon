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
shared class Foo() {}
shared class Bar() {}

class UnionTypeInfo(Foo|Bar param){
    Foo|Bar attr;
    shared Foo|Bar sharedAttr = Foo();
    Foo|Bar getter {
        return Foo();
    }
    assign getter {
    }

    shared Foo|Bar sharedGetter {
        return Foo();
    }
    assign sharedGetter {
    }
 
    Foo|Bar method(Foo|Bar methodParam){
        Foo|Bar val = Foo();
        return val;
    }

    shared Foo|Bar sharedMethod(Foo|Bar methodParam){
        Foo|Bar val = Foo();
        return val;
    }
}

shared class SharedUnionTypeInfo(Foo|Bar param){
}

Foo|Bar toplevelAttribute = Foo();
shared Foo|Bar sharedToplevelAttribute = Bar();

Foo|Bar toplevelGetter {
    return Foo();
}
assign toplevelGetter {
}

shared Foo|Bar toplevelSharedGetter {
    return Foo();
}
assign toplevelSharedGetter {
}

Foo|Bar toplevelMethod(Foo|Bar param){
    return Foo();
}

shared Foo|Bar sharedToplevelMethod(Foo|Bar param){
    return Foo();
}