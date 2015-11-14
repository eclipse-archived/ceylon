function(o) {
  return is$(o,{t:AppliedClass$jsint}) && o.tipo===this.tipo &&
    this.typeArgumentWithVariances.equals(o.typeArgumentWithVariances);
};
