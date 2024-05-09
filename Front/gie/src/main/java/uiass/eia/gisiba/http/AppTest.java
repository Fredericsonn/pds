package uiass.eia.gisiba.http;

import java.util.HashMap;
import java.util.Locale.Category;
import java.util.Map;

import uiass.eia.gisiba.http.dto.CategoryDto;
import uiass.eia.gisiba.http.dto.InventoryDto;
import uiass.eia.gisiba.http.dto.OrderDto;
import uiass.eia.gisiba.http.dto.ProductDto;
import uiass.eia.gisiba.http.dto.PurchaseDto;
import uiass.eia.gisiba.http.parsers.Parser;
import uiass.eia.gisiba.http.parsers.ProductParser;
import uiass.eia.gisiba.http.parsers.PurchaseParser;

public class AppTest {

    public static void main(String[] args) {

        /*Map<String,Object> map = new HashMap<String,Object>();

        map.put("categoryName", "CAMERA");

        map.put("brandName", "Panasonic");

        String json = Parser.jsonGenerator(map);*/

        //System.out.println(ProductDto.getFilteredProducts(json));

        System.out.println(PurchaseDto.getAllPurchases());
    }
}
