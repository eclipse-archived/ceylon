@nomodel
shared void nestedGenericEntryInstantiation<T>(T t) given T satisfies Equality {
    value entry = Entry<Integer, Entry<T, String>>(1, Entry<T, String>(t, ""));
    Exception e = Exception("", null);
    value entry2 = Entry<Exception, Entry<T, Exception>>(e, Entry<T, Exception>(t, e));
}

