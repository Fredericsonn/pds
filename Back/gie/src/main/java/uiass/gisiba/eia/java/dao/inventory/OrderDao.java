package uiass.gisiba.eia.java.dao.inventory;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.entity.sales.Sale;
import uiass.gisiba.eia.java.entity.sales.SaleOrder;

public class OrderDao implements iOrderDao {

    private EntityManager em;
	private EntityTransaction tr;
    
    public OrderDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
    }                                       // orders/:orderType/:orderId

    @Override
    public Order getOrderById(int orderId, String orderType) throws ClassNotFoundException, OrderNotFoundException {

        Class type = Class.forName(orderType);

        tr.begin();

        Order order = (Order) em.find(type, orderId);

        tr.commit();

        if (order != null) return order;

        throw new OrderNotFoundException(orderId);

    }

    @Override
    public List<Order> getAllOrders(String orderType) {

        Query query = em.createQuery("from " + orderType);

        return query.getResultList();
    }

    @Override
    public void addPurchaseOrder(Product product, int quantity, LocalTime orderTime, Purchase purchase) {

        PurchaseOrder order = new PurchaseOrder(product, quantity, orderTime, purchase);

        tr.begin();

        em.persist(order);

        tr.commit();
    }

    @Override
    public void addSaleOrder(Product product, int quantity, LocalTime orderTime, Sale sale) {

        SaleOrder order = new SaleOrder(product, quantity, orderTime, sale);

        tr.begin();

        em.persist(order);

        tr.commit();
    }

    @Override
    public void deleteOrder(int orderId, String orderType) throws ClassNotFoundException, OrderNotFoundException  {

        Order order = getOrderById(orderId, orderType);

        tr.begin();

        em.remove(order);

        tr.commit();

    }

    @Override
    public void updateOrder(int orderId, int quantity, String orderType) throws ClassNotFoundException, OrderNotFoundException {

        Order order = getOrderById(orderId, orderType);

        order.setQuantity(quantity);

        tr.begin();

        em.persist(order);

        tr.commit();
    }



}
