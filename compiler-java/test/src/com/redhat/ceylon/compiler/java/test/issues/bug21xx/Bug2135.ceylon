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
T notCalled<T>(T t){
    assert(false);
}

shared void bug2135(){
    // original issue
    printAll { if (1==2) for (fd in [3]) if (1==1) fd.string };
    // more tests
    value t1 = false;
    value t2 = true;
    Iterable<Object> seq = [];
    Iterable<Object> seq2 = [1];

    value iter = { 
        if (t1) 
        for (elem in notCalled(seq)) 
        if (notCalled(t2)) 
        elem.string 
    };
    assert (iter.sequence() == []);

    value iter2 = { 
        if (t1) 
        for (elem in notCalled(seq)) 
        elem.string 
    };
    assert (iter2.sequence() == []);

    value iter3 = { 
        if (t2) 
        for (elem in seq) 
        elem.string 
    };
    assert (iter3.sequence() == []);

    value iter4 = { 
        for (elem in seq) 
        if (t2) 
        for (elem2 in seq) 
        elem2.string 
    };
    assert (iter4.sequence() == []);

    value iter5 = { 
        for (elem in seq2) 
        if (t1) 
        elem.string 
    };
    assert (iter5.sequence() == []);

    value iter6 = { 
        for (elem in seq2) 
        if (t2) 
        elem.string 
    };
    assert (iter6.sequence() == ["1"]);
}