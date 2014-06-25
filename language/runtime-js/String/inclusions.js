function (e) {
  if (is$(e, {t:$_String})) {
    //TODO: handle codepoints
    var indexes = [];
    var i=0;
    while ((i=this.indexOf(e,i))>=0) {
      indexes.push(i);
      i+=e.length;
    }
    return indexes.reifyCeylonType({t:Integer});
  }
  else {
    //TODO: fix
    return this.tipo.$crtmm$['super'].inclusions(e);
  }
}