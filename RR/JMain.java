package RR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

class JMain {
    static double WT = 0, TAT = 0, Makespan = 0;
    static double fogCounter = 0, rFog = 0;
    static JFOGNODE[] fogNode = new JFOGNODE[7];

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Radhey\\OneDrive\\Desktop\\task50.txt"));
        double systemStartTime = System.currentTimeMillis();
        double systemEndTime;

        fogNode[0] = new JFOGNODE(1, 100, 7, 20, 2.4, "Balanced");
        fogNode[1] = new JFOGNODE(2, 90, 8, 50, 1, "Overloaded");
        fogNode[2] = new JFOGNODE(3, 200, 5, 27, 2.5, "Underloaded");
        fogNode[3] = new JFOGNODE(4, 240, 6, 50, 2, "Balanced");
        fogNode[4] = new JFOGNODE(5, 70, 7, 60, 0.57, "Overloaded");
        fogNode[5] = new JFOGNODE(6, 160, 10, 38, 2.9, "Underloaded");
        fogNode[6] = new JFOGNODE(7, 98, 9, 34, 1.8, "Balanced");

        String request = br.readLine();
        int count = 0, temp1 = -1;

        double buffer = 0, rbuffer = 0;

        for (int i = 0; request != null; i++) {

            double PS, RAM, CPU, BT, AT;
            double intermediate = System.currentTimeMillis();
            String[] temp = request.split(",");
            deAllocation(fogNode, systemStartTime);
            buffer = BufferAllocation(fogNode, systemStartTime);
            PS = getTaskPS(Integer.parseInt(temp[1]));
            RAM = getTaskRAM(Integer.parseInt(temp[1]));
            CPU = getTaskCPU(Integer.parseInt(temp[1]));
            BT = getTaskBT(Integer.parseInt(temp[1]));
            AT = getTaskAT(Integer.parseInt(temp[1]));
            //System.out.println((System.currentTimeMillis()-intermediate-systemStartTime)/1000);

            while ((System.currentTimeMillis() - systemStartTime) / 1000 < AT) {
            }
            int node = i % 7;
            count = 0;
            while (true) {
                if (temp1 == 6) {
                    temp1 = -1;
                }
                node = temp1 + 1;
                node = node + count;
                if (fogNode[node].RAM >= RAM && fogNode[node].Capacity >= PS && fogNode[node].CPU >= (fogNode[node].CPU * CPU) / 100) {
                    if (fogNode[node].freeRam >= RAM && fogNode[node].freeCapacity >= PS && fogNode[node].freeCPU >= (fogNode[node].CPU * CPU) / 100) {
                        fogNode[node].setReq(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]), AT, BT, RAM, PS, CPU);
                        System.out.println("Request_" + Double.parseDouble(temp[0]) + " is allocated to fogNode_" + node + " AT:" + AT + " currentTime: " + (System.currentTimeMillis() - systemStartTime) / 1000);
                        calculation();
                    } else {
                        fogNode[node].setBuffer(request, systemStartTime);
                        fogNode[node].Bufferreq.add(request);
                        rbuffer++;
                        System.out.println("Request_" + Double.parseDouble(temp[0]) + " is added in to Buffer of fognode_" + node);
                    }
                    temp1 = node;
                    break;
                } else {
                    count++;
                }
            }
            request = br.readLine();

        }

        while (true) {
            buffer = BufferAllocation(fogNode, systemStartTime);
            if (buffer != 1) {
                systemEndTime = System.currentTimeMillis();
                break;
            }
            deAllocation(fogNode, systemStartTime);
        }

        for (int i = 0; i < fogNode.length; i++) {
            for (int j = 0; j < fogNode[i].req.length; j++) {
                double CT, AT;
                CT = fogNode[i].endTime[j];
                AT = getTaskAT((int) fogNode[i].reqtype[j]);
                Makespan = Makespan + CT;
                TAT = TAT + (CT - AT);
                // System.out.println(CT-AT);
            }
        }

        System.out.println("Waiting Time : " + WT / 50);
        System.out.println("Makespan : " + Makespan);
        System.out.println("TAT : " + TAT / 50);
        System.out.println(rFog);
        System.out.println("RU : " + fogCounter/rFog);

    }

    public static double BufferAllocation(JFOGNODE[] fognode, double systemStartTime) throws Exception {
        double PS, RAM, CPU, BT, AT;
        double buffer = 0;
        for (int i = 0; i < fognode.length; i++) {
            // System.out.println(fognode[i].Bufferreq.size());
            for (int j = 0; j < fognode[i].Bufferreq.size(); j++) {
                buffer = 1;
                String[] temp = fognode[i].Bufferreq.get(j).split(",");
                PS = getTaskPS(Integer.parseInt(temp[1]));
                RAM = getTaskRAM(Integer.parseInt(temp[1]));
                CPU = getTaskCPU(Integer.parseInt(temp[1]));
                BT = getTaskBT(Integer.parseInt(temp[1]));
                AT = getTaskAT(Integer.parseInt(temp[1]));
                if (fognode[i].freeRam >= RAM && fognode[i].freeCapacity >= PS && fognode[i].freeCPU >= (fognode[i].CPU * CPU) / 100) {
                    WT = WT + (System.currentTimeMillis() - fognode[i].BufferStarttime[j]) / 1000;
                    fognode[i].setReq(fognode[i].BufferReq[j], Double.parseDouble(temp[1]), AT + ((System.currentTimeMillis() - fognode[i].BufferStarttime[j]) / 1000), BT, RAM, PS, CPU);
                    System.out.println("Request_" + Double.parseDouble(temp[0]) + " is allocated to fogNode_" + i + " AT:" + AT + " currentTime:" + (System.currentTimeMillis() - systemStartTime) / 1000 + " WT: " + (System.currentTimeMillis() - fognode[i].BufferStarttime[j]) / 1000);
                    calculation();
                    fognode[i].Bufferreq.remove(j);
                }
            }
        }
//        for (int i=0;i<fognode.length;i++){
//            for (int j=0;j<7;j++){
//                System.out.println(Bufferreq.get(j));
//            }
//        }
//        for(int i=0;i<fognode.length;i++){
//            for(int j=0;j<fognode[i].BufferProcess.length;j++) {
//                //System.out.println(fognode[i].BufferReq[j]);
//                PS = getTaskPS((int) fognode[i].BufferProcess[j]);
//                RAM = getTaskRAM((int) fognode[i].BufferProcess[j]);
//                CPU = getTaskCPU((int) fognode[i].BufferProcess[j]);
//                BT = getTaskBT((int) fognode[i].BufferProcess[j]);
//                AT = getTaskAT((int) fognode[i].BufferProcess[j]);
//
//                if(fognode[i].freeRam >= RAM && fognode[i].freeCapacity >= PS && fognode[i].freeCPU >= (fognode[i].CPU * CPU) / 100){
//                    WT = WT + System.currentTimeMillis() - fognode[i].BufferStarttime[j];
//                    fognode[i].setReq(fognode[i].BufferReq[j],((System.currentTimeMillis()-systemStartTime)/1000), BT, RAM, PS, CPU);
//                    System.out.println("Request_" + fognode[i].BufferReq[j] + " is allocated to fogNode_" + i);
//                    fognode[i].BufferReq[j]=0;
//                }
//                buffer = buffer + fognode[i].BufferReq[j];
//            }
//        }
        return buffer;
    }

    public static void calculation(){
        rFog++;
        double ru = 0;
        for(int i=0;i<fogNode.length;i++){
            ru += fogNode[i].usedRam/fogNode[i].RAM;
        }
        ru = ru/7;
        fogCounter = fogCounter + ru;
    }

    public static void deAllocation(JFOGNODE[] fognode, double systemStartTime) {
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
