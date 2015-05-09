class XXX() {
    class WWW() {}
    class YYY() extends XXX() {
        @error class ZZZ() extends WWW() {}
        String name = "";
        @error String name2 = YYY().name;
        @error String name3 = this.YYY().name;
        @error String name4 = super.YYY().name;
        String name5 = outer.YYY().name;
        @error String name6 = YYY().YYY().name;
        class VVV() extends WWW() {}
        @error class UUU() extends YYY() {}
        //class UUU() extends outer.YYY() {} //would be OK
    }
}