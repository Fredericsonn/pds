package uiass.gisiba.eia.java.dao.sale;

import java.time.LocalDate;
import java.util.List;

import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.sales.SaleOrder;
import uiass.gisiba.eia.java.entity.sales.Status;

public class SaleDao implements iSaleDao {

    @Override
    public void getSaleById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSaleById'");
    }

    @Override
    public void addSale(List<SaleOrder> orders, LocalDate saleDate, double total, Status state, Contact customer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addSale'");
    }

    @Override
    public void deleteSale(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSale'");
    }

}
