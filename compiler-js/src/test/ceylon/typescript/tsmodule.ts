export const constEmptyString: string = "";
export var varEmptyString: string = "";
export function getVarEmptyString(): string {
    return varEmptyString;
}
export function stringIdentity(s: string): string {
    return s;
}
export function numberIdentity(n: number): number {
    return n;
}
export function booleanIdentity(b: boolean) { // return type omitted: tests type inference support
    return b;
}
export function voidFunction(value: any): void {
    // do nothing
}
export interface IStringBox {
    s: string;
    setS(s: string): void;
    getS(): string;
    self(): IStringBox;
}
export class StringBox {
    s: string;
    constructor(s: string) {
        this.s = s;
    }
    setS(s: string): void {
        this.s = s;
    }
    getS(): string {
        return this.s;
    }
    self(): StringBox {
        return this;
    }
}
export function makeIStringBox(s: string): IStringBox {
    return new StringBox(s);
}
export enum NonConstEnum {
    Zero,
    One,
    Two = 2,
    Four = 4,
    Three = 3,
    FortyTwo = 42,
    ThirteenDotThreeSeven = 13.37,
    MinusOne = -1,
    Eight = 1 << 3,
    Twelve = Four | Eight,
    CounterFromThousand = 1000
}
export function incrementNonConstEnumCounter(): void {
    eval("(function(nce){nce.CounterFromThousand++})")(NonConstEnum);
}
export const enum ConstEnum {
    Zero,
    One,
    Two = 2,
    Four = 4,
    Three = 3,
    FortyTwo = 42,
    ThirteenDotThreeSeven = 13.37,
    MinusOne = -1,
    Eight = 1 << 3,
    Twelve = Four | Eight
}
