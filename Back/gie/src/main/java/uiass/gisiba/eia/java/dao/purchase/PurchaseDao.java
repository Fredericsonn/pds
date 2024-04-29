package uiass.gisiba.eia.java.dao.purchase;

import java.time.LocalDate;
import java.util.List;

import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;

public class PurchaseDao implements iPurchaseDao {

    @Override
    public void getPurchaseById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPurchaseById'");
    }

    @Override
    public void addPurchase(List<PurchaseOrder> orders, LocalDate purchaseDate, double total, Contact suppleir) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPurchase'");
    }

    @Override
    public void deletePurchase(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePurchase'");
    }

}
