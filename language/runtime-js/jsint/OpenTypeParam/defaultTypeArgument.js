var tp=getrtmm$$(this.container).$tp[this._fname];
if (typeof(tp.def)==='string') {
  return OpenTvar$jsint(OpenTypeParam$jsint(this.container, tp.def));
}
return tp.def?_openTypeFromTarg(tp.def,this.container):null;
