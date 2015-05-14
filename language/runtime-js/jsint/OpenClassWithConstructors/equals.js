function(other) {
  if (is$(other, {t:OpenClass$jsint})) {
    if (other.tipo===this.tipo)return true;
    if (this.anonymous && other.anonymous) {
      //Anonymous classes sometimes have a ref to the class constructor
      //and sometimes to the value getter, depending on how it was obtained
      return this.qualifiedName.equals(other.qualifiedName);
    }
  }
  return false;
}
