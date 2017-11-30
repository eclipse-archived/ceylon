var tp=getrtmm$$(this.container.tipo).tp[this._fname];
if (tp) {
  return tp['def'] !== undefined;
}
throw new Error("Invalid Type Parameter");
