var tp=getrtmm$$(this.container.tipo).tp[this._fname];
if (typeof(tp.def)==='string') {
  return OpenTvar$jsint(OpenTypeParam$jsint(this.container.tipo, tp.def));
}
return tp.def?_openTypeFromTarg(tp.def,this.container.tipo):null;
