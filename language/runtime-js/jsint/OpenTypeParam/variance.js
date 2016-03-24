var tp=getrtmm$$(this.container.tipo).tp[this._fname];
if (tp) {
  if (tp.dv==='out')return covariant$meta$declaration();
  if (tp.dv=== 'in')return contravariant$meta$declaration();
  return invariant$meta$declaration();
}
throw new Error("Invalid Type Parameter");
