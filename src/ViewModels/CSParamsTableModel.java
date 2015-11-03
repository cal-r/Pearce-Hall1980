package ViewModels;

import StringConstants.GuiConstants;
import StringConstants.TableConstants;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Objects;

/**
 * Created by Rokas on 03/11/2015.
 */
public class CSParamsTableModel extends BaseTableModel {

    @Override
    protected String[] getColumnHeaders() {
        String[] columnHeaders = {TableConstants.PARAMETER, TableConstants.VALUE};
        return columnHeaders;
    }

    @Override
    protected Object[][] getInitialData() {
        Object[][] data = {
                { "", ""}
        };
        return data;
    }
}
