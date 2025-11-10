package com.example.regularExpressions.Models;

public class ValidationRequest {
    private String pattern;
    private String text;

    // Getters and Setters
    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
