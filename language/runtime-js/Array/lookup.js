function(i){
  if (i>=0 && i<this.size) {
    var e=this.$_get(i);
    if (e===null) {
      return Entry(true,null,{Key$Entry:{t:$_Boolean},Item$Entry:{t:Null}});
    }
    return Entry(true,e,{Key$Entry:{t:$_Boolean},Item$Entry:{t:this.$$targs$$.Element$Array}});
  }
  return Entry(false,null,{Key$Entry:{t:$_Boolean},Item$Entry:{t:Null}});
}
