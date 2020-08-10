package Controller_V2;

import java.util.Scanner;

public class DataCenter{
    static Scanner scanner = new Scanner(System.in);
    private int total_vm;
    private int datacenter_id;
    private int region;
    private VM vm[];
    public DataCenter(int datacenter_id){
        this.datacenter_id = datacenter_id;
        System.out.print("Enter the region where the datacenter is located: ");
        region = scanner.nextInt();
        System.out.print("Enter the total no. of VMs the datacenter contains: ");
        total_vm = scanner.nextInt();
        vm = new VM[total_vm];
        for(int i=0;i<total_vm;i++){
            System.out.println("-------------Setting up the VMs of datacenter_"+datacenter_id+"-------------");
            vm[i] = new VM(i);
        }
    }

    public double getFreeRam(int vmid){
        return vm[vmid].getFreeRam();
    }
    public double nosCPU(int vmid){
        return vm[vmid].getNosCPU();
    }

    public int getTotal_vm() {
        return total_vm;
    }

    public void setUsedRam(int vmid, double usedRam){
        vm[vmid].setUsedRam(usedRam);
    }

    public int getRegion() {
        return region;
    }

    public double getUsedRam(int vmid){
        return vm[vmid].getUsedRam();
    }


}