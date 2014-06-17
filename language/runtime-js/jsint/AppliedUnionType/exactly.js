function exactly(type$22){
  if (is$(type$25, {t:AppliedUnionType$jsint})) {
    for (var i=0; i<this.caseTypes.size;i++) {
      var myt = this.caseTypes.$_get(i);
      var was=false;
      for (var j=0; j<type$25.caseTypes.size;j++) {
        was |= myt.exactly(type$25.caseTypes.$_get(j));
      }
      if (!was)return false;
    }
    //Now the other way around
    for (var i=0; i<type$25.caseTypes.size;i++) {
      var myt = type$25.caseTypes.$_get(i);
      var was=false;
      for (var j=0; j<this.caseTypes.size;j++) {
        was |= myt.exactly(this.caseTypes.$_get(j));
      }
      if (!was)return false;
    }
    return true;
  }
  return false;
}
