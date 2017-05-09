package ru.eventflow.ccg.annotation.project;

public class FolderElement extends ProjectElement {

    public FolderElement(String path) {
        super(path);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        super.accept(visitor);
    }
}
