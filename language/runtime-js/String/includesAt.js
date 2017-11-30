function (i,e) {
  if (e.size===0)return true;
  if (is$(e, {t:$_String})) {
    if (i<0 || i>this.size-e.size)return false;
    return cmpSubString(this,e,i);
  }
  else {
    return SearchableList.$$.prototype.includesAt.call(this,i,e);
  }
}
