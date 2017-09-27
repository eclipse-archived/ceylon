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
 
variable Integer numericOperationIncrDecrTestToplevel = 0;
Integer numericOperationIncrDecrTestGetter {
    return numericOperationIncrDecrTestToplevel;
}
assign numericOperationIncrDecrTestGetter {
    numericOperationIncrDecrTestToplevel = numericOperationIncrDecrTestGetter;
}

shared abstract class NumericOperationTestParameterized<T>(T init) {
    shared variable T boxedInteger = init;
}

shared class NumericOperationTest() extends NumericOperationTestParameterized<Integer>(0) {

  variable Integer incrDecrCounter = 0;
  variable Integer unboxedAttrIncrDecr = 0;
  variable Integer unboxedAttrIncrDecrGetterHolder = 0;
  Integer unboxedAttrIncrDecrGetter {
    return unboxedAttrIncrDecrGetterHolder;
  }
  assign unboxedAttrIncrDecrGetter {
    unboxedAttrIncrDecrGetterHolder = unboxedAttrIncrDecrGetter;
  }

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

  @test
  shared void testUnboxedLocalIncrDecr(){
    variable Integer n = 0;
    assertEquals(0, n);

    // postfix ++
    assertEquals(0, n++);
    assertEquals(1, n);
    n++;
    assertEquals(2, n);

    // postfix --
    assertEquals(2, n--);
    assertEquals(1, n);
    n--;
    assertEquals(0, n);
  
    // prefix ++
    assertEquals(1, ++n);
    assertEquals(1, n);
    ++n;
    assertEquals(2, n);

    // prefix --
    assertEquals(1, --n);
    assertEquals(1, n);
    --n;
    assertEquals(0, n);
  }

  @test
  shared void testUnboxedLocalIncrDecrGetter(){
    variable Integer nHolder = 0;
    Integer n {
        return nHolder;
    }
    assign n {
        nHolder = n;
    }
    assertEquals(0, n);

    // postfix ++
    assertEquals(0, n++);
    assertEquals(1, n);
    n++;
    assertEquals(2, n);

    // postfix --
    assertEquals(2, n--);
    assertEquals(1, n);
    n--;
    assertEquals(0, n);
  
    // prefix ++
    assertEquals(1, ++n);
    assertEquals(1, n);
    ++n;
    assertEquals(2, n);

    // prefix --
    assertEquals(1, --n);
    assertEquals(1, n);
    --n;
    assertEquals(0, n);
  }

  @test
  shared void testBoxedIncrDecr() {
    assertEquals(0, boxedInteger);

    // postfix ++
    assertEquals(0, boxedInteger++);
    assertEquals(1, boxedInteger);
    boxedInteger++;
    assertEquals(2, boxedInteger);

    // postfix --
    assertEquals(2, boxedInteger--);
    assertEquals(1, boxedInteger);
    boxedInteger--;
    assertEquals(0, boxedInteger);
  
    // prefix ++
    assertEquals(1, ++boxedInteger);
    assertEquals(1, boxedInteger);
    ++boxedInteger;
    assertEquals(2, boxedInteger);

    // prefix --
    assertEquals(1, --boxedInteger);
    assertEquals(1, boxedInteger);
    --boxedInteger;
    assertEquals(0, boxedInteger);
  }

  @test
  shared void testUnboxedAttrIncrDecr(){
    assertEquals(0, unboxedAttrIncrDecr);

    // postfix ++
    assertEquals(0, unboxedAttrIncrDecr++);
    assertEquals(1, unboxedAttrIncrDecr);
    unboxedAttrIncrDecr++;
    assertEquals(2, unboxedAttrIncrDecr);

    // postfix --
    assertEquals(2, unboxedAttrIncrDecr--);
    assertEquals(1, unboxedAttrIncrDecr);
    unboxedAttrIncrDecr--;
    assertEquals(0, unboxedAttrIncrDecr);
  
    // prefix ++
    assertEquals(1, ++unboxedAttrIncrDecr);
    assertEquals(1, unboxedAttrIncrDecr);
    ++unboxedAttrIncrDecr;
    assertEquals(2, unboxedAttrIncrDecr);

    // prefix --
    assertEquals(1, --unboxedAttrIncrDecr);
    assertEquals(1, unboxedAttrIncrDecr);
    --unboxedAttrIncrDecr;
    assertEquals(0, unboxedAttrIncrDecr);
  }

  @test
  shared void testUnboxedAttrIncrDecrGetter(){
    assertEquals(0, unboxedAttrIncrDecrGetter);

    // postfix ++
    assertEquals(0, unboxedAttrIncrDecrGetter++);
    assertEquals(1, unboxedAttrIncrDecrGetter);
    unboxedAttrIncrDecrGetter++;
    assertEquals(2, unboxedAttrIncrDecrGetter);

    // postfix --
    assertEquals(2, unboxedAttrIncrDecrGetter--);
    assertEquals(1, unboxedAttrIncrDecrGetter);
    unboxedAttrIncrDecrGetter--;
    assertEquals(0, unboxedAttrIncrDecrGetter);
  
    // prefix ++
    assertEquals(1, ++unboxedAttrIncrDecrGetter);
    assertEquals(1, unboxedAttrIncrDecrGetter);
    ++unboxedAttrIncrDecrGetter;
    assertEquals(2, unboxedAttrIncrDecrGetter);

    // prefix --
    assertEquals(1, --unboxedAttrIncrDecrGetter);
    assertEquals(1, unboxedAttrIncrDecrGetter);
    --unboxedAttrIncrDecrGetter;
    assertEquals(0, unboxedAttrIncrDecrGetter);
  }

  @test
  shared void testUnboxedToplevelAttrIncrDecr(){
    assertEquals(0, numericOperationIncrDecrTestToplevel);

    // postfix ++
    assertEquals(0, numericOperationIncrDecrTestToplevel++);
    assertEquals(1, numericOperationIncrDecrTestToplevel);
    numericOperationIncrDecrTestToplevel++;
    assertEquals(2, numericOperationIncrDecrTestToplevel);

    // postfix --
    assertEquals(2, numericOperationIncrDecrTestToplevel--);
    assertEquals(1, numericOperationIncrDecrTestToplevel);
    numericOperationIncrDecrTestToplevel--;
    assertEquals(0, numericOperationIncrDecrTestToplevel);
  
    // prefix ++
    assertEquals(1, ++numericOperationIncrDecrTestToplevel);
    assertEquals(1, numericOperationIncrDecrTestToplevel);
    ++numericOperationIncrDecrTestToplevel;
    assertEquals(2, numericOperationIncrDecrTestToplevel);

    // prefix --
    assertEquals(1, --numericOperationIncrDecrTestToplevel);
    assertEquals(1, numericOperationIncrDecrTestToplevel);
    --numericOperationIncrDecrTestToplevel;
    assertEquals(0, numericOperationIncrDecrTestToplevel);
  }

  @test
  shared void testUnboxedToplevelAttrIncrDecrGetter(){
    assertEquals(0, numericOperationIncrDecrTestGetter);

    // postfix ++
    assertEquals(0, numericOperationIncrDecrTestGetter++);
    assertEquals(1, numericOperationIncrDecrTestGetter);
    numericOperationIncrDecrTestGetter++;
    assertEquals(2, numericOperationIncrDecrTestGetter);

    // postfix --
    assertEquals(2, numericOperationIncrDecrTestGetter--);
    assertEquals(1, numericOperationIncrDecrTestGetter);
    numericOperationIncrDecrTestGetter--;
    assertEquals(0, numericOperationIncrDecrTestGetter);
  
    // prefix ++
    assertEquals(1, ++numericOperationIncrDecrTestGetter);
    assertEquals(1, numericOperationIncrDecrTestGetter);
    ++numericOperationIncrDecrTestGetter;
    assertEquals(2, numericOperationIncrDecrTestGetter);

    // prefix --
    assertEquals(1, --numericOperationIncrDecrTestGetter);
    assertEquals(1, numericOperationIncrDecrTestGetter);
    --numericOperationIncrDecrTestGetter;
    assertEquals(0, numericOperationIncrDecrTestGetter);
  }

  // Qualified tests

  @test
  shared void testUnboxedQualifiedAttrIncrDecr(){
    assertEquals(0, this.unboxedAttrIncrDecr);

    // postfix ++
    assertEquals(0, this.unboxedAttrIncrDecr++);
    assertEquals(1, this.unboxedAttrIncrDecr);
    this.unboxedAttrIncrDecr++;
    assertEquals(2, this.unboxedAttrIncrDecr);

    // postfix --
    assertEquals(2, this.unboxedAttrIncrDecr--);
    assertEquals(1, this.unboxedAttrIncrDecr);
    this.unboxedAttrIncrDecr--;
    assertEquals(0, this.unboxedAttrIncrDecr);
  
    // prefix ++
    assertEquals(1, ++this.unboxedAttrIncrDecr);
    assertEquals(1, this.unboxedAttrIncrDecr);
    ++this.unboxedAttrIncrDecr;
    assertEquals(2, this.unboxedAttrIncrDecr);

    // prefix --
    assertEquals(1, --this.unboxedAttrIncrDecr);
    assertEquals(1, this.unboxedAttrIncrDecr);
    --this.unboxedAttrIncrDecr;
    assertEquals(0, this.unboxedAttrIncrDecr);
  }

  @test
  shared void testUnboxedQualifiedAttrIncrDecrGetter(){
    assertEquals(0, this.unboxedAttrIncrDecrGetter);

    // postfix ++
    assertEquals(0, this.unboxedAttrIncrDecrGetter++);
    assertEquals(1, this.unboxedAttrIncrDecrGetter);
    this.unboxedAttrIncrDecrGetter++;
    assertEquals(2, this.unboxedAttrIncrDecrGetter);

    // postfix --
    assertEquals(2, this.unboxedAttrIncrDecrGetter--);
    assertEquals(1, this.unboxedAttrIncrDecrGetter);
    this.unboxedAttrIncrDecrGetter--;
    assertEquals(0, this.unboxedAttrIncrDecrGetter);
  
    // prefix ++
    assertEquals(1, ++this.unboxedAttrIncrDecrGetter);
    assertEquals(1, this.unboxedAttrIncrDecrGetter);
    ++this.unboxedAttrIncrDecrGetter;
    assertEquals(2, this.unboxedAttrIncrDecrGetter);

    // prefix --
    assertEquals(1, --this.unboxedAttrIncrDecrGetter);
    assertEquals(1, this.unboxedAttrIncrDecrGetter);
    --this.unboxedAttrIncrDecrGetter;
    assertEquals(0, this.unboxedAttrIncrDecrGetter);
  }
  
  NumericOperationTest getThisOnce(){
    if(incrDecrCounter == 1){
      fail();
    }
    return this;
  }
  
  void resetOnce(){
    incrDecrCounter = 0;
  }

  @test
  shared void testSingleAccessQualifiedAttrIncrDecr(){
    assertEquals(0, this.unboxedAttrIncrDecr);

    // postfix ++
    assertEquals(0, getThisOnce().unboxedAttrIncrDecr++);
    resetOnce();
    assertEquals(1, this.unboxedAttrIncrDecr);
    getThisOnce().unboxedAttrIncrDecr++;
    resetOnce();
    assertEquals(2, this.unboxedAttrIncrDecr);

    // postfix --
    assertEquals(2, getThisOnce().unboxedAttrIncrDecr--);
    resetOnce();
    assertEquals(1, this.unboxedAttrIncrDecr);
    getThisOnce().unboxedAttrIncrDecr--;
    resetOnce();
    assertEquals(0, this.unboxedAttrIncrDecr);
  
    // prefix ++
    assertEquals(1, ++getThisOnce().unboxedAttrIncrDecr);
    resetOnce();
    assertEquals(1, this.unboxedAttrIncrDecr);
    ++getThisOnce().unboxedAttrIncrDecr;
    resetOnce();
    assertEquals(2, this.unboxedAttrIncrDecr);

    // prefix --
    assertEquals(1, --getThisOnce().unboxedAttrIncrDecr);
    resetOnce();
    assertEquals(1, this.unboxedAttrIncrDecr);
    --getThisOnce().unboxedAttrIncrDecr;
    resetOnce();
    assertEquals(0, this.unboxedAttrIncrDecr);
  }
}