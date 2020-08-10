package Controller_V2;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int total_datacenter;
        System.out.println("----------Setting the Datacenters----------");
        System.out.println("Enter the total Data centers present: ");
        total_datacenter = scanner.nextInt();
        FOG fog = new FOG();
        DataCenter dataCenter[] = new DataCenter[total_datacenter];


        for (int i = 0; i < total_datacenter; i++) {
            System.out.println("Configuring Datacenter_" + i);
            dataCenter[i] = new DataCenter(i);
            System.out.println("Datacenter_" + i + " created...");
        }

        System.out.println("\n-------------Requests-------------\n");
        System.out.print("Enter the total no. of requests: ");
        int total_requests = scanner.nextInt();
        String requests[] = new String[total_requests];

        System.out.println("Enter the requests in [index, location, start-time, priority(1-3), resource-needed]...");
        scanner.nextLine();
        for (int i = 0; i < total_requests; i++) {
            System.out.print("Enter the request_" + i + ": ");
            requests[i] = scanner.nextLine();
        }
        for (int i = 0; i < total_requests; i++) {
            if(!RequestAllocationFOG(requests[i],fog, i))
                RequestAllocationDVM(requests[i], dataCenter, total_datacenter, i);
        }

    }

    public static int nextNearestDatacenter(DataCenter dataCenter[]) {
        //Logic of next Nearest Datacenter...
        //And return -1 if there are no more datacenter

        return (new Random().nextInt(dataCenter.length));
    }

    public static double getRequiredRam(String request) {
        double RAM;
        String temp[] = request.split(",");

        return (Double.parseDouble(temp[4]));

    }

    public static double getPriority(String request) {
        double priority;
        String temp[] = request.split(",");
        return (Double.parseDouble(temp[3]));
    }

    public static boolean RequestAllocationFOG(String request, FOG fog, int index){
        double priority = getPriority(request);
        if(priority!=3) {
            double RequiredRam = getRequiredRam(request);
            double maxFreeRam = 0;
            int vmid = 0;
            for (int i = 0; i < 4; i++) {
                if (maxFreeRam < fog.getFreeRam(i)) {
                    vmid = i;
                    maxFreeRam = fog.getFreeRam(i);
                }
            }
            if (maxFreeRam < RequiredRam) {
                System.out.println("FOG doesn't have enough resources...");
                return false;
            } else {
                System.out.println("Request_" + index + " assigned to FOGVM_" + vmid + "... ");
                fog.setUsedRam(vmid, (fog.getUsedRam(vmid) + RequiredRam));
                return true;
            }
        }
        else{
            return false;
        }

    }

    public static void RequestAllocationDVM(String request, DataCenter dataCenter[], int total_datacenter, int requestId) {

        boolean flag = false;
        double RequiredFreeRam = getRequiredRam(request);
        while (!flag) {

            if (getPriority(request) == 3) {
                System.out.println("Request_"+ requestId +" is handed over to cloud...");
                break;
            } else {
                //Maybe another condition for priority 2 and 1

                int tempDatacenterid = nextNearestDatacenter(dataCenter);
                int vmid = 0;
                double maxFreeRam = dataCenter[tempDatacenterid].getFreeRam(0);

                if (tempDatacenterid == -1) {
                    System.out.println("Request_"+ requestId +" is handed over to Cloud...");
                    break;
                } else {
                    //Finding the maximum free Ram from the nearest datacenter
                    for (int i = 1; i < dataCenter[tempDatacenterid].getTotal_vm(); i++) {
                        if (maxFreeRam < dataCenter[tempDatacenterid].getFreeRam(i)) {
                            maxFreeRam = dataCenter[tempDatacenterid].getFreeRam(i);
                            vmid = i;
                        }
                    }
                    if (maxFreeRam >= RequiredFreeRam) {

                        dataCenter[tempDatacenterid].setUsedRam(vmid, (dataCenter[tempDatacenterid].getUsedRam(vmid) + RequiredFreeRam));
                        System.out.println("Request_"+ requestId +" is handed over to VM_" + vmid + " of datacenter_" + tempDatacenterid);
                        break;
                    }
                    else{
                        System.out.println("Request_" + requestId + " is handed over to Cloud...");
                        break;
                    }
                }
            }

        }
    }
}
