package scheduler;

import process.Pcb;
import process.PcbData;

import java.util.ArrayList;

public class MultilevelFeedbackQueue extends Scheduler {

    private long[] quants;
    private ArrayList<Pcb>[] arrays;

    MultilevelFeedbackQueue(int num, long[] quants){
        arrays = new ArrayList[num];
        for(int i = 0; i<num; i++)
            arrays[i] = new ArrayList<Pcb>();
        this.quants = quants;
    }

    @Override
    public Pcb get(int cpuId) {
        Pcb pcb = null;
        int i;
        for(i = 0; i < arrays.length; i++){
            if(arrays[i].size()>0){
                pcb = arrays[i].remove(0);
                break;
            }
        }
        if(pcb != null) {
            pcb.setTimeslice(quants[i]);
            pcb.setAffinity(cpuId);
        }
        return pcb;
    }

    @Override
    public void put(Pcb pcb) {
        PcbData pcbData = null;
        if(pcb.getExecutionTime() == 0){
            pcbData = pcb.getPcbData();
            if(pcbData == null)
                pcbData = new PcbData();
            pcbData.setPriority(pcb.getPriority()%arrays.length);
            pcb.setPcbData(pcbData);
        }
        else{
            pcbData = pcb.getPcbData();
            if(pcb.getPreviousState() == Pcb.ProcessState.BLOCKED){
                pcbData.setPriority(pcbData.getPriority()==0?0:pcbData.getPriority()-1);
            }
            else if(pcb.getPreviousState() == Pcb.ProcessState.RUNNING){
                pcbData.setPriority(pcbData.getPriority()==(arrays.length - 1)?(arrays.length - 1):pcbData.getPriority()+1);
            }
        }
        arrays[pcbData.getPriority()].add(pcb);
    }
}
