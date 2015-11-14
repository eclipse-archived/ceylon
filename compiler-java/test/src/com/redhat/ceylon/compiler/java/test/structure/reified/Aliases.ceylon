shared class TargetClass(){}

shared class ClassAlias<T>() => TargetClass();

shared interface TargetInterface{}

shared interface InterfaceAlias<T> => TargetInterface;

shared alias TypeAlias<T> => TargetClass & TargetInterface;