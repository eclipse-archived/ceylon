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
interface Bug866_Visitable<Visited> of Visited satisfies Iterable<Integer|Visited> 
    given Visited satisfies Bug866_Visitable<Visited> {

    shared formal String description;

    void visitChildren(void f(Integer|Visited t), void dispatcher(Visited t)){
        for(child in this){ // ERROR LINE
            if(is Integer child){
                f(child);
            }else if(!is Integer child){
                dispatcher(child);
            }
        }
    }

    shared void visitPrefix(void f(Integer|Visited t)){
        f(this of Visited);
        visitChildren(f, function (Visited child) => child.visitPrefix(f));
    }

    shared void visitPostfix(void f(Integer|Visited t)){
        visitChildren(f, function (Visited child) => child.visitPostfix(f));
        f(this of Visited);
    }
}
