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
shared class EqualityTest() {

    class Entity (id) {    
    
        shared Integer id;
            
        shared actual Boolean equals(Object that) {
            if (is Entity that) {
                return id == that.id;
            } 
            return false;                        
        }
        
        shared actual Integer hash {
            return id;
        }        
    
    }

    @test
    shared void testEquals() {
        Object entity1 = Entity(+1);
        Object entity2 = Entity(+2);
        assertFalse(entity1 == entity2);
    }
        
    
    @test
    shared void testHash() {
        Object entity = Entity(+1);
        Integer entityHash = entity.hash;
        assertEquals(entityHash, entity.hash);        
    }    
    
}