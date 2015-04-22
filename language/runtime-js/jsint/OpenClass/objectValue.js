if (!this.anonymous)return null;
if (this.container && this.container.getMember) {
  var m=this.container.getMember(this.name,{getMember$Kind:{t:ValueDeclaration$meta$declaration}});
  if (m)return m;
}
return null
