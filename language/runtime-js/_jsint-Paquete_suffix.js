if (this.$suff!==undefined)return this.$suff;
var suffix = '';
if (this.name!==this.container.name) {
  var _s = this.name.substring(this.container.name.size);
  suffix = _s.replace(/\./g, '$');
}
this.$suff=suffix;
return suffix;
