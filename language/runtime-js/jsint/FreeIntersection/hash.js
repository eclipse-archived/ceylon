var s=this.satisfiedTypes.size;
for (var i=0;i<this.satisfiedTypes.size;i++) {
  s+=this.satisfiedTypes.$_get(i).string.hash;
}
return s;
