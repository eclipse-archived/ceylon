class Widget<X>() {}

interface Bucket<out X> {}

interface Wrong<out X> {}

@error abstract class WidgetBucket<in X>() 
        satisfies Bucket<Widget<X>> & Wrong<X> {
    @error shared Wrong<X> wrong;
    @error shared Bucket<Widget<X>> bucket;
}

