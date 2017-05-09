package ru.eventflow.ccg.annotation.project;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ProjectManager {

    ProjectElement getRootElement();

    List<ProjectElement> getChildren(ProjectElement element);

    InputStream getContent(FileElement element);

    String getContentAsString(FileElement element);

    File getFile(ProjectElement element);
}
