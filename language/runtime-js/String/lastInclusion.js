function (e) {
  if (is$(e, {t:$_String})) {
    //TODO: handle codepoints correctly
    var i = this.lastIndexOf(e);
    return i<=0 ? null : i;
  }
  else {
    //TODO: fix
    return this.tipo.$crtmm$['super'].lastInclusion(e);
  }
}