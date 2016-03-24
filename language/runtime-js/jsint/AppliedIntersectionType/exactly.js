function exactly(type$25){
  if (is$(type$25, {t:AppliedIntersectionType$jsint})) {
    for (var i=0; i<this.satisfiedTypes.size;i++) {
      var myt = this.satisfiedTypes.$_get(i);;
      var was=false;
      for (var j=0; j<type$25.satisfiedTypes.size;j++) {
        was |= myt.exactly(type$25.satisfiedTypes.$_get(j));
      }
      if (!was)return false;
    }
    //Now the other way around
    for (var i=0; i<type$25.satisfiedTypes.size;i++) {
      var myt = type$25.satisfiedTypes.$_get(i);
      var was=false;
      for (var j=0; j<this.satisfiedTypes.size;j++) {
        was |= myt.exactly(this.satisfiedTypes.$_get(j));
      }
      if (!was)return false;
    }
    return true;
  }
  return false;
}
