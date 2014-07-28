var tp=getrtmm$$(this.container).tp[this._fname];
if (tp) {
  if (tp.dv==='out')return getCovariant$meta$declaration();
  if (tp.dv=== 'in')return getContravariant$meta$declaration();
  return getInvariant$meta$declaration();
}
throw new Error("Invalid Type Parameter");
