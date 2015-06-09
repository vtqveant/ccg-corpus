package ru.eventflow.ccg.annotation.ui.component;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class LazyJTableModel implements TableModel {

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

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }

    public void setTableDataSource(LazyJTableDataSource tableDataSource) {
        this.cache = new LazyJTableCache(rowCount, tableDataSource);
    }

}
