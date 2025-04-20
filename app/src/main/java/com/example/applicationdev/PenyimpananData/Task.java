package com.example.applicationdev.PenyimpananData;

public class Task {
    public String id;
    public String title;
    public boolean done;
    public String priority;

    public Task() {

    }

    public Task(String id, String title, boolean done, String priority) {
        this.id = id;
        this.title = title;
        this.done = done;
        this.priority = priority;
    }
}
