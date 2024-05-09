package uiass.gisiba.eia.java.dao.inventory;

import java.sql.Time;
import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.InvalidOrderTypeException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.entity.sales.Sale;

public interface iOrderDao {

    Order getOrderById(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;

    List<Order> getAllOrdersByType(String orderType);

    List<Order> orderSearchFilter(String orderType, Map<String,String> criteria) throws InvalidOrderTypeException;

    List<PurchaseOrder> getAllOrdersByPurchase(int purchaseId);

    void addPurchaseOrder(InventoryItem product, int quantity, Time orderTime, Purchase purchase);

    void addSaleOrder(InventoryItem product, int quantity, Time orderTime, Sale sale);

    void deleteOrder(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;

    void updateOrder(int orderId, int quantity, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;
}
