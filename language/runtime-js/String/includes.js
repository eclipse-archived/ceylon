function (e) {
  if (is$(e, {t:$_String})) {
    return this.indexOf(e)>=0;
  }
  else {
    //TODO: fix
    return this.tipo.$crtmm$['super'].includes(e);
  }
}