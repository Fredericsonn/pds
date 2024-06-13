package uiass.gisiba.eia.java.dao.inventory;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.HQLQueryManager;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.InvalidOrderTypeException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.Status;
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
    }                                     



    @Override
    public Order getOrderById(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException {

        if (HQLQueryManager.validOrderType(orderType)) {

            Order order = null;
        
            if (orderType.equals("Purchase"))  order = (Order) em.find(PurchaseOrder.class, orderId);
    
            else if (orderType.equals("Sale"))  order = (Order) em.find(SaleOrder.class, orderId);
    
            if (order != null) return order;

            throw new OrderNotFoundException(orderId);

        }

        throw new InvalidOrderTypeException(orderType);

    }

    @Override
    public List<Order> getAllOrdersByType(String orderType) {

        Query query = em.createQuery("from " + orderType);

        return query.getResultList();
    }

    @Override
    public List<PurchaseOrder> getAllOrdersByPurchase(int purchaseId) {

        Purchase purchase = em.find(Purchase.class, purchaseId);
        
        String hql = "from Purchase_Order p where p.purchase = :purchase";

        Query query = em.createQuery(hql);

        query.setParameter("purchase", purchase);

        return query.getResultList();
    }

    @Override
    public List<Order> orderSearchFilter(String orderType, Map<String, String> criteria) throws InvalidOrderTypeException {

        if (HQLQueryManager.validOrderType(orderType)) {

            String hql = HQLQueryManager.orderSelectHQLQueryGenerator(orderType , criteria);

            Query query = em.createQuery(hql);

            for (String column : criteria.keySet()) {

                query.setParameter(column, criteria.get(column));

            }

            return query.getResultList();
        }

        throw new InvalidOrderTypeException(orderType);
    }

    @Override
    public void addPurchaseOrder(InventoryItem product, Time orderTime, int quantity, Purchase purchase) {

        PurchaseOrder order = new PurchaseOrder(product, orderTime, quantity);

        order.setPurchase(purchase);

        tr.begin();

        em.persist(order);

        tr.commit();
    }

    @Override
    public void addSaleOrder(InventoryItem product, Time orderTime, int quantity, Sale sale, double margin) {

        SaleOrder order = new SaleOrder(product, orderTime, quantity, margin);

        order.setSale(sale);

        tr.begin();

        em.persist(order);

        tr.commit();
    }

    @Override
    public void deleteOrder(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException  {

        Order order = getOrderById(orderId, orderType);

        tr.begin();

        em.remove(order);

        tr.commit();

    }

    @Override
    public void updateOrder(int orderId, int quantity, String orderType) throws InvalidOrderTypeException, OrderNotFoundException {

        Order order = getOrderById(orderId, orderType);

        order.setQuantity(quantity);

        tr.begin();

        em.persist(order);

        tr.commit();
    }















}
