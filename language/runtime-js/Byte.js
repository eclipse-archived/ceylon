function Byte(bits,that) {
  $init$Byte();
  if (that===undefined)that=new Byte.$$;
  $_Object(that);
  Binary({Other$Binary:{t:Byte}},that);
  Invertible({Other$Invertible:{t:Byte}},that);
  Enumerable({Other$Enumerable:{t:Byte}},that);
  var remainder = bits%256;
  that.val$ = remainder>=0 ? remainder : 256+remainder;
  return that;
}
