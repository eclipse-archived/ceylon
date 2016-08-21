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
