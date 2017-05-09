package ru.eventflow.ccg.annotation.ui.model;

import java.io.File;

public class ContentModel {

    private File file;

    private String content;

    private String contentType;

    public ContentModel(File file, String content, String contentType) {
        this.file = file;
        this.contentType = contentType;
        this.content = content;
    }

    public File getFile() {
        return file;
    }

    public String getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }
}
