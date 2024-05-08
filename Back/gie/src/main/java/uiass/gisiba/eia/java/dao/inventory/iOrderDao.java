package uiass.gisiba.eia.java.dao.inventory;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import uiass.gisiba.eia.java.dao.exceptions.InvalidOrderTypeException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.sales.Sale;

public interface iOrderDao {

    Order getOrderById(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;

    List<Order> getAllOrdersByType(String orderType);

    List<Order> getAllOrdersByStatus(String orderType, Status status) throws InvalidOrderTypeException;

    List<Order> getAllOrdersBetweenDates(String orderType, Date startDate, Date endDate) throws InvalidOrderTypeException;

    void addPurchaseOrder(Product product, int quantity, Time orderTime, Purchase purchase);

    void addSaleOrder(Product product, int quantity, Time orderTime, Sale sale);

    void deleteOrder(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;

    void updateOrder(int orderId, int quantity, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;
}
