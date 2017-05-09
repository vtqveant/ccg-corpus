package ru.eventflow.ccg.annotation.ui.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.EditorCaretEvent;
import ru.eventflow.ccg.annotation.ui.event.SaveFileEvent;
import ru.eventflow.ccg.annotation.ui.event.SaveFileEventHandler;
import ru.eventflow.ccg.annotation.ui.model.ContentModel;
import ru.eventflow.ccg.annotation.ui.view.FileEditorView;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.FileWriter;
import java.io.IOException;

public class FileEditorPresenter implements Presenter<FileEditorView>, FocusListener, CaretListener,
        SaveFileEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(FileEditorPresenter.class.getName());

    private final FileEditorView view;
    private final EventBus eventBus;

    private ContentModel model;

    public FileEditorPresenter(final EventBus eventBus) {
        this.eventBus = eventBus;
        this.view = new FileEditorView();

        eventBus.addHandler(SaveFileEvent.TYPE, this);

        this.view.getTextPane().addCaretListener(this);
        this.view.getTextPane().addFocusListener(this);
    }

    public void setModel(ContentModel model) {
        this.model = model;
        view.getTextPane().setContentType(model.getContentType());
        view.getTextPane().setText(model.getContent());
        view.getTextPane().setCaretPosition(0);
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

    @Override
    public void onEvent(SaveFileEvent event) {
        try {
            FileWriter writer = new FileWriter(model.getFile());
            view.getTextPane().write(writer);
            writer.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public FileEditorView getView() {
        return view;
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
