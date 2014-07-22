function(i,b) {
  if (i<0 || i>7)return this;
  if (b)return Byte(this.integer|(1<<i));
  return Byte(this.integer&(0xff^(1<<i)));
}
