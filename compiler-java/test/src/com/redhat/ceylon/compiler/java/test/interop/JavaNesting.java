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
package com.redhat.ceylon.compiler.java.test.interop;

interface JavaNestingInterface<C> {
    // inner classes are implicitely static
    public static class InnerStatic<I>{
        public class Inner2<I2>{
            public class Inner3<I3>{
            }
        }
        public static class InnerStatic2<I2>{
            public class Inner3<I3>{
            }
        }
    }

    public interface InnerInterface<I>{
        // implicitely static
        public static class Inner2<I2>{
            public class Inner3<I3>{
            }        
            public static class InnerStatic3<I3>{
            }        
        }        
    }
    
    public interface InnerInterface2 {}
}

public class JavaNesting<C> {
    public class Inner<I>{
        public class Inner2<I2>{
        }
    }

    public static class InnerStatic<I>{
        public class Inner2<I2>{
            public class Inner3<I3>{
            }
        }
        public static class InnerStatic2<I2>{
            public class Inner3<I3>{
            }
        }
        public interface InnerInterface<I2>{}
    }

    // implicitely static
    public interface InnerInterface<I>{
        // implicitely static
        public static class Inner2<I2>{
            public class Inner3<I3>{
            }        
            public static class InnerStatic3<I3>{
            }        
        }        
    }
}
