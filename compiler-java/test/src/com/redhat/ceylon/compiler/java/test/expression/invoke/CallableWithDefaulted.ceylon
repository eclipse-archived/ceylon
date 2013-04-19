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
void callableWithDefaulted() {
    Callable<Anything, [Integer, String=, Integer*]> defaultedVariadic = function (Integer a, String b = "b", Integer* args) => a;
    defaultedVariadic(1);
    defaultedVariadic(1, "a");
    defaultedVariadic(1, "a", 1);
    defaultedVariadic(1, "a", 1, 2);
    value ints = [1,2];
    defaultedVariadic(1, "a", *ints);
    defaultedVariadic(1, "a", 3, *ints);
    defaultedVariadic(1, "a", 3, 4, *ints);
    defaultedVariadic(1, "a", for (i in ints) i);
    defaultedVariadic(1, "a", 3, for (i in ints) i);
    defaultedVariadic(1, "a", 3, 4, for (i in ints) i);
    
    Callable<Anything, [Integer, String]> notDefaulted = function (Integer goto, String b) => goto;
    notDefaulted(1, "a");

    Callable<Anything, [Integer=, String=]> foo = function (Integer goto = 2, String b = "foo" + goto.string) => goto;
    foo();
    foo(1);
    foo(1, "a");

    Callable<Anything, [Integer=, String=, Integer=, String=, Integer=, String=]> bar = 
        function (Integer goto = 2, String b = "foo", Integer c = 2, String d = "foo", Integer e = 2, String f = "foo" + goto.string) => goto;
    bar();
    bar(1);
    bar(1, "a", 2, "b", 3, "c");
    
    Callable<Anything, [Integer, Integer=]> oneOrTwo = function (Integer a, Integer b = 2) => a;
    Callable<Anything, [Integer]> one = oneOrTwo;
    Callable<Anything, [Integer*]> oneOrPlenty = function (Integer* a) => a;
    Callable<Anything, [Integer]> oneAgain = oneOrPlenty;
}

@noanno
class CallableWithDefaulted(){
    void defaultedVariadic(Integer a, String b = "b", Integer* args){
    }
    void test(){
        value f = defaultedVariadic;
    }
    void methodWithDefaultCallableParam(Integer bar(Integer a, Integer b) => a + b){
        methodWithDefaultCallableParam{
            Integer bar(Integer a, Integer b){
                return a + b;
            }
        };
    }
    void methodWithDefaultCallableParam2(Callable<Integer,[]> bar = () => 2){
    }
}