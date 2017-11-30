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
import java.util { JHashMap = HashMap }

@noanno
shared String? bug2244TestNulls1()
   => let (map = JHashMap<String,String>())
   map.get("");

@noanno
shared String? bug2244TestNulls1b(){
    value map = JHashMap<String,String>();
    String s = if(true) then "" else "";
    Object o = if(true) then "" else "";
    Anything a = if(true) then "" else "";
    return if(true) then map.get("") else "";
}

@noanno
shared String? bug2244TestNulls1c(){
    value map = JHashMap<String,String>();
    String s = switch(true) case(true) "" else "";
    Object o = switch(true) case(true) "" else "";
    Anything a = switch(true) case(true) "" else "";
    return switch(true) case(true) map.get("");
}


@noanno
shared String? bug2244TestNulls2(){
    return let (map = JHashMap<String,String>())
            map.get("");
}

@noanno
shared String? bug2244TestNulls3(){
    value map = JHashMap<String,String>();
    return map.get("");
}
