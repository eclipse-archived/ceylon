function NatErr(e) {
    var that = new NatErr.$$;
    var msg;
    if (typeof e === 'string') {
        msg = e;
    } else if (e) {
        msg = e.toString();
    } else {
        msg = "Native JavaScript Error";
    }
    Throwable(msg,null,that);
    that.$naterr = e;
    return that;
}
initTypeProto(NatErr, 'ceylon.language::NativeError', $init$Throwable());
NatErr.$$.prototype.printStackTrace = function() {
    console.error(this.$naterr);
}
NatErr.$crtmm$=function(){return{nm:'NativeError',mt:'c',ps:[{$t:{t:Throwable},nm:'src',mt:'prm'}],pa:1,mod:$CCMM$,d:['$','Throwable']};}
ex$.NatErr=NatErr;
