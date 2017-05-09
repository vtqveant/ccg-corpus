package ru.eventflow.ccg.annotation.project;

import java.util.ArrayList;
import java.util.List;

public abstract class ProjectElement {

    private String path;

    private List<ProjectElement> children = new ArrayList<>();

    public ProjectElement(String path) {
        this.path = path;
    }

    public void accept(Visitor visitor) {
        for (ProjectElement child : children) {
            child.accept(visitor);
        }
    }

    public void addChild(ProjectElement child) {
        children.add(child);
    }

    public String getPath() {
        return path;
    }

    public List<ProjectElement> getChildren() {
        return children;
    }

    public String getName() {
        String[] parts = path.split("/");
        return (parts.length > 0 ? parts[parts.length - 1] : null);
    }
}
