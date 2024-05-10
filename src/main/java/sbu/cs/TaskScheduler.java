package sbu.cs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskScheduler
{
    public static class Task implements Runnable
    {
        /*
            ------------------------- You don't need to modify this part of the code -------------------------
         */
        String taskName;
        int processingTime;

        public Task(String taskName, int processingTime) {
            this.taskName = taskName;
            this.processingTime = processingTime;
        }
        /*
            ------------------------- You don't need to modify this part of the code -------------------------
         */

        @Override
        public void run() {
            try {
                Thread.sleep(processingTime);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static ArrayList<String> doTasks(ArrayList<Task> tasks)
    {
        ArrayList<String> finishedTasks = new ArrayList<>();
        List<Task> sorted_tasks = new ArrayList<>(tasks);
        Collections.sort(sorted_tasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return Integer.compare(o1.processingTime ,o2.processingTime);
            }
        });
        sorted_tasks = sorted_tasks.reversed();

        List<Thread> threads = new ArrayList<>();
        for(int i = 0; i < sorted_tasks.size(); i++){
            Thread t = new Thread(sorted_tasks.get(i));
            threads.add(t);
        }

        for (int i = 0; i < sorted_tasks.size(); i++){
            threads.get(i).start();
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            finishedTasks.add(sorted_tasks.get(i).taskName);
        }

        /*
        TODO
            Create a thread for each given task, And then start them based on which task has the highest priority
            (highest priority belongs to the tasks that take more time to be completed).
            You have to wait for each task to get done and then start the next task.
            Don't forget to add each task's name to the finishedTasks after it's completely finished.
         */

        return finishedTasks;
    }

    public static void main(String[] args) {
        // Test your code here
    }
}
