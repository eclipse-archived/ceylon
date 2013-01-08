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

// test let expressions in every context:

// static final field 
@nomodel
Entry<Integer,String> bug270 = Entry<Integer,String> {item="hello";key=1;};

@nomodel
variable Entry<Integer,String> bug270Variable = Entry<Integer,String> {item="hello";key=1;};

@nomodel
T bug270_eatEntry<T>(T entry){
    return entry;
}

@nomodel
Entry<Entry<Integer,String>,String> bug270_2 = bug270_eatEntry(Entry<Entry<Integer,String>,String> {
    item="hello";
    key=Entry<Integer,String>{
        item="hello";
        key=1;
    };
});

// static field 
@nomodel
class Bug270() {
    // in the constructor
    Entry<Integer,String> bug270 = Entry<Integer,String> {item="hello";key=1;};

    // in constructor with method call and entry in entry
    Entry<Entry<Integer,String>,String> bug270_2 = bug270_eatEntry(Entry<Entry<Integer,String>,String> {
        item="hello";
        key=Entry<Integer,String>{
            item="hello";
            key=1;
        };
    });
    
    void forceFieldGenerationByCapture(){
        this.bug270.equals(1);
    }
    
    void method(){
        // final variable 
        Entry<Integer,String> bug270 = Entry<Integer,String> {item="hello";key=1;};

        // variable 
        variable Entry<Integer,String> bug270Variable = Entry<Integer,String> {item="hello";key=1;};
        if(true){
            // inside a new block?
            Entry<Integer,String> bug270_2 = Entry<Integer,String> {item="hello";key=1;};
        }
        
        // as a method parameter
        bug270_eatEntry(Entry<Integer,String> {item="hello";key=1;});
    }
    
}
