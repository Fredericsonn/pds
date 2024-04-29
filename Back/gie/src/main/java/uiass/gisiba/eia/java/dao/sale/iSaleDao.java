package uiass.gisiba.eia.java.dao.sale;

import java.time.LocalDate;
import java.util.List;

import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.sales.SaleOrder;
import uiass.gisiba.eia.java.entity.sales.SaleState;

public interface iSaleDao {

    void getSaleById(int id);

    void addSale(List<SaleOrder> orders, LocalDate saleDate, double total, SaleState state, Contact customer);

    void deleteSale(int id);
}
