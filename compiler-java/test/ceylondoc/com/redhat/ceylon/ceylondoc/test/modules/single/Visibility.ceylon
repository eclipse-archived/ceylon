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
"This is `SharedClass`"
see(PrivateClass) 
shared class SharedClass() {
    "This simple attribte is `shared`"
    shared Integer sharedAttribute = 1;
    
    "This simple attribute is not `shared`"
    Integer privateAttribute = 1;
    
    "This getter is `shared`"
    shared Integer sharedGetter {
        return 1;
    }
    
    "This getter is not `shared`"
    Integer privateGetter {
        return 1;
    }
    
    "This method is `shared`"
    shared void sharedMethod() {}
    
    "This method is not `shared`"
    void privateMethod() {}
        
}

"This is `PrivateClass`"
by("Tom Bentley")
see(SharedClass)
class PrivateClass() {
    
    "This simple attribte is `shared`"
    shared Integer sharedAttribute = 1;
    
    "This simple attribute is not `shared`"
    Integer privateAttribute = 1;
    
    "This getter is `shared`"
    shared Integer sharedGetter {
        return 1;
    }
    
    "This getter is not `shared`"
    Integer privateGetter {
        return 1;
    }
    
    "This method is `shared`"
    shared void sharedMethod() {}
    
    "This method is not `shared`"
    void privateMethod() {}
        
}