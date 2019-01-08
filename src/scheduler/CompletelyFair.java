package scheduler;

import process.Pcb;
import process.PcbData;

import java.util.ArrayList;

public class CompletelyFair extends Scheduler {
    private ArrayList<Pcb> pcbs;

    CompletelyFair(){
        pcbs = new ArrayList<>();
    }


    @Override
    public Pcb get(int cpuId) {
        Pcb pcb = null;
        if(pcbs.size()>0) {
            pcb = pcbs.remove(0);
            long time = (Pcb.getCurrentTime() - pcb.getPcbData().getTimeFirstAdded()
                    - pcb.getPcbData().getTotalBurstExecutionTime())/Pcb.getProcessCount();
            pcb.setTimeslice(time);
        }
        if(pcb!=null)
            pcb.setAffinity(cpuId);
        return pcb;
    }

    @Override
    public void put(Pcb pcb) {
        PcbData pcbData = pcb.getPcbData();
        if(pcbData == null){
            pcbData =new PcbData();
            pcbData.setTimeFirstAdded(Pcb.getCurrentTime());
            pcb.setPcbData(pcbData);
        }
        else if(pcb.getPreviousState() == Pcb.ProcessState.BLOCKED){
                pcbData.setTotalBurstExecutionTime(0);
                pcbData.setTimeFirstAdded(Pcb.getCurrentTime());
        }
        else{
            pcbData.setTotalBurstExecutionTime(pcbData.getTotalBurstExecutionTime()+pcb.getExecutionTime());
        }
        if (pcbs.size() == 0) {
            pcbs.add(pcb);
        }
        else{
            int i;
            for (i = 0; i < pcbs.size(); i++) {
                if (pcbs.get(i).getPcbData().getTotalBurstExecutionTime() >= pcb.getPcbData().getTotalBurstExecutionTime()) {
                    pcbs.add(i, pcb);
                    break;
                }
            }
            if(pcbs.size() == i){
                pcbs.add(i, pcb);
            }
        }
    }
}
