package ru.eventflow.ccg.annotation.project;

public interface Visitor {
    void visit(FolderElement element);
    void visit(FileElement element);
}
