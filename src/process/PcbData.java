package process;

public class PcbData {
    private long tExpected = 0;
    private long timeFirstAdded;

    public long getTimeFirstAdded() {
        return timeFirstAdded;
    }

    public void setTimeFirstAdded(long timeFirstAdded) {
        this.timeFirstAdded = timeFirstAdded;
    }

    public long getTotalBurstExecutionTime() {
        return totalBurstExecutionTime;
    }

    public void setTotalBurstExecutionTime(long totalBurstExecutionTime) {
        this.totalBurstExecutionTime = totalBurstExecutionTime;
    }

    private long totalBurstExecutionTime = 0;
    private int priority;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long gettExpected() {
        return tExpected;
    }

    public void settExpected(long tExpected) {
        this.tExpected = tExpected;
    }
    //TODO: Add implementation
}
