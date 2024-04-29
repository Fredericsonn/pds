package uiass.gisiba.eia.java.dao.purchase;

import java.time.LocalDate;
import java.util.List;

import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;

public interface iPurchaseDao {

    void getPurchaseById(int id);

    void addPurchase(List<PurchaseOrder> orders, LocalDate purchaseDate, double total, Contact suppleir);

    void deletePurchase(int id);
}
