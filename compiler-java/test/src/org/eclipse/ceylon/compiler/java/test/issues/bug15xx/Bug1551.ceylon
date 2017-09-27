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
shared class Sql() {
    shared class Insert<in ValueTypes>() {
        shared void execute(ValueTypes arguments) {
        }
    }
    shared Insert<ValueTypes> foo<in ValueTypes>(){
        return Insert<ValueTypes>();
    }
}

@noanno
shared class Insert<in ValueTypes>() {
    shared void execute(ValueTypes arguments) {
    }
}

shared Insert<ValueTypes> insert<in ValueTypes>() {
    return Insert<ValueTypes>();
}

@noanno
void runInsert() {
    Sql sql = Sql();
    sql.Insert<[String,Integer]>().execute(["Second", 2]);
    sql.foo<[String,Integer]>().execute(["Second", 2]);
    Insert<[String,Integer]>().execute(["Second", 2]);
    insert<[String,Integer]>().execute(["Second", 2]);
    value t = ["Second", 2];
    insert<[String,Integer]>().execute(t);
}
