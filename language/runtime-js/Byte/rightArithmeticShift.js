function(bits) {
  return Byte(this.signed>>(bits&7));
}
