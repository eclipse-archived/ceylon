@nomodel
class Bug241<T>() {
    
}

@nomodel
void bug241<Key,Item>() given Key satisfies Sized given Item satisfies Sized  {
    value builder = Bug241<Entry<Key,Item>>();   
}