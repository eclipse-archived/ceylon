import com.redhat.sample.multisource.domain { Order }

shared class OrderService() {
    Order createOrder() {
        return Order();
    }
}