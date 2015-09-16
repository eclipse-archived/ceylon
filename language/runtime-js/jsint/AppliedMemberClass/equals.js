function(o){
  var eq=is$(o,{t:AppliedMemberClass$jsint}) && o.tipo===this.tipo;
  if (this.$bound) {
    eq=eq && o.$bound && o.$bound.equals(this.$bound);
  } else {
    eq=eq && o.$bound===undefined;
  }
  return eq && this.typeArgumentWithVariances.equals(o.typeArgumentWithVariances);
}
