package Controller_V1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Test Started...");
        DataCenter datacenter[] = new DataCenter[2];

        for(int i=0;i<datacenter.length;i++){
            datacenter[i] = new DataCenter();
            System.out.println("Data Center_"+ i +" created...");
        }

        System.out.println("Enter the total number of requests...");
        int noOfRequests = scanner.nextInt();
        scanner.nextLine();
        String requests[] = new String[noOfRequests];
        for(int i=0;i<noOfRequests;i++){
            System.out.println("Enter the request...");
            requests[i] = scanner.nextLine();
        }

        for(int i=0;i<noOfRequests;i++) {
            int dataCenterId = AllocateDataCenter(requests[i]);
            double vmid = AllocateVM(requests[i], dataCenterId, datacenter[dataCenterId]);
            if(vmid == -1)
                System.out.println("Request has been allocated to the cloud...");
            else
                System.out.println("Request_" + i + " is allocated to DataCenter_" + dataCenterId + " to VM_" + vmid +"\b\b");
        }
    }

    public static int AllocateDataCenter(String req){
        //Logic according to the nearest datacenter concept.
        return 0;
    }

    public static double AllocateVM(String req,int dataCenterId, DataCenter dataCenter){
        String param[] = req.split(",");
        int vmid = -1;
        double MaxFreeRam = 0;
        double NewFreeRam = 0;

        double dparam[] = new double[param.length];
        for(int i=0;i<param.length;i++){
            dparam[i] = Double.parseDouble(param[i]);
        }

        //Treating a request as a whole...
        for(int i=0;i<dataCenter.getTotal_vm();i++){
            if(dataCenter.getFreeRam(i) > MaxFreeRam){
                MaxFreeRam = dataCenter.getFreeRam(i);
                vmid = i;
            }
        }

        if(MaxFreeRam < dparam[3]){
            return -1;
        }
        else{
            NewFreeRam = MaxFreeRam - dparam[3];
            dataCenter.setFreeRam(vmid, NewFreeRam);
            dataCenter.setUsedRam(vmid, (512 - NewFreeRam));
        }
        return vmid;
    }
}