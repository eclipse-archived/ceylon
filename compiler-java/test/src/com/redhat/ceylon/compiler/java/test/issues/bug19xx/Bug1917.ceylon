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
void bug1917(){
    variable Object a;
    Integer b = a = 2;
    a = 2;
    
    variable String? head = null;
    variable String? tail = null;
    head = tail = null;
    head = tail = nothing;
}

@noanno
void bug1917fails<Result>(Result() f) {
    variable Anything memo;
    Result x = memo = f();
}

@noanno
void bug1917works<Result>(Result() f) {
    variable Result|Object|Null memo;
    Result x = memo = f();
}

@noanno
class Bug1917Interop(Bug1917Java dataSource) extends Bug1917Java(){
    shared Integer loginTimeout1
            => loginTimeout;
    
    assign loginTimeout1
            => loginTimeout = loginTimeout1;

    shared Integer loginTimeout2
            => dataSource.loginTimeout;

    assign loginTimeout2
            => dataSource.loginTimeout = loginTimeout2;
}
