package ru.eventflow.ccg.annotation.ui.component;

import ru.eventflow.ccg.annotation.ui.model.Context;

import java.util.List;

public class MyLazyTableDataSource implements LazyTableDataSource {

    List<Context> contexts;

    public MyLazyTableDataSource(List<Context> contexts) {
        this.contexts = contexts;
    }

    @Override
    public Object[][] retrieveRows(int from, int to) {
        List<Context> sublist = contexts.subList(from, to);
        int size = sublist.size();
        Object[][] results = new Object[size][4];
        for (int i = 0; i < size; i++) {
            Context context = sublist.get(i);
            results[i][0] = context.getLeft();
            results[i][1] = context.getOccurence() + " " + context.getRight();
            results[i][2] = context.getSentenceId();
            results[i][3] = context.isApproved();
        }
        return results;
    }

}
