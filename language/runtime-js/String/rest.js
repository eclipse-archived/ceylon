if (this.length==0) return this;
var i = (this.charCodeAt(0)&0xfc00) === 0xd800 ? 2 : 1;
return $_String(this.substring(i));
