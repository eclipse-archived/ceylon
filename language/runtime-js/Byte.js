function Byte(bits,that) {
  $i$Byte();
  if (that===undefined)that=new Byte.$$;
  $_Object(that);
  Binary({Other$Binary:{t:Byte}},that);
  Invertible({Other$Invertible:{t:Byte}},that);
  Enumerable({Other$Enumerable:{t:Byte}},that);
  that.val$ = (bits % 0x100) & 0xFF;
  return that;
}
