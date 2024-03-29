package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.ui.component.LazyJTableDataSource;
import ru.eventflow.ccg.annotation.ui.model.ContextEntry;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Token;
import ru.eventflow.ccg.datasource.model.corpus.Variant;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import java.util.*;

public class ConcordanceLazyTableDataSource implements LazyJTableDataSource {

    public static final int WINDOW_SIZE = 8;

    private List<Sentence> sentences;
    private Form form;

    public ConcordanceLazyTableDataSource(List<Sentence> sentences, Form form) {
        this.sentences = sentences;
        this.form = form;
    }

    private static Object[][] toArray(List<ContextEntry> contextEntries) {
        int size = contextEntries.size();
        Object[][] results = new Object[size][4];
        for (int i = 0; i < size; i++) {
            ContextEntry contextEntry = contextEntries.get(i);
            results[i][0] = contextEntry.getLeft() + " ";
            results[i][1] = contextEntry.getOccurence() + "  " + contextEntry.getRight();
            results[i][2] = contextEntry.getSentenceId();
            results[i][3] = contextEntry.isApproved();
        }
        return results;
    }

    public int getTotalRowCount() {
        return sentences.size();
    }

    @Override
    public Object[][] retrieveRows(int from, int to) {
        final List<ContextEntry> contextEntries = new ArrayList<>();
        for (Sentence sentence : sentences.subList(from, to)) {
            Collections.sort(sentence.getTokens(), new Comparator<Token>() {
                @Override
                public int compare(Token o1, Token o2) {
                    return o1.getPosition() - o2.getPosition();
                }
            });

            String occurence = null;
            LimitedQueue<String> left = new LimitedQueue<String>(WINDOW_SIZE);
            List<String> right = new LinkedList<String>();
            boolean found = false;
            byte counter = 0;
            for (Token token : sentence.getTokens()) {
                if (!found) {
                    for (Variant variant : token.getVariants()) {
                        if (variant.getForm() == null) continue;
                        if (variant.getForm().getId() == form.getId()) {
                            found = true;
                            occurence = variant.getToken().getOrthography();
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
            ContextEntry contextEntry = new ContextEntry(leftCtx, occurence, rightCtx, sentence.getId(), false);
            contextEntries.add(contextEntry);
        }
        return toArray(contextEntries);
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

    private class LimitedQueue<T> extends LinkedList<T> {
        private int limit;

        public LimitedQueue(int limit) {
            this.limit = limit;
        }

        @Override
        public boolean add(T o) {
            super.add(o);
            while (size() > limit) {
                super.remove();
            }
            return true;
        }
    }

}
