interface Hierarchy {
    
    interface Producer<out T> {}
    interface Consumer<in T> {}
    
    class Super() {}
    class Sub() extends Super() {}
    class Other() {}
    
    interface SuperProducer satisfies Producer<Super> {}
    interface SubProducer satisfies Producer<Sub> {}
    interface OtherProducer satisfies Producer<Other> {}
    
    class Good() satisfies SuperProducer & SubProducer {}
    
    @error class Bad() satisfies SubProducer & OtherProducer {}
}