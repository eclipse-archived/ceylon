@test
shared void bytes() {
  check(Byte(0).integer==0, "byte 0");
  check(Byte(255).integer==255, "byte 255");
  check(Byte(256).integer==0, "byte 256");
  check(Byte(65535).integer==255, "byte 65535");
  check(Byte(0).or(Byte(1))==Byte(1), "byte 0|1");
  check(Byte(15).or(Byte(5))==Byte(15), "byte 15|5");
  check(Byte(7).and(Byte(1))==Byte(1), "byte 7&2");
  check(Byte(7).and(Byte(64))==Byte(0), "byte 7&64");
  check(Byte(3).xor(Byte(1))==Byte(2), "byte 3^1");
  check(Byte(3).xor(Byte(2))==Byte(1), "byte 3^2");
  check(Byte(5).xor(Byte(10))==Byte(15), "byte 5^10");
  check(Byte(127).negated==Byte(127), "byte negated");
  check(Byte(127)+Byte(128)==Byte(255), "byte 127+128");
  check(Byte(250)+Byte(10)==Byte(4), "byte 250+10");
  check(Byte(499)+Byte(1)==Byte(244), "byte 499+1");
  check(Byte(15).flip(3)==Byte(7), "15 flip 3");
  check(Byte(255).flip(4)==Byte(239), "255 flip 4");
  check(Byte(1).flip(3)==Byte(9), "1 flip 3");
  check(Byte(15).get(3), "15 get 4");
  check(!Byte(15).get(4), "15 get 5");
  check(Byte(7).set(3,true)==Byte(15), "7.set(4,t)");
  check(Byte(128).set(7,false)==Byte(0), "128.set(7,f)");
  check(!Byte(15)==Byte(240), "byte !15");
  check(Byte(170).hash==170, "byte.hash");
  check(Byte(85).string=="85", "byte.string");
  check(Byte(1).leftLogicalShift(3) == Byte(8), "1<<3");
  check(Byte(8).rightLogicalShift(3) == Byte(1), "8>>3");
  check(Byte(255).leftLogicalShift(9) == Byte(0), "255<<9");
  check(Byte(255).rightLogicalShift(9) == Byte(0), "255>>9");
  check(Byte(255).rightLogicalShift(4) == Byte(15), "255>>4");
  check(Byte(255).rightArithmeticShift(4)==Byte(255).rightLogicalShift(4), "equal right shifts");
}
