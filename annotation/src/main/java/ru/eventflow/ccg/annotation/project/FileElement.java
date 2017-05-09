package ru.eventflow.ccg.annotation.project;

public class FileElement extends ProjectElement {

    public FileElement(String path) {
        super(path);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        super.accept(visitor);
    }
}
