package uiass.gisiba.eia.java.dao.inventory;

import java.time.LocalTime;
import java.util.List;

import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.sales.Sale;

public interface iOrderDao {

    Order getOrderById(int orderId, String orderType) throws ClassNotFoundException, OrderNotFoundException;

    List<Order> getAllOrders(String orderType);
    
    void addPurchaseOrder(Product product, int quantity, LocalTime orderTime, Purchase purchase);

    void addSaleOrder(Product product, int quantity, LocalTime orderTime, Sale sale);

    void deleteOrder(int orderId, String orderType) throws ClassNotFoundException, OrderNotFoundException;

    void updateOrder(int orderId, int quantity, String orderType) throws ClassNotFoundException, OrderNotFoundException;
}
