package scheduler;

import process.Pcb;
import process.PcbData;

import java.util.ArrayList;

public class ShortestJobFirst extends Scheduler {
    private boolean preemptive;
    private double alpha;

    private double shortest = 0, longest = 0;

    private ArrayList<Pcb> pcbs = new ArrayList<>();

    public ShortestJobFirst(boolean preemptive, double alpha) {
        this.preemptive = preemptive;
        this.alpha = alpha;
    }

    @Override
    public synchronized Pcb get(int cpuId) {
        Pcb pcb = null;
//        if(pcbs.size()>0) {
//            pcb = pcbs.remove(0);
//        }
        if(pcbs.size()>0){
            if(shortest>longest){
                pcb = pcbs.remove(pcbs.size()-1);
                longest = pcbs.size()==0?0:pcbs.get(pcbs.size()-1).getPcbData().gettExpected();
                shortest = 0;
            }
            else{
                pcb = pcbs.remove(0);
                shortest += pcb.getPcbData().gettExpected();
            }
        }
        if(pcb!=null)
            pcb.setAffinity(cpuId);
        return pcb;
    }

    @Override
    public synchronized void put(Pcb pcb) {
        long time = pcb.getExecutionTime();
        PcbData pcbData = pcb.getPcbData();
        if (pcbData == null) {
            pcbData = new PcbData();
            pcb.setPcbData(pcbData);
        }
        long tExpected = pcbData.gettExpected();
        long newTExpected = (long) (alpha * tExpected + (1 - alpha) * time);
        pcbData.settExpected(newTExpected);
        if (pcbs.size() == 0) {
            pcbs.add(pcb);
        }
        else {
            int i;
            for (i = 0; i < pcbs.size(); i++) {
                if (pcbs.get(i).getPcbData().gettExpected() >= newTExpected) {
                    pcbs.add(i, pcb);
                    break;
                }
            }
            if(pcbs.size() == i){
                pcbs.add(i, pcb);
            }
        }
        if(preemptive){
            long max = Long.MIN_VALUE;
            Pcb temp = null;
            for(Pcb p:Pcb.RUNNING){
                if(p.getPcbData()!=null && p.getPcbData().gettExpected()>max){
                    max = p.getPcbData().gettExpected();
                    temp = p;
                }
            }
            if(temp!=null && temp.getPcbData().gettExpected()>newTExpected)
            {
                System.out.println("PREEMPT" + preemptive);
                temp.preempt();
            }
        }
        longest = pcbs.get(pcbs.size() - 1).getPcbData().gettExpected();
    }
}
