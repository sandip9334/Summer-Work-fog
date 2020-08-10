package Controller_V2;

import java.util.Scanner;

public class DataCenterVM {
    private double freeRam;
    private double usedRam;
    private double totalRam;
    private double nosCPU;
    private int vmid;
    static Scanner scanner = new Scanner(System.in);

    public DataCenterVM(int vmid){
        System.out.println("-------------Configuring the VM_"+vmid+"-------------");
        this.vmid = vmid;
        System.out.print("Enter the total Ram of the VM: ");
        totalRam = scanner.nextDouble();
        freeRam = totalRam;
        usedRam = 0;
        System.out.print("Enter the no. of CPUs: ");
        nosCPU = scanner.nextDouble();
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
        return nosCPU;
    }

    public double getTotalRam() {
        return totalRam;
    }
}