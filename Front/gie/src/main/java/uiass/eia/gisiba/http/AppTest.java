package uiass.eia.gisiba.http;

import java.util.Locale.Category;

import uiass.eia.gisiba.http.dto.CategoryDto;
import uiass.eia.gisiba.http.dto.ProductDto;
import uiass.eia.gisiba.http.parsers.ProductParser;

public class AppTest {

    public static void main(String[] args) {

        System.out.println(ProductDto.getAllProducts());
    }
}
