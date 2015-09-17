function(o){
  if (is$(o,{t:AppliedMemberClass$jsint}) && o.tipo===this.tipo) {
    var eq;
    if (this.$bound) {
      eq= o.$bound && o.$bound.equals(this.$bound);
    } else {
      eq= o.$bound===undefined;
    }
    if (eq && this.typeArgumentWithVariances.equals(o.typeArgumentWithVariances)) {
      if (this.container.tipo===o.container.tipo) {
        //This will check for type arguments etc
        return this.container.equals(o.container);
      } else {
        //Can't compare different parent types
        return true;
      }
    }
  }
  return false;
}
