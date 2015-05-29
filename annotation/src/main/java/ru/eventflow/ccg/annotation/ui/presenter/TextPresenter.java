package ru.eventflow.ccg.annotation.ui.presenter;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.view.TextView;
import ru.eventflow.ccg.datasource.model.corpus.Paragraph;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Comparator;

public class TextPresenter implements Presenter<TextView> {

    private TextView view;
    private EventBus eventBus;

    private EventList<Sentence> sentences;


    @Inject
    public TextPresenter(final EventBus eventBus) {
        this.view = new TextView();
        this.eventBus = eventBus;

        // setup the model
        this.sentences = new BasicEventList<Sentence>();
        SortedList<Sentence> sortedSentences = new SortedList<Sentence>(sentences, new SentenceComparator());
        AdvancedTableModel<Sentence> issuesTableModel = GlazedListsSwing.eventTableModelWithThreadProxyList(sortedSentences,
                new SentenceTableFormat());
        view.getTable().setModel(issuesTableModel);

        TableComparatorChooser<Sentence> tableSorter =
                TableComparatorChooser.install(view.getTable(), sortedSentences, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE);

        initHandlers();
    }

    private void initHandlers() {
        this.eventBus.addHandler(TextSelectedEvent.TYPE, new TextSelectedEventHandler() {
            @Override
            public void onEvent(TextSelectedEvent e) {
                sentences.clear();
                if (e.getText() == null) return;  // this is the root node
                for (Paragraph p : e.getText().getParagraphs()) {
                    sentences.addAll(p.getSentences());
                }
            }
        });

//        this.view.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                int row = view.getTable().getSelectedRow();
//                String id = view.getTable().getValueAt(row, 0).toString();
//                eventBus.fireEvent(new StatusUpdateEvent(id));
//            }
//        });
    }

    @Override
    public TextView getView() {
        return view;
    }

    private class SentenceComparator implements Comparator<Sentence> {
        @Override
        public int compare(Sentence o1, Sentence o2) {
            if (o1.getParagraph().getId() < o2.getParagraph().getId()) return -1;
            return o1.getId() - o2.getId();
        }
    }

    private class SentenceTableFormat implements TableFormat<Sentence> {

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) return "ID";
            else if (column == 1) return "Source";

            throw new IllegalStateException();
        }

        @Override
        public Object getColumnValue(Sentence sentence, int column) {
            if (column == 0) return sentence.getId();
            else if (column == 1) return sentence.getSource();

            throw new IllegalStateException();
        }
    }


}
