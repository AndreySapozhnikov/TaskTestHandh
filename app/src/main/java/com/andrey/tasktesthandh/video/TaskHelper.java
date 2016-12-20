package com.andrey.tasktesthandh.video;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;


public class TaskHelper {

    public final WeakHashMap<String, WeakReference<Task>> tasks = new WeakHashMap<String, WeakReference<Task>>();
    private static TaskHelper instance;

    public static TaskHelper getInstance() {
        if (instance == null) {
            synchronized (TaskHelper.class) {
                if (instance == null) {
                    instance = new TaskHelper();
                }
            }
        }

        return instance;
    }

    public Task getTask(String key) {
        return tasks.get(key) == null ? null : tasks.get(key).get();
    }

    public void addTask(String key, Task response) {
        addTask(key, response, null);
    }

    public void addTask(String key, Task response, Activity o) {
        detach(key);
        tasks.put(key, new WeakReference<Task>(response));

        if (o != null) {
            attach(key, o);
        }
    }

    public void removeTask(String key) {
        detach(key);
        tasks.remove(key);
    }

    public void detach(String key) {
        if (tasks.containsKey(key) && tasks.get(key) != null && tasks.get(key).get() != null) {
            tasks.get(key).get().detach();
        }
    }

    public void attach(String key, Activity o) {
        Task handler = getTask(key);
        if (handler != null) {
            handler.attach(o);
        }
    }
}