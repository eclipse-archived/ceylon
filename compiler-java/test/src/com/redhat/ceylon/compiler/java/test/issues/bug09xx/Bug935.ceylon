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
[Sequential<[Integer,Element]>,Sequential<Element>] bug_935_collectMatches<Element>(Element[] data, Integer search(Element element)){
    variable [[Integer,Element]*] ok = [];
    variable [Element*] ko = [];
    for(element in data){
        value result = search(element);
        if(result > 0){
            ok = [[result, element], *ok];
        }else{
            ko = [element, *ko];
        }
    }
    return [ok.sequence(), ko.sequence()];
}

@noanno
void bug_935_test(){
   value results = bug_935_collectMatches(["Merry Christmas", "Happy Holidays"], function (String s) => s.size);
   print("Uppercase letters: ");
   for(result in results[0]){
       print("Size: `` result[0] ``, for: '`` result[1] ``'");
   }
}