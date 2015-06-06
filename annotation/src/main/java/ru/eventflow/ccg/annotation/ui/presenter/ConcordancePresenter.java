package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.FormSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.FormSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.model.Context;
import ru.eventflow.ccg.annotation.ui.view.ConcordanceView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Token;
import ru.eventflow.ccg.datasource.model.corpus.Variant;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import java.util.*;

public class ConcordancePresenter implements Presenter<ConcordanceView> {

    public static final int WINDOW_SIZE = 5;
    private ConcordanceView view;
    private EventBus eventBus;
    private DataManager dataManager;

    @Inject
    public ConcordancePresenter(final EventBus eventBus, final DataManager dataManager) {
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        this.view = new ConcordanceView();

        this.eventBus.addHandler(FormSelectedEvent.TYPE, new FormSelectedEventHandler() {
            @Override
            public void onEvent(FormSelectedEvent e) {
                Form form = e.getForm();

                List<Context> contexts = new ArrayList<Context>();

                if (e.getForm() != null) {

                    // TODO fetch sentences containing the form, build a list of contexts, set the table model
                    // TODO refactor!
                    List<Sentence> sentences = dataManager.getSentencesByFormOccurence(form);

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

                }

                view.setData(contexts);
            }
        });
    }

    private static String buildContextString(List<String> queue) {
        StringBuilder sb = new StringBuilder();
        for (Iterator it = queue.iterator(); it.hasNext(); ) {
            sb.append(it.next());
            sb.append(' ');
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public ConcordanceView getView() {
        return view;
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
