var tp=getrtmm$$(this.container).$tp[this._fname];
if (tp) {
  return tp['def'] !== undefined;
}
throw new Error("Invalid Type Parameter");
