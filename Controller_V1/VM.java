package Controller_V1;

public class VM {
    private int ram;    //Mbs
    private double freeRam;
    private double usedRam;
    private int vmid;
    private static int vmcounter = -1;

    public VM(){
        ram = 512;
        freeRam = 512;
        usedRam = 0;
        vmid = ++vmcounter;
    }

    public void setFreeRam(double freeRam) {
        this.freeRam = freeRam;
    }

    public void setUsedRam(double usedRam) {
        this.usedRam = usedRam;
    }

    public double getFreeRam() {
        return freeRam;
    }

    public double getUsedRam() {
        return usedRam;
    }

    public double getVmid() {
        return vmid;
    }
}
