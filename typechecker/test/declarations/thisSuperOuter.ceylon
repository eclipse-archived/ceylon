void thisSuperOuter() {
    
    @error print(super.string);
    @error print(this.string);
    @error print(outer.string);
    @error value s = super;
    @error value o = outer;
    @error value t = this;
    
    class OuterClass() {
        void method() {
            print(super.string);
            print(this.string);
            @error print(outer.string);
            @error value ss = super;
            @error value oo = outer;
            @type["OuterClass"] value tt = this;
        }
        class InnerClass() {
            void method() {
                print(super.string);
                print(this.string);
                print(outer.string);
                @error value ss = super;
                @type["OuterClass"] value oo = outer;
                @type["OuterClass.InnerClass"] value tt = this;
            }
        }
    }

}