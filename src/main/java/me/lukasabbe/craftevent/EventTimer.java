package me.lukasabbe.craftevent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EventTimer {

    private List<Integer> timers = new ArrayList<>();
    private final List<Runnable> taskList = new ArrayList<>();

    public EventTimer(){
        startTimer();
    }

    private void startTimer(){
        CraftEvent
                .instance
                .getServer()
                .getScheduler()
                .scheduleSyncRepeatingTask(
                        CraftEvent.instance,
                        () -> {
                            timers = timers.stream().map(t -> t + 1).collect(Collectors.toList());
                            taskList.forEach(Runnable::run);
                        },
                        20,
                        20);
    }

    public int addTimer(){
        int index = timers.size();
        timers.add(0);
        return index;
    }
    public void removeTimer(int id){
        timers.remove(id);
    }
    public int getTimeOfTimer(int id){
        return timers.get(id);
    }
    public void addTickListener(Runnable task){
        taskList.add(task);
    }
}
