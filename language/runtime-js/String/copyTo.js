function(d,sp,dp,l){
  if (sp===undefined)sp=0;
  if (dp===undefined)dp=0;
  if (l===undefined)l=Math.min(this.size-sp,d.size-dp);
  if (l===0)return;
  for (var i=sp;i<sp+l;i++){
    d.set(dp++,this.$_get(i));
  }
}
