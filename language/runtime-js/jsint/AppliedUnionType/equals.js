function(u) {
  if(is$(u,{t:AppliedUnionType$jsint})) {
    var mine=this.caseTypes;
    var his=u.caseTypes;
    if (mine.size==his.size) {
      for (var i=0;i<mine.size;i++) {
        if (!his.contains(mine.$_get(i)))return false;
      }
      return true;
    }
  }
  return false;
}
