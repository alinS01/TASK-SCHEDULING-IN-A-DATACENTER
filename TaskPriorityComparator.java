import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if (task1.getPriority() < task2.getPriority()) {
            return 1;
        } else if (task1.getPriority() > task2.getPriority()) {
            return -1;
        } else {
            return Integer.compare(task1.getStart(), task2.getStart());
        }
    }
}
