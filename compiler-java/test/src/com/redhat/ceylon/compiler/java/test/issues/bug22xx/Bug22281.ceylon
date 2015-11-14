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
void bug2228Shit<T,Element>() given T satisfies Element[] {
    bug2228Test<Tuple<Integer|Element, Integer, T>>([1], [1, 2]);
    bug2228Test<Tuple<Integer|String, Integer, [Integer]|[String]|[]>>([1], [1, 2], [1, "a"]);
}

void bug2228Method(){
    bug2228Test<[Integer*]>([], [1], [1,2]);
    bug2228Test<[Integer+]>([1], [1,2]);
    bug2228Test<[Integer]>([1]);
    bug2228Test<[Integer=]>([1], []);
    bug2228Test<[Integer=,String=]>([], [1], [1,"a"]);
    bug2228Test<[Integer,String=]>([1], [1, "a"]);
    bug2228Test<[Integer,String]>([1, "a"]);
    bug2228Test<[Integer,String=,Boolean*]>([1], [1, "a"], [1, "a", true], [1, "a", true, false]);
    bug2228Test<[Integer,String+]>([1, "a"], [1, "a", "b"]);
    bug2228Shit<[Integer*],Integer>();
}
void bug2228Test<T>(Object* args){
    print("Testing with `` `T` ``");
    for(arg in args){
        print(" value `` arg ``");
        assert(is T arg);
    }
}