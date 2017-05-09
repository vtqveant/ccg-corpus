package ru.eventflow.ccg.annotation.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class FileSystemProjectManager implements ProjectManager {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemProjectManager.class.getName());

    private final File projectFolder;
    private FolderElement root;

    public FileSystemProjectManager(File projectFolder) {
        this.projectFolder = projectFolder;
    }

    private static String slurp(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }
        return sb.toString();
    }

    public File getProjectFolder() {
        return projectFolder;
    }

    @Override
    public List<ProjectElement> getChildren(final ProjectElement element) {
        List<ProjectElement> children = new ArrayList<>();
        try {
            File base = new File(projectFolder, element.getPath());
            File[] files = base.listFiles();

            if (files != null) {
                for (File f : files) {
                    if (f.isHidden()) {
                        continue;
                    }
                    int prefixLength = projectFolder.getCanonicalPath().length();
                    String path = f.getCanonicalPath().substring(prefixLength).replaceAll(Pattern.quote(File.separator), "/");
                    ProjectElement child = (f.isDirectory() ? new FolderElement(path) : new FileElement(path));
                    children.add(child);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        children.sort(Comparator.comparing(ProjectElement::getPath));
        return children;
    }

    @Override
    public String getContentAsString(FileElement element) {
        String content = null;
        try {
            content = slurp(getFile(element));
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
        return content;
    }

    @Override
    public InputStream getContent(FileElement element) {
        InputStream in = null;
        try {
            in = new FileInputStream(getFile(element));
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
        return in;
    }

    @Override
    public File getFile(ProjectElement element) {
        return new File(projectFolder, element.getPath());
    }

    @Override
    public ProjectElement getRootElement() {
        if (root == null) {
            root = new FolderElement("/");
//            attach(root);
        }
        return root;
    }

    private void attach(ProjectElement element) {
        for (ProjectElement child : getChildren(element)) {
            element.addChild(child);
            attach(child);
        }
    }
}
