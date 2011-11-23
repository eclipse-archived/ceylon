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
shared class NumericOperationTest() extends Test() {

  @test
  shared void testEqual() {
  	Boolean equal = 1==1;
     assertTrue(equal);
  }

  @test
  shared void testNotEqual() {
  	Boolean notEqual = 1!=2;
    assertTrue(notEqual);
  }      
  
  @test
  shared void testSmaller() {
  	Boolean smaller = 1<2;
    assertTrue(smaller);
  }
  
  @test
  shared void testLarger() {
  	Boolean larger = 2>1;
    assertTrue(larger);
  }         
  
  @test
  shared void testSmallAs() {
  	Boolean smallAs = 1<=2;
     assertTrue(smallAs);
  }
  
  @test
  shared void testLargeAs() {
  	Boolean largeAs = 2>=1;
    assertTrue(largeAs);
  }

}