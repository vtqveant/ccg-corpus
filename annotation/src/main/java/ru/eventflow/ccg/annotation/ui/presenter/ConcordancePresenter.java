package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.FormSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.FormSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.event.TabEvent;
import ru.eventflow.ccg.annotation.ui.model.Context;
import ru.eventflow.ccg.annotation.ui.view.ConcordanceView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Token;
import ru.eventflow.ccg.datasource.model.corpus.Variant;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConcordancePresenter implements Presenter<ConcordanceView>, FormSelectedEventHandler {

    public static final int WINDOW_SIZE = 5;
    private ConcordanceView view;
    private EventBus eventBus;
    private DataManager dataManager;

    @Inject
    public ConcordancePresenter(final EventBus eventBus, final DataManager dataManager) {
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        this.view = new ConcordanceView();

        this.eventBus.addHandler(FormSelectedEvent.TYPE, this);

        // open tab with sentence on double click
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    Point point = e.getPoint();
                    int idx = table.rowAtPoint(point);
                    int modelIdx = table.convertRowIndexToModel(idx);
                    int sentenceId = (int) table.getModel().getValueAt(modelIdx, 2);
                    Sentence sentence = dataManager.getSentenceById(sentenceId);
                    eventBus.fireEvent(new TabEvent(sentence));
                }
            }
        });
    }

    @Override
    public void onEvent(FormSelectedEvent event) {
        Form form = event.getForm();
        if (form != null) {
            DataLoaderWorker worker = new DataLoaderWorker(form);
            worker.execute();
        } else {
            view.setData(new ArrayList<Context>()); // clear concordancer table
        }
    }


    @Override
    public ConcordanceView getView() {
        return view;
    }

    // TODO refactor!  s. http://stackoverflow.com/questions/17383799/delayed-response-to-jtable-row-selection-event-under-a-huge-data-load/17384208#17384208
    // do this: publish(data); Thread.yield();
    private class DataLoaderWorker extends SwingWorker<List<Sentence>, Void> {

        private final Form form;

        public DataLoaderWorker(Form form) {
            this.form = form;
        }

        @Override
        protected List<Sentence> doInBackground() throws Exception {
            return dataManager.getSentencesByFormOccurence(form);
        }

        @Override
        protected void done() {
            List<Sentence> sentences;
            try {
                List<Context> contexts = new ArrayList<Context>();
                sentences = get();
                for (Sentence sentence : sentences) {
                    LimitedQueue<String> left = new LimitedQueue<String>(WINDOW_SIZE);
                    List<String> right = new LinkedList<String>();
                    boolean found = false;
                    byte counter = 0;
                    List<Token> tokens = sentence.getTokens();
                    Collections.sort(tokens, new Comparator<Token>() {
                        @Override
                        public int compare(Token o1, Token o2) {
                            return o1.getId() - o2.getId();
                        }
                    });
                    for (Token token : tokens) {
                        if (!found) {
                            for (Variant variant : token.getVariants()) {
                                if (variant.getForm() == null) continue;
                                if (variant.getForm().getId() == form.getId()) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                left.add(token.getOrthography());
                            }
                        } else {
                            if (counter < WINDOW_SIZE) {
                                right.add(token.getOrthography());
                                counter++;
                            } else {
                                break;
                            }
                        }
                    }

                    String leftCtx = buildContextString(left);
                    String rightCtx = buildContextString(right);
                    Context context = new Context(leftCtx, form.getOrthography(), rightCtx, sentence.getId(), false);
                    contexts.add(context);
                }
                view.setData(contexts);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        private String buildContextString(List<String> queue) {
            StringBuilder sb = new StringBuilder();
            for (String s : queue) {
                sb.append(s);
                sb.append(' ');
            }
            if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }

    private class LimitedQueue<E> extends LinkedList<E> {
        private int limit;

        public LimitedQueue(int limit) {
            this.limit = limit;
        }

        @Override
        public boolean add(E o) {
            super.add(o);
            while (size() > limit) {
                super.remove();
            }
            return true;
        }
    }
}
