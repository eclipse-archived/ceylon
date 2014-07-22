function Byte(bits,that) {
  $init$Byte();
  if (that===undefined)that=new Byte.$$;
  $_Object(that);
  Binary({Other$Binary:{t:Byte}},that);
  Invertible({Other$Invertible:{t:Byte}},that);
  Ordinal({Other$Ordinal:{t:Byte}},that);
  that.val$=Math.abs(bits)%256;
  return that;
}
