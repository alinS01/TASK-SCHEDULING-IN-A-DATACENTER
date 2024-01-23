import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class MyHost extends Host {
    // Coada blocanta pentru stocarea sarcinilor cu prioritati
    private final BlockingQueue<Task> taskQueue = new PriorityBlockingQueue<>(10, new TaskPriorityComparator());
    private AtomicBoolean shutdownRequested = new AtomicBoolean(false);

    volatile long workLeft = 0;

    volatile int tasks;

    private Task task;


    @Override
    public void run() {
        while (!shutdownRequested.get()) {
            synchronized (taskQueue) {
                Task currentTask = taskQueue.poll(); // Ia task-ul curent din coada (returneaza null in cazul in care coada e goala)
                task = currentTask;
                if (currentTask != null) {
                    simulateTaskExecution(currentTask); // simuleaza executia task-ului
                    currentTask.finish(); // task finalizat
                    tasks--;
                } else {
                    try {
                        taskQueue.wait(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }


    @Override
    public void addTask(Task task) {
        try {
            taskQueue.put(task); // Adauga task-ul in coada (blocheaza daca coada e plina)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Incrementarea nr de task-uri  si actualizarea timpului de lucru
        tasks++;
        workLeft += task.getDuration();
    }

    @Override
    public int getQueueSize() {
        return tasks;
    }

    @Override
    public long getWorkLeft() {
        long workLeft = 0;
        for (Task task : taskQueue) {
            workLeft += task.getDuration(); // Aduna duratele task-urilor din coada
        }
        // Adauga timpul ramas pentru task-ul curent (daca exista)
        if (task != null) {
            workLeft = workLeft + task.getLeft();
        }
        return workLeft;
    }

    @Override
    synchronized public void shutdown() {
        shutdownRequested.set(true);
    }

    private void simulateTaskExecution(Task task) {

        long timeLeft = task.getLeft();

        while (timeLeft > 0) {
            // Verifica daca task-ul curent poate fi preemptat de un alt task cu prioritate mai mare
            if (task.isPreemptible()) {
                if (!taskQueue.isEmpty() && taskQueue.peek().getPriority() > task.getPriority()) {
                    addTask(task);
                    return;
                }
            }
            try {
                Thread.sleep(1000); // Simuleaza executia pentru durata task-ului
                workLeft -= 1000;
                timeLeft -= 1000;
                task.setLeft(timeLeft);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
