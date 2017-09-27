import rx { Observable }

shared void run(){
    Observable<Integer> obs = nothing;
    value mappedObservable = obs.map( (Integer k) {
        print("map is called and return ``k*10``");
        return k*10;
    });
}