function(bits) {
  if (bits>7)return Byte(0);
  if (bits<1)return this;
  return Byte(this.integer<<bits);
}
