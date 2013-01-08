

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
@nomodel
class AnythingBoxing() {
    Anything valueAnything = 1;
    variable Anything variableAnything = valueAnything;
    Anything getterAnything { 
        return variableAnything;
    } 
    assign getterAnything {
        variableAnything = getterAnything;
    }
    void returnsvoid(Anything v){}
    Anything returnsAnything(Anything v){
        return v;
    }
    void callablevoid(Callable<Anything, [Anything]> f){
        f(valueAnything);
    }
    Anything callableAnything(Callable<Anything, [Anything]> f){
        return f(valueAnything);
    }
        
    void test() {
        Anything v1 = valueAnything;
        Anything v2 = getterAnything;
        Anything v3 = returnsvoid("");
        Anything v4 = returnsvoid{v="";};
        Anything v5 = returnsAnything("");
        Anything v6 = returnsAnything{v="";};
        returnsvoid(returnsvoid(""));
        returnsvoid(returnsAnything(""));
        returnsAnything(returnsvoid(""));
        returnsAnything(returnsAnything(""));
        Anything v7 = callablevoid(returnsvoid);
        Anything v8 = callablevoid(returnsAnything);
        Anything v9 = callableAnything(returnsvoid);
        Anything v10= callableAnything(returnsAnything);
        Anything v11= callablevoid((Anything s) => s);
        Anything v12= callableAnything((Anything s) => s); 
        
        variable Anything v;
        v = valueAnything;
        v = getterAnything;
        v = returnsvoid("");
        v = returnsvoid{v="";};
        v = returnsAnything("");
        v = returnsAnything{v="";};
        v = callablevoid(returnsvoid);
        v = callablevoid(returnsAnything);
        v = callableAnything(returnsvoid);
        v = callableAnything(returnsAnything);
        v = callablevoid((Anything s) => s);
        v = callableAnything((Anything s) => s);
        
    }
}