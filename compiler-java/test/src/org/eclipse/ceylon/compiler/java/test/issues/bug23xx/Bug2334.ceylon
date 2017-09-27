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
import ceylon.language.meta.model { Attribute }

interface Bug2334I<T> {}

class Bug2334MyImpl() satisfies Bug2334I<String>{}

void bug2334Work<in Container>(
    Attribute<Container, Bug2334I<String>>|Attribute<Container, String, String> attribute) 
{
    value v = if(is Attribute<Container, Bug2334I<String>> attribute) then 0 else 1; // NPE here
}

shared void bug2334() {
    void doRun() { 
        class MyContainer() {
            shared Bug2334MyImpl myI = Bug2334MyImpl();
        }
        bug2334Work(`MyContainer.myI`); 
    }
    doRun();
}
