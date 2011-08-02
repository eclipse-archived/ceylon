void thisSuperOuter() {
    
    @error writeLine(super.string);
    @error writeLine(this.string);
    @error writeLine(outer.string);
    @error value s = super;
    @error value o = outer;
    @error value t = this;
    
    class OuterClass() {
        void method() {
            writeLine(super.string);
            writeLine(this.string);
            @error writeLine(outer.string);
            @error value ss = super;
            @error value oo = outer;
            @type["OuterClass"] value tt = this;
        }
        class InnerClass() {
            void method() {
                writeLine(super.string);
                writeLine(this.string);
                writeLine(outer.string);
                @error value ss = super;
                @type["OuterClass"] value oo = outer;
                @type["OuterClass.InnerClass"] value tt = this;
            }
        }
    }

}