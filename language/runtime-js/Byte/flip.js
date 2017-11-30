function(i) {
  if (i<0||i>7)return this;
  var bit = this.val$&(1<<i);
  if (bit>0) {
    return Byte(this.val$&(0xff^(1<<i)));
  } else {
    return Byte(this.val$|(1<<i));
  }
}
