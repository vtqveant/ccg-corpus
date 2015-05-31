package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.view.AnnotationView;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Token;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;

import java.util.ArrayList;
import java.util.List;

public class AnnotationPresenter implements Presenter<AnnotationView> {

    private AnnotationView view;
    private EventBus eventBus;

    public AnnotationPresenter(final EventBus eventBus, Sentence sentence) {
        this.view = new AnnotationView();
        this.eventBus = eventBus;

        List<String> tokens = new ArrayList<>();
        List<String> glosses = new ArrayList<>();
        for (Token token : sentence.getTokens()) {
            tokens.add(token.getOrthography());

            Form form = token.getVariants().get(0).getForm();
            if (form == null) {
                glosses.add("OOV");
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
}
