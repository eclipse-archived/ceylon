if (this.tipo) {
  var mm = getrtmm$$(this.tipo);
  if (typeof(mm.$t)==='string') {
    return OpenTvar$jsint(OpenTypeParam$jsint(mm.$cont,mm.$t));
  }
  return _openTypeFromTarg(mm.$t, this.tipo);
}
throw Error("OpenValue.openType-we don't have a metamodel!");
