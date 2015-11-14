var check = this.value & 0xfffffff0;
if (check in Character.dig$) {
  return (this.value&0xf) <= 9;
}
if ((check|6) in Character.dig$) {
  return (this.value&0xf) >= 6;
}
return this.value>=0x1d7ce && this.value<=0x1d7ff;
