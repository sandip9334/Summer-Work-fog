package AWRR;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    static double WT = 0, TAT = 0, Makespan = 0;
    static double fogCounter = 0, rFog = 0, alpha = 0.07;
    static FOGNODE[] fogNode = new FOGNODE[7];

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Radhey\\OneDrive\\Desktop\\task50.txt"));
        double systemStartTime = System.currentTimeMillis();
        double systemEndTime;

        fogNode[0] = new FOGNODE(0, 100, 7, 20, 2.4, "Balanced");
        fogNode[1] = new FOGNODE(1, 90, 8, 50, 1, "Overloaded");
        fogNode[2] = new FOGNODE(2, 200, 5, 27, 2.5, "Underloaded");
        fogNode[3] = new FOGNODE(3, 240, 6, 50, 2, "Balanced");
        fogNode[4] = new FOGNODE(4, 70, 7, 60, 0.57, "Overloaded");
        fogNode[5] = new FOGNODE(5, 160, 10, 38, 2.9, "Underloaded");
        fogNode[6] = new FOGNODE(6, 98, 9, 34, 1.8, "Balanced");

        String request = br.readLine();

        boolean buffer;

        while (request!=null){
            double PS, RAM, CPU, BT, AT;
            String[] temp = request.split(",");
            deAllocation(fogNode, systemStartTime);
            BufferAllocation(fogNode, systemStartTime);
            PS = getTaskPS(Integer.parseInt(temp[1]));
            RAM = getTaskRAM(Integer.parseInt(temp[1]));
            CPU = getTaskCPU(Integer.parseInt(temp[1]));
            BT = getTaskBT(Integer.parseInt(temp[1]));
            AT = getTaskAT(Integer.parseInt(temp[1]));
            //System.out.println((System.currentTimeMillis()-intermediate-systemStartTime)/1000);

            while ((System.currentTimeMillis() - systemStartTime) / 1000 < AT) {
                ;
            }




            request = br.readLine();
        }

        System.out.println("Buffer Allocation Started...");
        while (true) {
            buffer = BufferAllocation(fogNode, systemStartTime);
            if (buffer) {
                break;
            }
            deAllocation(fogNode, systemStartTime);
        }

        System.out.println("Waiting Time : " + WT / 50);
        System.out.println("Makespan : " + Makespan);
        System.out.println("TAT : " + TAT / 50);
        System.out.println("RU: " + (fogCounter/rFog)*100);

    }

    public static void updateAW(){
        System.out.println("Updating....");
        for(int i=0;i<fogNode.length;i++){
            fogNode[i].AW = alpha*(fogNode[i].freeCapacity * fogNode[i].freeRam)/fogNode[i].freeCPU;
            //System.out.println(fogNode[i].freeCapacity + " " + fogNode[i].freeRam + " " + fogNode[i].freeCPU);
            fogNode[i].continuousCounter = 0;
        }
        System.out.println();
    }

    public static boolean BufferAllocation(FOGNODE[] fognode, double systemStartTime) throws Exception {
        double PS, RAM, CPU, BT, AT;
        boolean buffer = true;
        for (int i = 0; i < fognode.length; i++) {
            // System.out.println(fognode[i].Bufferreq.size());
            for (int j = 0; j < fognode[i].BufferReq.size(); j++) {
                String[] temp = fognode[i].BufferReq.get(j).split(",");
                PS = getTaskPS(Integer.parseInt(temp[1]));
                RAM = getTaskRAM(Integer.parseInt(temp[1]));
                CPU = getTaskCPU(Integer.parseInt(temp[1]));
                BT = getTaskBT(Integer.parseInt(temp[1]));
                AT = getTaskAT(Integer.parseInt(temp[1]));
                if (fognode[i].freeRam >= RAM && fognode[i].freeCapacity >= PS && fognode[i].freeCPU >= (fognode[i].CPU * CPU) / 100) {
                    WT = WT + (System.currentTimeMillis() - systemStartTime)/1000 - AT;
                    fognode[i].setReq(Double.parseDouble(temp[0]), ((System.currentTimeMillis() - systemStartTime) / 1000), BT, RAM, PS, CPU);
                    System.out.println("Request_" + Double.parseDouble(temp[0]) + " is allocated to fogNode_" + i + " AT: " + AT + " currentTime: " + (System.currentTimeMillis() - systemStartTime) / 1000 + " WT: " + ((System.currentTimeMillis() - systemStartTime) / 1000 - AT));
                    calculation();
                    fognode[i].BufferReq.remove(j);
                }
            }
            System.out.print(i + " " + fognode[i].BufferReq.size() + "->");
            if(fognode[i].BufferReq.size() != 0){
                buffer = false;
            }
        }
        System.out.println();
        return buffer;
    }

    public static void calculation(){
        rFog++;
        double ramru = 0, cpuru = 0, capcacityru = 0;
        for(int i=0;i<fogNode.length;i++){
            ramru = ramru + (fogNode[i].usedRam/fogNode[i].RAM);
            cpuru = cpuru + (fogNode[i].usedCPU/fogNode[i].CPU);
            capcacityru = capcacityru + (fogNode[i].usedCapacity/fogNode[i].Capacity);
        }
        fogCounter = fogCounter + (ramru/7 + cpuru/7 + capcacityru/7)/3;
    }

    public static void deAllocation(FOGNODE[] fognode, double systemStartTime) {
        for (int i = 0; i < fognode.length; i++) {
            for (int j = 0; j < fognode[i].req.length; j++) {
                if (fognode[i].endTime[j] <= (System.currentTimeMillis() - systemStartTime) / 1000) {
                    //   System.out.println("Request_" + fognode[i].req[j] + " is deallocated to fogNode_" + i);
                    fognode[i].freeRam = fognode[i].freeRam + fognode[i].reqRam[j];
                    fognode[i].usedRam = fognode[i].RAM - fognode[i].freeRam;
                    fognode[i].reqRam[j] = 0;
                    fognode[i].freeCapacity = fognode[i].freeCapacity + fognode[i].reqCapacity[j];
                    fognode[i].usedCapacity = fognode[i].Capacity - fognode[i].freeCapacity;
                    fognode[i].reqCapacity[j] = 0;
                    fognode[i].freeCPU = fognode[i].freeCPU + fognode[i].reqCPU[j];
                    fognode[i].usedCPU = fognode[i].CPU - fognode[i].freeCPU;
                    fognode[i].reqCPU[j] = 0;
                }
            }
        }
    }

    public static double getTaskPS(int request) {
        double PS;
        switch (request) {
            case 1:
                PS = 10;
                break;
            case 2:
                PS = 20;
                break;
            case 3:
                PS = 15;
                break;
            case 4:
                PS = 29;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + request);
        }
        return PS;
    }

    public static double getTaskRAM(int request) {
        double RAM;
        switch (request) {
            case 1:
                RAM = 4; // 4
                break;
            case 2:
                RAM = 5; //5
                break;
            case 3:
                RAM = 7;  //7
                break;
            case 4:
                RAM = 8;  //8
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + request);
        }
        return RAM;
    }

    public static double getTaskCPU(int request) {
        double CPU;
        switch (request) {
            case 1:
                CPU = 17;
                break;
            case 2:
                CPU = 23;
                break;
            case 3:
                CPU = 18;
                break;
            case 4:
                CPU = 30;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + request);
        }
        return CPU;
    }

    public static double getTaskBT(int request) {
        double BT;
        switch (request) {
            case 1:
                BT = 9;
                break;
            case 2:
                BT = 5;
                break;
            case 3:
                BT = 3;
                break;
            case 4:
                BT = 4;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + request);
        }
        return BT;
    }

    public static double getTaskAT(int request) {
        double AT;
        switch (request) {
            case 1:
                AT = 0;
                break;
            case 2:
                AT = 1;
                break;
            case 3:
                AT = 2;
                break;
            case 4:
                AT = 3;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + request);
        }
        return AT;
    }

}
