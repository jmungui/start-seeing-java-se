package org.example.startseeing.render;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class BooleanSiNoRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        if (value instanceof Boolean) {
            setText((Boolean) value ? "Sí" : "No");
        } else {
            setText("");
        }

        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }

}
