package ru.eventflow.ccg.annotation.ui.component;

public interface LazyJTableDataSource {

    Object[][] retrieveRows(int from, int to);

    int getTotalRowCount();

}
