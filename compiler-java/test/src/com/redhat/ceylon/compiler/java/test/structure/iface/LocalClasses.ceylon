@noanno
interface LocalClasses {
    shared String f() {
        class LocalClass() {
            
        }
        value lc = LocalClass();
        value getter => "";
        assign getter {}
        object o {}
        return lc.string + getter + o.string + object {}.string;
    }
}