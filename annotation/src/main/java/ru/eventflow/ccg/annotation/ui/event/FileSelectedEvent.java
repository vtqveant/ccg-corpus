package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;
import ru.eventflow.ccg.annotation.project.ProjectElement;

import java.io.File;

public class FileSelectedEvent extends Event<FileSelectedEventHandler> {

    public final static Type<FileSelectedEventHandler> TYPE = new Type<>();
    private File base;
    private ProjectElement projectElement;
    private RequestType requestType;

    public FileSelectedEvent(File base, ProjectElement projectElement, RequestType requestType) {
        this.base = base;
        this.projectElement = projectElement;
        this.requestType = requestType;
    }

    public File getBase() {
        return base;
    }

    public ProjectElement getProjectElement() {
        return projectElement;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    @Override
    public Type<FileSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FileSelectedEventHandler handler) {
        handler.onEvent(this);
    }

    public enum RequestType {FILE, PROJECT}
}
