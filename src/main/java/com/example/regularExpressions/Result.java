package com.example.regularExpressions;
import java.util.List;

public class Result {
    private boolean success;
    private String message;
    private List<Match> matches;
    private String highlightedText;

    public Result() {}

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(boolean success, String message, List<Match> matches, String highlightedText) {
        this.success = success;
        this.message = message;
        this.matches = matches;
        this.highlightedText = highlightedText;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Match> getMatches() { return matches; }
    public void setMatches(List<Match> matches) { this.matches = matches; }

    public String getHighlightedText() { return highlightedText; }
    public void setHighlightedText(String highlightedText) { this.highlightedText = highlightedText; }
}
