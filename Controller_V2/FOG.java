package Controller_V2;

public class FOG {
    FOGVM vm[];

    public FOG(){
        vm = new FOGVM[4];
        for(int i=0;i<4;i++){
            vm[i] = new FOGVM(i);
        }
    }

    public double getFreeRam(int vmid){
        return vm[vmid].getFreeRam();
    }
    public double nosCPU(int vmid){
        return vm[vmid].getNosCPU();
    }

    public void setUsedRam(int vmid, double usedRam){
        vm[vmid].setUsedRam(usedRam);
    }
    public double getUsedRam(int vmid){
        return vm[vmid].getUsedRam();
    }
}