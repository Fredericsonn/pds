package uiass.eia.gisiba;

import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

public class TableViewCellFactory {

    public static void setTextCellFactory(TableColumn<List<String>, String> column) {
        // Use TextFieldTableCell for text cells
        column.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String fromString(String string) {
                return string;
            }

            @Override
            public String toString(String object) {
                return object;
            }
        }));
    }
}

