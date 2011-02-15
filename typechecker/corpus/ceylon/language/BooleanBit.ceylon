shared extension class BooleanBit(Boolean this)
        satisfies Bits<1> {

    shared extension Boolean boolean { return this; }

    shared actual Bits<1> or(Bits<1> bit) {
        if (this) {
            return this;
        else {
            return bit;
        }
    }

    shared actual Bits<1> and(Bits<1> bit) {
        if (this) {
            return bit;
        else {
            return false;
        }
    }

    shared actual Bits<1> xor(Bits<1> bit) {
        if (this) {
            return bit.complement;
        else {
            return bit;
        }
    }

    shared actual Bits<1> complement(Bits<1> bit) {
        if (this) {
            return bit.complement;
        else {
            return this;
        }
    }

    shared actual Bits<1> complement {
        return BooleanBit(!this);
    }

    shared actual Gettable<Boolean> value(Bounded<1> index) {
        return this;
    }

    shared actual Gettable<Boolean?> value(Natural key) {
        Boolean? result;
        if (key==0) {
            result = boolean;
        }
        else {
            result = null;
        }
        return result;
    }

    shared actual Bounded<1> lastIndex {
        return 0;
    }

}