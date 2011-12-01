@nomodel
class Bug241<T>() {
    
}

@nomodel
void bug241<Key,Item>() given Key satisfies Equality given Item satisfies Equality  {
    value builder = Bug241<Entry<Key,Item>>();   
}