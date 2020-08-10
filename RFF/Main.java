package RFF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {

    static EDGE[] edge = new EDGE[11];
    static FOGNODE[] fogNode = new FOGNODE[8];
    static CLOUD[] cloud = new CLOUD[5];
    static File file = new File("C:\\Users\\Radhey\\OneDrive\\Desktop\\updated500.txt");
    static int criticalCounter = 0, nonCriticalCounter = 0;
    public static double cloudCounter = 0, rcloud = 0, creq=0, mcloudcounter=0, mcloud=0;
    public static double edgeCounter = 0, redge = 0, ereq=0, medgecounter=0, medge=0 ;
    public static double fogCounter = 0, rfog = 0, freq=0, mfogcounter=0, mfog=0;

    public static void main(String[] args) throws Exception {

        String[] Critical = getCritical(file);
        String[] nonCritical = getNonCritical(file);
        init();

        boolean flag = true;

        double systemStartTime = System.currentTimeMillis();

        while(flag){

            //De-Allocation of Resources....
            deAllocation(edge, systemStartTime);
            deAllocation(fogNode, systemStartTime);
            deAllocation(cloud, systemStartTime);

            //Bifurcation of request on the basis of Priority....
            double CriticalStartTime = 0, nextCriticalStartTime = 0;
            double nonCriticalStartTime = 0, nextNonCriticalStartTime = 0;


            do{
                updation();
                CPUcalculation();
                Memcalculation();
                if(criticalCounter < Critical.length) {
                    CriticalStartTime = getStartTime(Critical[criticalCounter]);
                    if (CriticalStartTime <= (System.currentTimeMillis() - systemStartTime) / 1000) {

                        //Request Allocation of Critical Request-------------------------------------------
                        String request = Critical[criticalCounter];
                        if(!edgeAllocation(request)){
                            if(!fogAllocation(request)){
                                if(!cloudAllocation(request)){
                                    //Migration....
                                }
                            }
                        }
                        //--------------------------------------------------------------

                        criticalCounter++;
                        if (criticalCounter != Critical.length)
                            nextCriticalStartTime = getStartTime(Critical[criticalCounter]);
                    }
                }
                else{
                    break;
                }
            }while (CriticalStartTime == nextCriticalStartTime);

            do{
                updation();
                CPUcalculation();
                Memcalculation();
                if(nonCriticalCounter < nonCritical.length) {
                    nonCriticalStartTime = getStartTime(nonCritical[nonCriticalCounter]);
                    if (nonCriticalStartTime <= (System.currentTimeMillis() - systemStartTime) / 1000) {

                        //Request Allocation of Non-Critical Request-------------------------------------------
                        String request = nonCritical[nonCriticalCounter];
                        if(!edgeAllocation(request)){
                            if(!fogAllocation(request)){
                                if(!cloudAllocation(request)){
                                    //Migration....
                                }
                            }
                        }
                        //--------------------------------------------------------------

                        nonCriticalCounter++;
                        if (nonCriticalCounter != nonCritical.length)
                            nextNonCriticalStartTime = getStartTime(nonCritical[nonCriticalCounter]);
                    }
                }
                else {
                    break;
                }
            }while (nonCriticalStartTime == nextNonCriticalStartTime);

            if(criticalCounter == Critical.length && nonCriticalCounter == nonCritical.length)
                flag = false;
        }

        System.out.println("Total number of request allocated on edge: " + ereq);
        System.out.println("Total number of request allocated on fog: " + freq);
        System.out.println("Total number of request allocated on cloud: " + creq);

        System.out.println("Edge CPU Utilization: " + 100*edgeCounter/redge);
        System.out.println("Fog CPU Utilization: " + 100*fogCounter/rfog);
        System.out.println("Cloud CPU Utilization: " + 100*cloudCounter/rcloud);

        System.out.println("Edge Memory Utilization: " + 100*medgecounter/medge);
        System.out.println("Fog Memory Utilization: " + 100*mfogcounter/mfog);
        System.out.println("Cloud Memory Utilization: " + 100*mcloudcounter/mcloud);

    }

    public static void fogMigration(){
        ;
    }

    public static void init(){

        //Initialisation of Edge nodes.....
        edge[0] = new EDGE(0, 100, 70, 20, 2.4, "Underloaded", 2, 2);
        edge[1] = new EDGE(1, 190, 115, 50, 1, "Underloaded", 4, 4);
        edge[2] = new EDGE(2, 200, 125, 27, 2.5, "Underloaded", 5, 5);
        edge[3] = new EDGE(3, 240, 140, 50, 2, "Underloaded", 12, 12);
  //      edge[4] = new EDGE(4, 70, 70, 60, 0.57, "Underloaded", 14, 14);
        edge[4] = new EDGE(5, 160, 100, 38, 2.9, "Underloaded", 15, 15);
//        edge[5] = new EDGE(6, 98, 90, 34, 1.8, "Underloaded", 10, 10);
        edge[5] = new EDGE(7, 290, 135, 67, 2, "Underloaded", 11, 18);
        edge[6] = new EDGE(8, 270, 130, 70, 0.57, "Underloaded", 1, 11);
        edge[7] = new EDGE(9, 160, 120, 68, 2.9, "Underloaded", 7, 7);
        edge[8] = new EDGE(10, 150, 88, 40, 2.9, "Underloaded", 7, 7);
  //      edge[10] = new EDGE(11, 80, 45, 32, 2.9, "Underloaded", 7, 7);
        edge[9] = new EDGE(11, 280, 148, 55, 2.9, "Underloaded", 7, 7);
        edge[10] = new EDGE(12, 195, 125, 32, 2.9, "Underloaded", 7, 7);

        //Initialisaton of Fog nodes....
        fogNode[0] = new FOGNODE(0, 300, 175, 72, 2.4, "Underloaded", 2, 2);
        fogNode[1] = new FOGNODE(1, 490, 155, 83, 1, "Underloaded", 4, 4);
        fogNode[2] = new FOGNODE(2, 320, 185, 97, 2.5, "Underloaded", 5, 5);
        fogNode[3] = new FOGNODE(3, 384, 200, 70, 2, "Underloaded", 12, 12);
        fogNode[4] = new FOGNODE(4, 425, 215, 100, 0.57, "Underloaded", 14, 14);
        fogNode[5] = new FOGNODE(5, 450, 170, 115, 2.9, "Underloaded", 15, 15);
        fogNode[6] = new FOGNODE(6, 500, 158, 94, 1.8, "Underloaded", 10, 10);
 //       fogNode[7] = new FOGNODE(7, 350, 168, 90, 1.8, "Underloaded", 10, 10);
        fogNode[7] = new FOGNODE(7, 550, 225, 120, 1.8, "Underloaded", 10, 10);

        //Initialisation of Cloud nodes.....
        cloud[0] = new CLOUD(0, 700, 250, 120, 2.4, "Underloaded", 2, 2);
        cloud[1] = new CLOUD(1, 850, 380, 135, 1, "Underloaded", 4, 4);
        cloud[2] = new CLOUD(2, 775, 415, 150, 2.5, "Underloaded", 5, 5);
        cloud[3] = new CLOUD(3, 915, 315, 210, 2, "Underloaded", 12, 12);
        cloud[4] = new CLOUD(4, 1000, 512, 200, 0.57, "Underloaded", 14, 14);
//        cloud[5] = new CLOUD(5, 160, 100, 38, 2.9, "Underloaded", 15, 15);
//        cloud[6] = new CLOUD(6, 98, 90, 34, 1.8, "Underloaded", 10, 10);

    }

    public static void updation(){

        //Updating the values of AW and the status of each node....

        for(int i=0;i<edge.length;i++){
            if(edge[i].freeCPU != 0){   //Condition to ignore division by 0....
                edge[i].AW = (edge[i].freeCapacity * edge[i].freeRam)/edge[i].freeCPU;

                //Range of setting status based on AW...

            }
            else{
                edge[i].AW = 0;
                edge[i].Status = "Overloaded";
            }
        }

        for(int i=0;i<fogNode.length;i++){
            if(fogNode[i].freeCPU != 0){   //Condition to ignore division by 0....
                fogNode[i].AW = (fogNode[i].freeCapacity * fogNode[i].freeRam)/fogNode[i].freeCPU;

                //Range of setting status based on AW...

            }
            else{
                fogNode[i].AW = 0;
                fogNode[i].Status = "Overloaded";
            }
        }

        for(int i=0;i<cloud.length;i++){
            if(cloud[i].freeCPU != 0){   //Condition to ignore division by 0....
                cloud[i].AW = (cloud[i].freeCapacity * cloud[i].freeRam)/cloud[i].freeCPU;

                //Range of setting status based on AW...

            }
            else{
                cloud[i].AW = 0;
                cloud[i].Status = "Overloaded";
            }
        }
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

    public static void deAllocation(CLOUD[] cloud, double systemStartTime) {
        for (int i = 0; i < cloud.length; i++) {
            for (int j = 0; j < cloud[i].req.length; j++) {
                if (cloud[i].endTime[j] <= (System.currentTimeMillis() - systemStartTime) / 1000) {
                    //   System.out.println("Request_" + cloud[i].req[j] + " is deallocated to cloud_" + i);
                    cloud[i].freeRam = cloud[i].freeRam + cloud[i].reqRam[j];
                    cloud[i].usedRam = cloud[i].RAM - cloud[i].freeRam;
                    cloud[i].reqRam[j] = 0;
                    cloud[i].freeCapacity = cloud[i].freeCapacity + cloud[i].reqCapacity[j];
                    cloud[i].usedCapacity = cloud[i].Capacity - cloud[i].freeCapacity;
                    cloud[i].reqCapacity[j] = 0;
                    cloud[i].freeCPU = cloud[i].freeCPU + cloud[i].reqCPU[j];
                    cloud[i].usedCPU = cloud[i].CPU - cloud[i].freeCPU;
                    cloud[i].reqCPU[j] = 0;
                }
            }
        }
    }

    public static void deAllocation(EDGE[] edge, double systemStartTime) {
        for (int i = 0; i < edge.length; i++) {
            for (int j = 0; j < edge[i].req.length; j++) {
                if (edge[i].endTime[j] <= (System.currentTimeMillis() - systemStartTime) / 1000) {
                    //   System.out.println("Request_" + edge[i].req[j] + " is deallocated to edge_" + i);
                    edge[i].freeRam = edge[i].freeRam + edge[i].reqRam[j];
                    edge[i].usedRam = edge[i].RAM - edge[i].freeRam;
                    edge[i].reqRam[j] = 0;
                    edge[i].freeCapacity = edge[i].freeCapacity + edge[i].reqCapacity[j];
                    edge[i].usedCapacity = edge[i].Capacity - edge[i].freeCapacity;
                    edge[i].reqCapacity[j] = 0;
                    edge[i].freeCPU = edge[i].freeCPU + edge[i].reqCPU[j];
                    edge[i].usedCPU = edge[i].CPU - edge[i].freeCPU;
                    edge[i].reqCPU[j] = 0;
                }
            }
        }
    }

    public static double getStartTime(String request){
        String[] temp = request.split(",");
        return Double.parseDouble(temp[3]);
    }

    public static double getDuration(String request){
        String[] temp = request.split(",");
        return Double.parseDouble(temp[4]);
    }

    public static double getRAM(String request){
        String temp[] = request.split(",");
        int pType = Integer.parseInt(temp[8]);
        double RAM;
        switch (pType){
            case 1:
                RAM = 30;
                break;
            case 2:
                RAM = 27;
                break;
            case 3:
                RAM = 18;
                break;
            case 4:
                RAM = 15;
                break;
            case 5:
                RAM = 10;
                break;
            case 6:
                RAM = 9;
                break;
            default:
                RAM = -1;
        }
        return RAM;
    }

    public static double getCPU(String request){
        String temp[] = request.split(",");
        int pType = Integer.parseInt(temp[8]);
        double CPU;
        switch (pType){
            case 1:
                CPU = 20;
                break;
            case 2:
                CPU = 18;
                break;
            case 3:
                CPU = 15;
                break;
            case 4:
                CPU = 13;
                break;
            case 5:
                CPU = 11;
                break;
            case 6:
                CPU = 8;
                break;
            default:
                CPU = -1;
        }
        return CPU;
    }

    public static double getCapacity(String request){
        String temp[] = request.split(",");
        int pType = Integer.parseInt(temp[8]);
        double capacity;
        switch (pType){
            case 1:
                capacity = 50;
                break;
            case 2:
                capacity = 43;
                break;
            case 3:
                capacity = 32;
                break;
            case 4:
                capacity = 26;
                break;
            case 5:
                capacity = 14;
                break;
            case 6:
                capacity = 12;
                break;
            default:
                capacity = -1;
        }
        return capacity;
    }

    public static double getLongitude(String request){
        String[] temp = request.split(",");
        return Double.parseDouble(temp[6]);
    }

    public static double getLatitude(String request){
        String[] temp = request.split(",");
        return Double.parseDouble(temp[7]);
    }

    public static String[] getNonCritical(File file)throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String request = br.readLine();
        String[] nonCritical = new String[0];
        while(request!=null){
            String[] temp = request.split(",");
            String requestType = temp[5];
            if(requestType.compareTo("0") == 0){
                String t[] = new String[nonCritical.length+1];
                for(int i=0;i<nonCritical.length;i++){
                    t[i] = nonCritical[i];
                }
                t[nonCritical.length] = request;
                nonCritical = t;
            }
            request = br.readLine();
        }
        return nonCritical;
    }

    public static String[] getCritical(File file) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String request = br.readLine();
        String[] critical = new String[0];
        while(request!=null){
            String[] temp = request.split(",");
            String requestType = temp[5];
            if(requestType.compareTo("1") == 0){
                String t[] = new String[critical.length+1];
                for(int i=0;i<critical.length;i++){
                    t[i] = critical[i];
                }
                t[critical.length] = request;
                critical = t;
            }
            request = br.readLine();
        }
        return critical;
    }

    public static double distanceBetweenNodeAndRequest(double x1, double y1, double x2, double y2){
        double result = Math.pow((Math.pow((x2-x1),2) + Math.pow((y2-y1),2)), 0.5);
        return result;
    }

    public static double[][] arrangeAccordingToAW(EDGE edge[], String request){
        double latitude = getLatitude(request);
        double longitude = getLongitude(request);
        double dist[][] = new double[edge.length][2];
        for(int i=0;i<edge.length;i++){
            dist[i][0] = i;
            dist[i][1] = edge[i].AW;
        }
        for(int i=0;i<edge.length;i++){
            for(int j=0;j<edge.length-1-i;j++){
                if(dist[j][1] > dist[j+1][1]){
                    double ti = dist[j][0];
                    double taw = dist[j][1];

                    dist[j][0] = dist[j+1][0];
                    dist[j+1][0] = ti;

                    dist[j][1] = dist[j+1][1];
                    dist[j+1][1] = taw;
                }
            }
        }
        return dist;
    }

    public static double[][] arrangeAccordingToAW(FOGNODE fog[], String request){
        double latitude = getLatitude(request);
        double longitude = getLongitude(request);
        double dist[][] = new double[fog.length][2];
        for(int i=0;i<fog.length;i++){
            dist[i][0] = i;
            dist[i][1] = fog[i].AW;
        }
        for(int i=0;i<fog.length;i++){
            for(int j=0;j<fog.length-1-i;j++){
                if(dist[j][1] > dist[j+1][1]){
                    double ti = dist[j][0];
                    double taw = dist[j][1];

                    dist[j][0] = dist[j+1][0];
                    dist[j+1][0] = ti;

                    dist[j][1] = dist[j+1][1];
                    dist[j+1][1] = taw;
                }
            }
        }
        return dist;
    }

    public static double[][] arrangeAccordingToAW(CLOUD cloud[], String request){
        double latitude = getLatitude(request);
        double longitude = getLongitude(request);
        double dist[][] = new double[cloud.length][2];
        for(int i=0;i<cloud.length;i++){
            dist[i][0] = i;
            dist[i][1] = cloud[i].AW;
        }
        for(int i=0;i<cloud.length;i++){
            for(int j=0;j<cloud.length-1-i;j++){
                if(dist[j][1] > dist[j+1][1]){
                    double ti = dist[j][0];
                    double taw = dist[j][1];

                    dist[j][0] = dist[j+1][0];
                    dist[j+1][0] = ti;

                    dist[j][1] = dist[j+1][1];
                    dist[j+1][1] = taw;
                }
            }
        }
        return dist;
    }

    public static boolean edgeAllocation(String request)throws Exception{
        boolean flag = false;
        int i;
        for (i = 0; i < edge.length; i++){
            if (edge[i].freeRam >= getRAM(request) && edge[i].freeCPU >= getCPU(request) && edge[i].freeCapacity >= getCapacity(request)) {
                edge[i].setReq(getId(request), getPriority(request), getStartTime(request), getDuration(request), getRAM(request), getCapacity(request), getCPU(request));
                System.out.println("Request_" + getId(request) + " is allocated to edgeNode_" + i);
                flag = true;
                ereq++;
                break;
            }
        }

        if(i == edge.length){
            flag = false;
        }
        return flag;
    }

    public static boolean fogAllocation(String request) throws Exception {
        int i;
        boolean flag = false;
        for (i = 0; i < fogNode.length; i++){
            if (fogNode[i].freeRam >= getRAM(request) && edge[i].freeCapacity >= getCapacity(request) && edge[i].freeCPU >= getCPU(request)) {
                fogNode[i].setReq(getId(request), getPriority(request), getStartTime(request), getDuration(request), getRAM(request), getCapacity(request), getCPU(request));
                System.out.println("Request_" + getId(request) + " is allocated to fogNode_" + i);
                flag = true;
                freq++;
                break;
            }
        }

        if(i == fogNode.length){
            flag = false;
        }
        return flag;
    }

    public static boolean cloudAllocation(String request)throws Exception{
        int i;
        boolean flag = false;
        for (i = 0; i < cloud.length; i++){
            if (cloud[i].freeRam >= getRAM(request) && cloud[i].freeCapacity >= getCapacity(request) && cloud[i].freeCPU >= getCPU(request)) {
                cloud[i].setReq(getId(request), getPriority(request), getStartTime(request), getDuration(request), getRAM(request), getCapacity(request), getCPU(request));
                System.out.println("Request_" + getId(request) + " is allocated to cloudNode_" + i);
                flag = true;
                creq++;
                break;
            }
        }

        if(i == cloud.length){
            flag = false;
        }
        return flag;
    }

    public static String getPriority(String request){
        String[] temp = request.split(",");
        return temp[5];
    }

    public static double getId(String request){
        String[] temp = request.split(",");
        return Double.parseDouble(temp[0]);
    }

    public static void CPUcalculation(){
        double eru = 0, fru = 0, cru = 0;
        for(int i=0;i<edge.length;i++){
            eru += edge[i].usedCPU/edge[i].CPU;
        }
        eru = eru/edge.length;
        edgeCounter += eru;
        redge ++;

        for(int i=0;i<fogNode.length;i++){
            fru += fogNode[i].usedCPU/fogNode[i].CPU;
        }
        fru = fru/fogNode.length;
        fogCounter += fru;
        rfog ++;

        for(int i=0;i<cloud.length;i++){
            cru += cloud[i].usedCPU/cloud[i].CPU;
        }
        cru = cru/cloud.length;
        cloudCounter += cru;
        rcloud ++;

    }

    public static void Memcalculation(){
        double eru = 0, fru = 0, cru = 0;
        for(int i=0;i<edge.length;i++){
            eru += edge[i].usedRam/edge[i].RAM;
        }
        eru = eru/edge.length;
        medgecounter += eru;
        medge ++;

        for(int i=0;i<fogNode.length;i++){
            fru += fogNode[i].usedRam/fogNode[i].RAM;
        }
        fru = fru/fogNode.length;
        mfogcounter += fru;
        mfog ++;

        for(int i=0;i<cloud.length;i++){
            cru += cloud[i].usedRam/cloud[i].RAM;
        }
        cru = cru/cloud.length;
        mcloudcounter += cru;
        mcloud ++;

    }
}
