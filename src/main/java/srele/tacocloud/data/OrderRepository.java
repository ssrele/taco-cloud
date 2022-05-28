package srele.tacocloud.data;

import srele.tacocloud.Order;

public interface OrderRepository {
    Order   save(Order order);
}
