package ru.eventflow.ccg.annotation.ui.component;

import javax.swing.table.AbstractTableModel;

public class LazyJTableModel extends AbstractTableModel {

    private LazyJTableCache cache;
    private String[] columnNames;
    private Class[] columnClasses;
    private int rowCount;

    public LazyJTableModel(String[] columnNames, Class[] columnClasses, int rowCount) {
        this.columnNames = columnNames;
        this.columnClasses = columnClasses;
        this.rowCount = rowCount;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex < columnNames.length) {
            return columnNames[columnIndex];
        }
        return null;
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex < columnClasses.length) {
            return columnClasses[columnIndex];
        }
        return null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return cache.retrieveRowFromCache(rowIndex)[columnIndex];
    }

    public void setTableDataSource(LazyJTableDataSource tableDataSource) {
        this.cache = new LazyJTableCache(rowCount, tableDataSource);
    }

}
