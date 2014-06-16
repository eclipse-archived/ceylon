if (this._cont===undefined) {
  this._cont = getModules$meta().find(this.name,this.version);
}
return this._cont;
