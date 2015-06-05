package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.view.SearchView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * http://stackoverflow.com/questions/13517980/how-i-handle-keypress-event-for-jcombobox-in-java
 */
public class SearchPresenter implements Presenter<SearchView> {

    private SearchView view;
    private EventBus eventBus;
    private DataManager dataManager;

    @Inject
    public SearchPresenter(final EventBus eventBus, final DataManager dataManager) {
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        this.view = new SearchView();

        view.getCombo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) view.getCombo().getSelectedItem();
                if (s == null || s.length() < 2) {
                    return;
                }
                for (int i = 0; i < view.getCombo().getItemCount(); i++) {
                    if (view.getCombo().getItemAt(i).toString().equals(s)) {
                        return;
                    }
                }
                // TODO use prefix tree instead
                List<String> orthographies = dataManager.getOrthographies(s.toLowerCase());
                for (String orthography : orthographies) {
                    if (view.getComboBoxModel().getIndexOf(orthographies) == -1) {
                        view.getComboBoxModel().addElement(orthography);
                    }
                }
                view.getCombo().showPopup();
            }
        });
    }

    @Override
    public SearchView getView() {
        return view;
    }
}
