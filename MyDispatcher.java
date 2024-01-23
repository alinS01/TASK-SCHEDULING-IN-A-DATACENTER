/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {
    int lastAssignedNodeId = -1;

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    @Override
    public void addTask(Task task) {

        if (algorithm.equals(SchedulingAlgorithm.ROUND_ROBIN)) {
            roundRobinAlgorithm(task);
        }

        if (algorithm.equals(SchedulingAlgorithm.SHORTEST_QUEUE)) {
            shortestQueueAlgorithm(task);
        }

        if (algorithm.equals(SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT)) {
            if (task.getType() == TaskType.SHORT) {
                hosts.get(0).addTask(task);
            }
            if (task.getType() == TaskType.MEDIUM) {
                hosts.get(1).addTask(task);
            }
            if (task.getType() == TaskType.LONG) {
                hosts.get(2).addTask(task);
            }
        }

        if (algorithm.equals(SchedulingAlgorithm.LEAST_WORK_LEFT)) {
            leastWorkLeftAlgorithm(task);
        }

    }

    private void roundRobinAlgorithm(Task task) {
        int n = hosts.size();
        lastAssignedNodeId = (lastAssignedNodeId + 1) % n;
        Host targetHost = hosts.get(lastAssignedNodeId);
        targetHost.addTask(task);
    }

    private void shortestQueueAlgorithm(Task task) {
        Host minHost = hosts.get(0);
        int i;
        int n = hosts.size();
        for (i = 0; i < n; i++) {
            Host host2 = hosts.get(i);
            if (host2.getQueueSize() < minHost.getQueueSize()) {
                minHost = host2;
            }
        }
        minHost.addTask(task);
    }

    private void leastWorkLeftAlgorithm(Task task) {
        Host minHost = hosts.get(0);
        int i;
        int n = hosts.size();
        for (i = 0; i < n; i++) {
            Host host2 = hosts.get(i);
            if (host2.getWorkLeft() < minHost.getWorkLeft()) {
                minHost = host2;
            }
        }
        minHost.addTask(task);
    }


}
