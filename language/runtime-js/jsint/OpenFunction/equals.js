function(o) {
  if (is$(o,{t:OpenFunction$jsint})) {
    return o.tipo === this.tipo;
  }
  return false;
}
