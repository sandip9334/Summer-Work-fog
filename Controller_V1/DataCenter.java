package Controller_V1;

import java.util.Random;

public class DataCenter{

    private int total_vm  = 0;
    private int region;
    private VM vm[];

    public DataCenter(){
        region = 1;
        total_vm = 2;
        vm = new VM[total_vm];
        initializeVM(vm, total_vm);
    }

    public static void initializeVM(VM vm[], int total_vm){
        for(int i=0;i<total_vm;i++){
            vm[i] = new VM();
        }
    }

    public int getRegion() {
        return region;
    }

    public double getFreeRam(int vmid) {
        return vm[vmid].getFreeRam();
    }

    public double getUsedRam(int vmid) {
        return vm[vmid].getUsedRam();
    }

    public int getTotal_vm() {
        return total_vm;
    }

    public void setFreeRam(int vmid, double freeRam) {
        vm[vmid].setFreeRam(freeRam);
    }

    public void setUsedRam(int vmid, double usedRam) {
        vm[vmid].setUsedRam(usedRam);
    }
}
