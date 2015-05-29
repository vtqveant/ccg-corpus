package ru.eventflow.ccg.datasource;

import ru.eventflow.ccg.datasource.model.corpus.Text;

import java.util.List;

public interface DataManager {

    List<Text> getAllTexts();
}
