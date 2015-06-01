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
import javax.swing.text.Utilities;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

public class AnnotationPresenter implements Presenter<AnnotationView>, FocusListener, CaretListener {

    private AnnotationView view;
    private EventBus eventBus;

    public AnnotationPresenter(final EventBus eventBus, Sentence sentence) {
        this.view = new AnnotationView();
        this.eventBus = eventBus;

        this.view.getTextPane().addCaretListener(this);
        this.view.getTextPane().addFocusListener(this);

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

    @Override
    public void caretUpdate(CaretEvent e) {
        fireCurrentCaretPosition();
    }

    @Override
    public void focusGained(FocusEvent e) {
        fireCurrentCaretPosition();
    }

    @Override
    public void focusLost(FocusEvent e) {
        // nothing
    }

    private void fireCurrentCaretPosition() {
        JTextPane editor = view.getTextPane();
        int pos = editor.getCaretPosition();
        int row = (pos == 0) ? 1 : 0;
        int column = -1;
        try {
            int offs = pos;
            while (offs > 0) {
                offs = Utilities.getRowStart(editor, offs) - 1;
                row++;
            }
            column = pos - Utilities.getRowStart(editor, pos) + 1;
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        eventBus.fireEvent(new EditorCaretEvent(row, column));
    }

}