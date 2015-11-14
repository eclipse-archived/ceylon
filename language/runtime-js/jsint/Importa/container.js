if (this._cont===undefined) {
  this._cont = modules$meta().find(this.name,this.version);
}
return this._cont;
