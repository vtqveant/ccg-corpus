package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.EditorCaretEvent;
import ru.eventflow.ccg.annotation.ui.view.AnnotationView;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Token;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import java.util.ArrayList;
import java.util.List;

public class AnnotationPresenter implements Presenter<AnnotationView> {

    private AnnotationView view;
    private EventBus eventBus;

    public AnnotationPresenter(final EventBus eventBus, Sentence sentence) {
        this.view = new AnnotationView();
        this.eventBus = eventBus;

        this.view.getTextPane().addCaretListener(new ProxyCaretListener());

        List<String> tokens = new ArrayList<>();
        List<String> glosses = new ArrayList<>();
        for (Token token : sentence.getTokens()) {
            tokens.add(token.getOrthography());

            Form form = token.getVariants().get(0).getForm();
            if (form == null) {
                glosses.add("oov");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Grammeme grammeme : form.getGrammemes()) {
                    sb.append(grammeme.getName());
                    sb.append('.');
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                glosses.add(sb.toString());
            }
        }
        view.addGlosses(tokens, glosses);
    }

    @Override
    public AnnotationView getView() {
        return view;
    }

    private class ProxyCaretListener implements CaretListener {

        @Override
        public void caretUpdate(CaretEvent e) {
            JTextPane editor = (JTextPane) e.getSource();
            int row = getRow(e.getDot(), editor);
            int column = getColumn(e.getDot(), editor);
            eventBus.fireEvent(new EditorCaretEvent(row, column));
        }

        private int getRow(int pos, JTextComponent editor) {
            int rn = (pos == 0) ? 1 : 0;
            try {
                int offs = pos;
                while (offs > 0) {
                    offs = Utilities.getRowStart(editor, offs) - 1;
                    rn++;
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            return rn;
        }

        private int getColumn(int pos, JTextComponent editor) {
            try {
                return pos - Utilities.getRowStart(editor, pos) + 1;
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }
}
