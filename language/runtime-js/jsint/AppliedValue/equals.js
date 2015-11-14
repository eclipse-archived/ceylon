function(o) {
  if (is$(o,{t:AppliedValue$jsint}) && this.tipo===o.tipo) {
    if (this.obj)return this.obj.equals(o.obj);
    if (this.container)return this.container.equals(o.container);
    return true;
  }
  return false;
}
