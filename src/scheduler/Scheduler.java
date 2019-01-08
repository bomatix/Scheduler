package scheduler;
import process.Pcb;

public abstract class Scheduler {
    public abstract Pcb get(int cpuId);

    public abstract void put(Pcb pcb);

    public static Scheduler createScheduler(String[] args) {
        Scheduler sched = null;
        switch (args[0]){
            case "SJF":{
                sched = new ShortestJobFirst(Integer.parseInt(args[1])==1, Double.parseDouble(args[2]));
                break;
            }
            case "CF":{
                sched = new CompletelyFair();
                break;
            }
            case "MFQ":{
                int quantsLength = Integer.parseInt(args[1]);
                long[] quants = new long[quantsLength];
                for(int i = 0; i < quantsLength;i++){
                    quants[i] = Integer.parseInt(args[i+2]);
                }
                sched = new MultilevelFeedbackQueue(Integer.parseInt(args[1]), quants);
                break;
            }
        }
        return sched;
    }
}