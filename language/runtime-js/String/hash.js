if (this._hash === undefined) {
  var h=0;
  for (var i = 0; i < this.length; i++) {
    var c = this.charCodeAt(i);
    h=(31*h+c)&0xffffffff;
  }
  this._hash=h;
}
return this._hash;
