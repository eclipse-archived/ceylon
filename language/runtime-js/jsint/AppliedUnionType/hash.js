var h=this.caseTypes.size;
for (var i=0; i<this.caseTypes.size;i++) {
  h+=this.caseTypes.$_get(i).hash;
}
return h;
