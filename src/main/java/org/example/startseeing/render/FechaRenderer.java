package org.example.startseeing.render;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FechaRenderer extends DefaultTableCellRenderer {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        if (value instanceof LocalDateTime) {
            value = ((LocalDateTime) value).format(FORMATTER);
        }

        return super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
        );
    }

}
