package LC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    static double WT = 0;
    static double fogCounter = 0, rFog = 0;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Radhey\\OneDrive\\Desktop\\task50.txt"));
        double systemStartTime = System.currentTimeMillis();
        FOGNODE[] fogNode = new FOGNODE[7];

        fogNode[0] = new FOGNODE(0, 100, 7, 20, 2.4, "Balanced");
        fogNode[1] = new FOGNODE(1, 90, 8, 50, 1, "Overloaded");
        fogNode[2] = new FOGNODE(2, 200, 5, 27, 2.5, "Underloaded");
        fogNode[3] = new FOGNODE(3, 240, 6, 50, 2, "Balanced");
        fogNode[4] = new FOGNODE(4, 70, 7, 60, 0.57, "Overloaded");
        fogNode[5] = new FOGNODE(5, 160, 10, 38, 2.9, "Underloaded");
        fogNode[6] = new FOGNODE(6, 98, 9, 34, 1.8, "Balanced");

        String request = br.readLine();
        double PS, RAM, CPU, BT, AT;
        boolean buffer;
        int totalReq = 0;

        for (int i = 0; request != null; i++) {

            double intermediate = System.currentTimeMillis();
            String[] temp = request.split(",");
            deAllocation(fogNode, systemStartTime);
            BufferAllocation(fogNode, systemStartTime);
            PS = getTaskPS(Integer.parseInt(temp[1]));
            RAM = getTaskRAM(Integer.parseInt(temp[1]));
            CPU = getTaskCPU(Integer.parseInt(temp[1]));
            BT = getTaskBT(Integer.parseInt(temp[1]));
            AT = getTaskAT(Integer.parseInt(temp[1]));

            while ((System.currentTimeMillis() - systemStartTime) / 1000 < AT) {
            }

            //Calculation of current running service on each node
            int[][] tempNS = new int[fogNode.length][2];
            for (int j = 0; j < fogNode.length; j++) {
                for (int k = 0; k < fogNode[j].reqCapacity.length; k++) {
                    if (fogNode[j].reqCapacity[k] > 0) {
                        fogNode[j].ns++;
                    }
                }
                tempNS[j][0] = j;
                tempNS[j][1] = fogNode[j].ns + fogNode[j].BufferReq.size();
                fogNode[j].ns = 0;
            }

            //Arranging tempNS in increasing order
            for (int j = 0; j < tempNS.length; j++) {
                for (int k = 0; k < tempNS.length - 1 - j; k++) {
                    if (tempNS[k][1] > tempNS[k + 1][1]) {
                        int swap = tempNS[k][0];
                        tempNS[k][0] = tempNS[k + 1][0];
                        tempNS[k + 1][0] = swap;

                        swap = tempNS[k][1];
                        tempNS[k][1] = tempNS[k + 1][1];
                        tempNS[k + 1][1] = swap;
                    }
                }
            }

            //Checking according to the minimum service running....
            int j;
            int totalCapableNodes[] = new int[0];
            for (j = 0; j < tempNS.length; j++) {
                int node = tempNS[j][0];
                if (fogNode[node].RAM >= RAM && fogNode[node].Capacity >= PS && fogNode[node].CPU >= (fogNode[node].CPU * CPU) / 100) {
                    if (fogNode[node].freeRam >= RAM && fogNode[node].freeCapacity >= PS && fogNode[node].freeCPU >= (fogNode[node].CPU * CPU) / 100) {
                        fogNode[node].setReq(Double.parseDouble(temp[0]), AT, BT, RAM, PS, CPU);
                        fogCounter += (fogNode[node].usedRam/fogNode[node].RAM)*100;
                        rFog++;
                        System.out.println("Request_" + Double.parseDouble(temp[0]) + " is allocated to fogNode_" + node + " AT: " + AT + " currentTime: " + (System.currentTimeMillis() - systemStartTime) / 1000 + " WT: " + ((System.currentTimeMillis() - systemStartTime) / 1000 - AT));
                        break;
                    } else {
                        //fogNode[node].setBuffer(request);
                        //System.out.println("Request_" + Double.parseDouble(temp[0]) + " is added in to Buffer of fognode_" + node);
                        int[] t = new int[totalCapableNodes.length + 1];
                        for (int k = 0; k < totalCapableNodes.length; k++) {
                            t[k] = totalCapableNodes[k];
                        }
                        t[totalCapableNodes.length] = node;
                        totalCapableNodes = t;
                    }
                }
            }

            //Selecting the buffer....
            if (j == tempNS.length) {

                double[][] tempCT = new double[totalCapableNodes.length][2];
                for(j=0;j<totalCapableNodes.length;j++){
                    double ct = 0;
                    int node = totalCapableNodes[j];
                    for(int k=0;k<fogNode[node].BufferReq.size();k++){
                        String req = fogNode[node].BufferReq.get(k);
                        String[] req1 = req.split(",");
                        int reqId = Integer.parseInt(req1[1]);
                        ct = ct + getTaskBT(reqId);
                    }
                    tempCT[j][0] = node;
                    tempCT[j][1] = ct;
                }

                //Arranging tempCT in ascending order...
                for(j=0;j<tempCT.length;j++){
                    for(int k=0;k<tempCT.length-j-1;k++){
                        if(tempCT[k][1] > tempCT[k+1][1]){
                            double swap = tempCT[k][0];
                            tempCT[k][0] = tempCT[k+1][0];
                            tempCT[k+1][0] = swap;

                            swap = tempCT[k][1];
                            tempCT[k][1] = tempCT[k+1][1];
                            tempCT[k+1][1] = swap;
                        }
                    }
                }


                for(j=0;j<tempCT.length;j++){
                    fogNode[(int)tempCT[j][0]].setBuffer(request);
                    System.out.println("Request_" + Double.parseDouble(temp[0]) + " is added in to Buffer of fognode_" + (int)tempCT[j][0]);
                    break;
                }
            }
            request = br.readLine();
            totalReq++;
        }
        System.out.println(totalReq);
        System.out.println("Buffer Allocation Started");
        while (true) {
            buffer = BufferAllocation(fogNode, systemStartTime);
            if (buffer)
                break;
            deAllocation(fogNode, systemStartTime);
        }
        //Divide the WT by number of request instead of 10...
        System.out.println("Average waiting time: " + (WT / totalReq));
        System.out.println("ARU: " + fogCounter/rFog);
        System.out.println("Total Completion time: " + (System.currentTimeMillis() - systemStartTime) / 1000);
    }

    public static boolean BufferAllocation(FOGNODE[] fognode, double systemStartTime) throws Exception {
        double PS, RAM, CPU, BT, AT;
        boolean buffer = true;
        for (int i = 0; i < fognode.length; i++) {
            for (int j = 0; j < fognode[i].BufferReq.size(); j++) {
                String[] temp = fognode[i].BufferReq.get(j).split(",");
                PS = getTaskPS(Integer.parseInt(temp[1]));
                RAM = getTaskRAM(Integer.parseInt(temp[1]));
                CPU = getTaskCPU(Integer.parseInt(temp[1]));
                BT = getTaskBT(Integer.parseInt(temp[1]));
                AT = getTaskAT(Integer.parseInt(temp[1]));
                if (fognode[i].freeRam >= RAM && fognode[i].freeCapacity >= PS && fognode[i].freeCPU >= (fognode[i].CPU * CPU) / 100) {
                    //Change made in calculation of WT...
                    WT = WT + (System.currentTimeMillis() - systemStartTime) / 1000 - AT;
                    fognode[i].setReq(Double.parseDouble(temp[0]), ((System.currentTimeMillis() - systemStartTime) / 1000), BT, RAM, PS, CPU);
                    System.out.println("Request_" + Double.parseDouble(temp[0]) + " is allocated to fogNode_" + i + " AT: " + AT + " currentTime: " + (System.currentTimeMillis() - systemStartTime) / 1000 + " WT: " + ((System.currentTimeMillis() - systemStartTime) / 1000 - AT));
                    fogCounter += (fognode[i].usedRam/fognode[i].RAM)*100;
                    rFog++;
                    fognode[i].BufferReq.remove(j);
                }
            }
            if (fognode[i].BufferReq.size() != 0) {
                buffer = false;
            }
        }

        return buffer;
    }

    public static void deAllocation(FOGNODE[] fognode, double systemStartTime) {
        for (int i = 0; i < fognode.length; i++) {
            for (int j = 0; j < fognode[i].req.length; j++) {
                if (fognode[i].endTime[j] <= (System.currentTimeMillis() - systemStartTime) / 1000) {
                    //System.out.println("Request_" + fognode[i].req[j] + " is deallocated to fogNode_" + i);
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
                RAM = 3; // 4
                break;
            case 2:
                RAM = 3; //5
                break;
            case 3:
                RAM = 4;  //7
                break;
            case 4:
                RAM = 5;  //8
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
