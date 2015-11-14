var s=this.caseTypes.size;
for (var i=0;i<this.caseTypes.size;i++) {
  s+=this.caseTypes.$_get(i).string.hash;
}
return s;
