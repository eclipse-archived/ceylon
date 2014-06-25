function (i,e) {
  if (is$(e, {t:$_String})) {
    //TODO: handle codepoints correctly
    return this.indexOf(e)==i;
  }
  else {
    //TODO: fix
    return this.tipo.$crtmm$['super'].includesAt(i,e);
  }
}