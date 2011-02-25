shared extension class Bit(Boolean this)
        satisfies FixedSlots<Bit> {

    shared extension Boolean boolean { return this; }

    shared actual Bit or(Bit bit) {
        if (this) {
            return this;
        }
        else {
            return bit;
        }
    }

    shared actual Bit and(Bit bit) {
        if (this) {
            return bit;
        }
        else {
            return false;
        }
    }

    shared actual Bit xor(Bit bit) {
        if (this) {
            return bit.complement;
        }
        else {
            return bit;
        }
    }

    shared actual Bit complement(Bit bit) {
        if (this) {
            return bit.complement;
        }
        else {
            return this;
        }
    }

    shared actual Bit complement {
        return Bit(!this);
    }

}