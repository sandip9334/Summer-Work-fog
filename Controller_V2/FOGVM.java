package Controller_V2;

public class FOGVM {
    private double freeRam;
    private double usedRam;
    private double totalRam;
    private int noCPUs;
    private int vmid;

    public FOGVM(int vmid){
        this.vmid = vmid;
        freeRam = 512;
        usedRam = 0;
        totalRam = 512;
        noCPUs = 4;
    }
    public double getUsedRam() {
        return usedRam;
    }

    public double getFreeRam() {
        return freeRam;
    }

    public void setFreeRam() {
        freeRam = totalRam - usedRam;
    }

    public void setUsedRam(double usedRam) {
        this.usedRam = usedRam;
        this.setFreeRam();
    }

    public int getVmid() {
        return vmid;
    }

    public double getNosCPU() {
        return noCPUs;
    }

    public double getTotalRam() {
        return totalRam;
    }
}