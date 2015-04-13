if (this.value==304) return Character(105); //lowercase dotted I to i
var lcstr = codepointToString(this.value).toLowerCase();
return countCodepoints(lcstr)==1 ? Character(codepointFromString(lcstr, 0)) : this;
