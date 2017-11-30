function(sub) {
    var str;
    if (sub.constructor === String) {str = sub;}
    else if (sub.constructor === Character.$$) {str = codepointToString(sub.value);}
    else {return false;}
    return this.indexOf(str) >= 0;
}
