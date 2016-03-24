function(o,skip) {
  if (is$(o,{t:OpenTypeParam$jsint}) && o._fname===this._fname) {
    if (!skip) {
      var mc=this.container,oc=o.container;
      return mc.equals(oc);
    }
    return true;
  }
  return false;
}
