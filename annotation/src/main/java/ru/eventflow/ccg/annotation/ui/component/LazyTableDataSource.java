package ru.eventflow.ccg.annotation.ui.component;

public interface LazyTableDataSource {

    Object[][] retrieveRows(int from, int to);

}
