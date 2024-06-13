package uiass.gisiba.eia.java.dao.sale;

import java.sql.Date;
import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.InsufficientQuantityInStock;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidFilterCriteriaMapFormatException;
import uiass.gisiba.eia.java.dao.exceptions.OperationNotModifiableException;
import uiass.gisiba.eia.java.dao.exceptions.SaleNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.inventory.Status;

import uiass.gisiba.eia.java.entity.sales.Sale;
import uiass.gisiba.eia.java.entity.sales.SaleOrder;

public interface iSaleDao {

    Sale getSaleById(int id) throws SaleNotFoundException;

    void addSale(Sale sale) throws InsufficientQuantityInStock;

    void deleteSale(int id) throws SaleNotFoundException;

    List<Sale> getAllSales();

    List<Sale> getAllSalesByContactType(String type) throws InvalidContactTypeException;

    public List<Sale> getAllSalesByCustomer(String name, String customerType);

    List<Contact> getAllCustomers(String customerType) throws InvalidContactTypeException;

    List<Sale> getAllSalesByStatus(Status status);

    List<Sale> salesDatesFilter(Map<String,Date> datesCriteria) throws InvalidFilterCriteriaMapFormatException;

    List<Sale> salesFilter(Map<String, Object> criteria) throws InvalidFilterCriteriaMapFormatException;

    List<SaleOrder> getSaleOrders(int id) throws SaleNotFoundException;

    void updateSaleOrders(int id, List<SaleOrder> newOrders) throws SaleNotFoundException,
    
            OperationNotModifiableException;

    void updateSaleStatus(int id, Status status) throws SaleNotFoundException;
}
