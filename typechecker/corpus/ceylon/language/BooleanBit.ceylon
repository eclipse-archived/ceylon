shared extension class BooleanSlot(Boolean this)
        satisfies FixedSlots<BooleanSlot> {

    shared extension Boolean boolean { return this; }

    shared actual BooleanSlot or(BooleanSlot bit) {
        if (this) {
            return this;
        }
        else {
            return bit;
        }
    }

    shared actual BooleanSlot and(BooleanSlot bit) {
        if (this) {
            return bit;
        }
        else {
            return false;
        }
    }

    shared actual BooleanSlot xor(BooleanSlot bit) {
        if (this) {
            return bit.complement;
        }
        else {
            return bit;
        }
    }

    shared actual BooleanSlot complement(BooleanSlot bit) {
        if (this) {
            return bit.complement;
        }
        else {
            return this;
        }
    }

    shared actual BooleanSlot complement {
        return BooleanBit(!this);
    }

    shared actual Gettable<Boolean> value(BooleanSlot index) {
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

    shared actual BooleanSlot lastIndex {
        return 0;
    }

}