<<<<<<< HEAD
if (this.length==0) return this;
var i = (this.charCodeAt(0)&0xfc00) === 0xd800 ? 2 : 1;
return $_String(this.substring(i));
=======
return $_String(this.substring((this.charCodeAt(0)&0xfc00) === 0xd800 ?2:1));
>>>>>>> parent of 232d871c14... Revert "narrow type of String.exceptLast + fix impl of String.rest on JS #5641"
