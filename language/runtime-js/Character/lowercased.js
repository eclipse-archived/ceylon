var lcstr = codepointToString(this.value).toLowerCase();
return countCodepoints(lcstr)==1 ? Character(codepointFromString(lcstr, 0)) : this;
