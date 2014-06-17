var tp=getrtmm$$(this.container).$tp[this._fname];
if (tp.of) {
  var a=[];
  for (var i=0;i<tp.of.length;i++) {
    var _ct=tp.of[i];if (typeof(_ct)==='function')_ct=getrtmm$$(_ct).$t;
    a.push(_openTypeFromTarg(_ct,this.container));
  }
  return a.length===0?getEmpty():ArraySequence(a,{Element$ArraySequence:{t:OpenType$meta$declaration}});
}
return getEmpty();
