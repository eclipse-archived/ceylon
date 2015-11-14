var h=this.satisfiedTypes.size;
for (var i=0; i<this.satisfiedTypes.size;i++) {
  h+=this.satisfiedTypes.$_get(i).hash;
}
return h;
