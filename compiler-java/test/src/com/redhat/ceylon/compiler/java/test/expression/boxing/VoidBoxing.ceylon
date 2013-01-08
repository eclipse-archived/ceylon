

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
class VoidBoxing() {
    Void valueVoid = 1;
    variable Void variableVoid = valueVoid;
    Void getterVoid { 
        return variableVoid;
    } 
    assign getterVoid {
        variableVoid = getterVoid;
    }
    void returnsvoid(Void v){}
    Void returnsVoid(Void v){
        return v;
    }
    void callablevoid(Callable<Void, [Void]> f){
        f(valueVoid);
    }
    Void callableVoid(Callable<Void, [Void]> f){
        return f(valueVoid);
    }
        
    void test() {
        Void v1 = valueVoid;
        Void v2 = getterVoid;
        Void v3 = returnsvoid("");
        Void v4 = returnsvoid{v="";};
        Void v5 = returnsVoid("");
        Void v6 = returnsVoid{v="";};
        returnsvoid(returnsvoid(""));
        returnsvoid(returnsVoid(""));
        returnsVoid(returnsvoid(""));
        returnsVoid(returnsVoid(""));
        Void v7 = callablevoid(returnsvoid);
        Void v8 = callablevoid(returnsVoid);
        Void v9 = callableVoid(returnsvoid);
        Void v10= callableVoid(returnsVoid);
        Void v11= callablevoid((Void s) s);
        Void v12= callableVoid((Void s) s); 
        
        variable Void v;
        v = valueVoid;
        v = getterVoid;
        v = returnsvoid("");
        v = returnsvoid{v="";};
        v = returnsVoid("");
        v = returnsVoid{v="";};
        v = callablevoid(returnsvoid);
        v = callablevoid(returnsVoid);
        v = callableVoid(returnsvoid);
        v = callableVoid(returnsVoid);
        v = callablevoid((Void s) s);
        v = callableVoid((Void s) s);
        
    }
}