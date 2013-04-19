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
void bug1011(Iterable<Integer> it) {
    bug1011 { for (x in {1}) x };
}

@noanno
Map<Item,Set<Key>> foo<Key,Item>(Map<Key,Item> map, Entry<Item,Set<Key>> extra)
   given Key satisfies Object
   given Item satisfies Object {
   LazyMap { for (key->item in map) item ->
                LazySet ([ for (k->i in map) if (i==item) k ]) };
   LazyMap ({ for (key->item in map) item ->
                LazySet ([ for (k->i in map) if (i==item) k ]) });
   LazyMap { extra, for (key->item in map) item ->
                LazySet ([ for (k->i in map) if (i==item) k ]) };
   LazyMap ({ extra, for (key->item in map) item ->
                LazySet ([ for (k->i in map) if (i==item) k ]) });
   Anything v1 = { for (key->item in map) item ->
                    LazySet ([ for (k->i in map) if (i==item) k ]) };
   Anything v2 = { extra, for (key->item in map) item ->
                    LazySet ([ for (k->i in map) if (i==item) k ]) };
   return nothing;
}