if (typeof(this._anns)==='function')this._anns=this._anns();
if (this._anns)for (var i=0;i<this._anns.length;i++) {
  if (this._anns[i]===shared)return true;
}
return false;
