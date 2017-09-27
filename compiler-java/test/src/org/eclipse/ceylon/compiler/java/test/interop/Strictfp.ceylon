import java.lang{strictfp}

@noanno
class Strictfp() {
    shared variable strictfp Float s = 1.0+1.0;
    shared strictfp Float t => s;
    assign t {
        s = t;
    }
    shared strictfp Float m(Float x=1.0) {
        return s+t;
    }
}
@noanno
strictfp class StrictfpClass() {
}
@noanno
strictfp interface StrictfpInterface {
    shared Float m() {
        return 1.0;
    }
}
@noanno
strictfp Float strictfpGetter => 0.0;
assign strictfpGetter {}
@noanno
strictfp variable Float strictfpVariable = 0.0;
@noanno
strictfp Float strictfpFunction() => 0.0;
@noanno
strictfp object strictfpObject {}