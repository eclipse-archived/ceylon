function(index){
  if (index<0 || index>=this.length)
    return getFinished();
  if (this._bumps===undefined)
    this._bumps=[];
  var cnt=0;
  var mb=0;
  for (var i=0;i<this._bumps.length;i++) {
    mb=this._bumps[i];
    if (mb<index)cnt++;
  }
  if (index<=this._maxidx) {
    index+=cnt;
    return Character(codepointFromString(this, index));
  }
  if (this._maxidx>mb)
    mb=this._maxidx;
  for (cnt=mb; cnt<index; cnt++) {
    if ((this.charCodeAt(mb)&0xfc00) === 0xd800) {
      this._bumps.push(mb);
      ++mb;
    }
    if (++mb >= this.length)
      return getFinished();
  }
  if (this._maxidx===undefined || mb>this._maxidx)
    this._maxidx=mb;
  return Character(codepointFromString(this, mb));
}
