package ru.eventflow.ccg.annotation.ui.model;

public class ContextEntry {
    private String left;
    private String occurence;
    private String right;
    private int sentenceId;
    private boolean approved;

    public ContextEntry(String left, String occurence, String right, int sentenceId, boolean approved) {
        this.left = left;
        this.occurence = occurence;
        this.right = right;
        this.sentenceId = sentenceId;
        this.approved = approved;
    }

    public String getLeft() {
        return left;
    }

    public String getOccurence() {
        return occurence;
    }

    public String getRight() {
        return right;
    }

    public int getSentenceId() {
        return sentenceId;
    }

    public boolean isApproved() {
        return approved;
    }

}
