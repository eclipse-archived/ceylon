function(idx) {
  if (idx>0 && idx<8) {
    return (this.val$&(1<<idx))>0;
  }
  return false;
}
