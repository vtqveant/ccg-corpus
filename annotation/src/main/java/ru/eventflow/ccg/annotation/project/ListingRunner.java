package ru.eventflow.ccg.annotation.project;

import java.io.File;

public class ListingRunner {

    private static final File RESOURCES_PATH = new File(ListingRunner.class.getResource("/").getFile());

    public static void main(String[] args) {
        ProjectElement projectElement = new FileSystemProjectManager(RESOURCES_PATH).getRootElement();
        projectElement.accept(new PathPrinterVisitor());
    }

    private static class PathPrinterVisitor implements Visitor {
        @Override
        public void visit(FolderElement element) {
            System.out.format("%3d %s\r\n", element.getChildren().size(), element.getPath());
        }

        @Override
        public void visit(FileElement element) {
            System.out.format("    %s\r\n", element.getPath());
        }
    }

}
