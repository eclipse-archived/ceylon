
@error interface Xa satisfies Ya {}
@error interface Ya => Za;
@error interface Za satisfies Xa {}

@error class Xb() extends Yb() {}
@error class Yb() => Zb();
@error class Zb() extends Xb() {}

@error alias Xc => Yc;
alias Yc => Zc;
alias Zc => Xc;