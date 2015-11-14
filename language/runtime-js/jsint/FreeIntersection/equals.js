function(u) {
  if(is$(u,{t:FreeIntersection$jsint})) {
    var mine=this.satisfiedTypes;
    var his=u.satisfiedTypes;
    if (mine.size==his.size) {
      for (var i=0;i<mine.size;i++) {
        if (!his.contains(mine.$_get(i))){
          return false;
        }
      }
      return true;
    }
  }
  return false;
}
