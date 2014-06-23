function() {
  var result = "";
  for (var i=this.length; i>0;) {
    var cc = this.charCodeAt(--i);
    if ((cc&0xfc00)!==0xdc00 || i===0) {
      result += this.charAt(i);
    } else {
      result += this.substr(--i, 2);
    }
  }
  return result;
}
