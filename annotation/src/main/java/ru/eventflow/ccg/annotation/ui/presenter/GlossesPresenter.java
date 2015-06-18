package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.ui.view.GlossesView;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Token;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GlossesPresenter implements Presenter<GlossesView> {

    private GlossesView view;

    public GlossesPresenter(Sentence sentence) {
        this.view = new GlossesView();

        List<String> tokens = new ArrayList<>();
        List<String> glosses = new ArrayList<>();

        Collections.sort(sentence.getTokens(), new Comparator<Token>() {
            @Override
            public int compare(Token o1, Token o2) {
                return o2.getPosition() - o1.getPosition();
            }
        });

        for (Token token : sentence.getTokens()) {
            tokens.add(token.getOrthography());

            Form form = token.getVariants().get(0).getForm();
            if (form == null) {
                glosses.add("oov");
            } else {
                // TODO refac
                List<String> grammemes = new ArrayList<>();
                for (Grammeme grammeme : form.getLexeme().getLemma().getGrammemes()) {
                    if (!grammemes.contains(grammeme.getName())) {
                        grammemes.add(grammeme.getName());
                    }
                }
                for (Grammeme grammeme : form.getGrammemes()) {
                    if (!grammemes.contains(grammeme.getName())) {
                        grammemes.add(grammeme.getName());
                    }
                }
                Collections.sort(grammemes);

                StringBuilder sb = new StringBuilder();
                for (String name : grammemes) {
                    sb.append(name);
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
    public GlossesView getView() {
        return view;
    }
}
