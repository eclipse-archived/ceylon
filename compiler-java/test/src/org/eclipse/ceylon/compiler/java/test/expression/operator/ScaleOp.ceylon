@noanno
class ScaleOp() satisfies Scalable<Float, ScaleOp> {
    shared actual ScaleOp scale(Float other) => nothing;
    
    void m() {
        ScaleOp twice = 2.0 ** this;
        ScaleOp thrice = (2+2) ** this;
    }
}