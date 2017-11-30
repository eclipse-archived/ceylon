function(o,skip) {
  if (is$(o,{t:OpenTypeParam$jsint}) && o._fname===this._fname) {
    if (!skip) {
      return $eq$(this.container,o.container);
    }
    return true;
  }
  return false;
}
