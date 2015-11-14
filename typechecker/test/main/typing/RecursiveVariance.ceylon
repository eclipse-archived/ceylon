class Widget<X>() {}

interface Bucket<out X> {}

interface Pile<out X> {}

@error abstract class WidgetBucket<in X>() 
        satisfies Bucket<Widget<X>> & Pile<X> {
    @error shared Pile<X> wrong;
    @error shared Bucket<Widget<X>> bucket;
}

class Better<out T>() satisfies Pile<Bucket<T>> {
	shared Pile<Bucket<T>> ok() {
		Pile<Bucket<T>> w = this;
		return w;
	}
}
@error class Worse<out T>() extends Widget<Bucket<T>>() {
	Widget<Bucket<T>> notok() {
		Widget<Bucket<T>> w = this;
		return w;
	}
}
