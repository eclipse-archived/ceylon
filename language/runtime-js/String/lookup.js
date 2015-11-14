function(i){
  if (i>0 && i<this.size) {
    return Entry(true,this.$_get(i),{Key$Entry:{t:$_Boolean},Item$Entry:{t:Character}});
  }
  return Entry(false,null,{Key$Entry:{t:$_Boolean},Item$Entry:{t:Null}});
}

