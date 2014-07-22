function(idx) {
  if (idx>0 && idx<8) {
    return (this.integer&(1<<idx))>0;
  }
  return false;
}
