package BFDWN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {
    public static EDGE[] edge = new EDGE[36];
    public static FOGNODE[] fogNode = new FOGNODE[38];
    public static CLOUD[] cloud = new CLOUD[34];
    public static double cloudCounter = 0, rcloud = 0, cNode = 0;
    public static double edgeCounter = 0, redge = 0, eNode = 0;
    public static double fogCounter = 0, rfog = 0, fNode = 0;

    public static void main(String[] args) throws Exception {

        for(int i=0;i<edge.length;i++)
            edge[i] = new EDGE();
        for(int i=0;i<fogNode.length;i++)
            fogNode[i] = new FOGNODE();
        for(int i=0;i<cloud.length;i++)
            cloud[i] = new CLOUD();

        File file = new File("C:\\Users\\Radhey\\OneDrive\\Desktop\\newData2000.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] tempSubset = subset(file);
        String[][] subset = new String[tempSubset.length][];
        for (int i = 0; i < tempSubset.length; i++) {
            subset[i] = tempSubset[i].split("-");
        }
        for (int i = 0; i < subset.length; i++) {
            subset[i] = decreasing(subset[i]);
        }

        double systemStartTime = System.currentTimeMillis();
        int timeCounter = 0;
        int reqCounter = 0;
        String request = subset[timeCounter][reqCounter];
        double duration = 0;
        double startTime = getStartTime(request);
        while (true) {
            double intermediate = System.currentTimeMillis();
            deAllocationEDGE(systemStartTime);
            deAllocationFOG(systemStartTime);
            deAllocationCLOUD(systemStartTime);
            intermediate = System.currentTimeMillis() - intermediate;
            while ((System.currentTimeMillis() - systemStartTime - duration - intermediate) / 1000 < startTime) {
            }
            duration = System.currentTimeMillis();
            boolean flag = edgeAllocation(request);
            if(!flag){
                flag = fogAllocation(request);
                if(!flag){
                    rcloud++;
                    cloudAllocation(request);
                }
                else{
                    rfog++;
                }
            }
            else{
                redge++;
            }
            duration = System.currentTimeMillis() - duration;
            reqCounter++;
            if (reqCounter == subset[timeCounter].length-1) {
                if (timeCounter == subset.length - 1) {
                    break;
                } else {
                    reqCounter = 0;
                    timeCounter++;
                    request = subset[timeCounter][reqCounter];
                }
            } else {
                request = subset[timeCounter][reqCounter];
            }
            startTime = getStartTime(request);
        }

        finalStatus();
    }

    public static void finalStatus() {
        System.out.println();
        for (int i = 0; i < fogNode.length; i++) {
            System.out.println("fogNode_" + i + " served " + fogNode[i].req.length + " total requests.");
        }
        for (int i = 0; i < edge.length; i++) {
            System.out.println("edgeNode_" + i + " served " + edge[i].req.length + " total requests.");
        }
        for (int i = 0; i < cloud.length; i++) {
            System.out.println("cloudNode_" + i + " served " + cloud[i].req.length + " total requests.");
        }

        System.out.println();
        System.out.println("Resource Utilisation");
        double nredge = edgeCounter / redge;
        System.out.println("Edge: " + nredge);
        double nrfog = fogCounter / rfog;
        System.out.println("Fog: " + nrfog);
        double nrcloud = cloudCounter / rcloud;
        System.out.println("Cloud: " + nrcloud);
        System.out.println();
        System.out.println("Request on Edge: " + redge);
        System.out.println("Request on Fog: " + rfog);
        System.out.println("Request on Cloud: " + rcloud);
        System.out.println("Edge Node: " + eNode);
        System.out.println("Fog Node: " + fNode);
        System.out.println("Cloud Node: " + cNode);
        System.out.println("Total Nodes created: ");
        System.out.println("|------>");
        System.out.println("\t\tTotal edgeNode created: " + edge.length + "\n\t\tTotal services served by EdgeLayer " + redge);
        System.out.println("\t\tTotal fogNode created: " + fogNode.length + "\n\t\tTotal services served by FogLayer " + rfog);
        System.out.println("\t\tTotal cloudNode created: " + cloud.length + "\n\t\tTotal services served by CloudLayer " + rcloud);
    }

    public static String[] decreasing(String[] str) {

        double[][] idRes = new double[str.length][2];
        for (int i = 0; i < str.length; i++) {
            idRes[i][0] = getReqId(str[i]);
            idRes[i][1] = getRequiredResources(str[i]);
        }

        for (int i = 0; i < idRes.length; i++) {
            for (int j = 0; j < idRes.length - 1 - i; j++) {
                if (idRes[j][1] < idRes[j + 1][1]) {
                    double temp = idRes[j][0];
                    idRes[j][0] = idRes[j + 1][0];
                    idRes[j + 1][0] = temp;

                    temp = idRes[j][1];
                    idRes[j][1] = idRes[j + 1][1];
                    idRes[j + 1][1] = temp;
                }
            }
        }
        String string = "";
        for (int i = 0; i < idRes.length; i++) {
            for (int j = 0; j < str.length; j++) {
                if (getReqId(str[j]) == idRes[i][0]) {
                    string = string + str[j] + " - ";
                }
            }
        }
        return string.split("-");

    }

    public static String[] subset(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String request = br.readLine();
        String[] result = new String[1];
        result[0] = "";
        int counter = 0;
        double st = getStartTime(request);
        result[counter] = result[counter] + request + "-";
        request = br.readLine();
        try {
            while (request != null) {
                if (st == getStartTime(request)) {
                    result[counter] = result[counter] + request + "-";
                } else {
                    String[] temp = new String[result.length + 1];
                    int i;
                    for (i = 0; i < result.length; i++) {
                        temp[i] = result[i];
                    }
                    temp[i] = "";
                    temp[i] = temp[i] + request + "-";
                    result = temp;
                    counter++;
                    st = getStartTime(request);
                }
                request = br.readLine();
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static double getReqId(String request) {
        String[] temp = request.split(",");
        return Double.parseDouble(temp[0]);
    }

    public static double getNodeType(String request) {
        String[] temp = request.split(",");
        return Double.parseDouble(temp[1]);
    }

    public static double getRequiredResources(String request) {
        String[] temp = request.split(",");
        return Double.parseDouble(temp[2]);
    }

    public static double getStartTime(String request) {
        String[] temp = request.split(",");
        return Double.parseDouble(temp[3]);
    }

    public static double getTimeDuration(String request) {
        String[] temp = request.split(",");
        return Double.parseDouble(temp[4]);
    }

    public static boolean fogAllocation(String request) throws Exception {
        double reqResource = getRequiredResources(request);
        int i;
        int id = -1;
        double difference = -1;
        boolean flag = false;
        for(i=0;i<fogNode.length;i++){
            double temp = fogNode[i].freeCapacity - reqResource;
            if(!flag && temp>=0){
                id = i;
                difference = temp;
                flag = true;
            }
            else if(flag){
                if(temp<difference && temp>=0){
                    id = i;
                    difference = temp;
                }
            }
        }
        if(id>fNode)
            fNode = id;
        if(!flag){
            return flag;
        }
        else{
            fogNode[id].setReq(getReqId(request),getStartTime(request),getTimeDuration(request),getRequiredResources(request));
            fogCounter = fogCounter + 100*(fogNode[id].usedCapacity/12);
            System.out.println("Request_" + getReqId(request) + " is allocated to fogNode_" + id);
            return flag;
        }
    }

    public static void cloudAllocation(String request)throws Exception{
        double reqResource = getRequiredResources(request);
        int i;
        int id = -1;
        double difference = -1;
        boolean flag = false;
        for(i=0;i<cloud.length;i++){
            double temp = cloud[i].freeCapacity - reqResource;
            if(!flag && temp>=0){
                id = i;
                difference = temp;
                flag = true;
            }
            else if(flag){
                if(temp<difference && temp>=0){
                    id = i;
                    difference = temp;
                }
            }
        }
        if(id>cNode)
            cNode  = id;
        if(!flag){
            CLOUD[] temp = new CLOUD[cloud.length+1];
            for(i=0;i<cloud.length;i++){
                temp[i] = cloud[i];
            }
            temp[i] = new CLOUD();
            if(i>cNode)
                cNode = i;
            temp[i].setReq(getReqId(request),getStartTime(request),getTimeDuration(request),getRequiredResources(request));
            cloudCounter = cloudCounter + (temp[i].usedCapacity/18)*100;
            System.out.println("Request_" + getReqId(request) + " is allocated to cloudNode_" + i);
            cloud = temp;
        }
        else{
            cloud[id].setReq(getReqId(request),getStartTime(request),getTimeDuration(request),getRequiredResources(request));
            cloudCounter = cloudCounter + (cloud[id].usedCapacity/18)*100;
            System.out.println("Request_" + getReqId(request) + " is allocated to cloudNode_" + id);
        }
    }

    public static boolean edgeAllocation(String request)throws Exception{
        double reqResource = getRequiredResources(request);
        int i;
        int id = -1;
        double difference = -1;
        boolean flag = false;
        for(i=0;i<edge.length;i++){
            double temp = edge[i].freeCapacity - reqResource;
            if(!flag && temp>=0){
                id = i;
                difference = temp;
                flag = true;
            }
            else if(flag){
                if(temp<difference && temp>=0){
                    id = i;
                    difference = temp;
                }
            }
        }
        if(id>eNode)
            eNode = id;
        if(!flag){
            return flag;
        }
        else{
            edge[id].setReq(getReqId(request),getStartTime(request),getTimeDuration(request),getRequiredResources(request));
            edgeCounter = edgeCounter + (edge[id].usedCapacity/7)*100;
            System.out.println("Request_" + getReqId(request) + " is allocated to edgeNode_" + id);
            return true;
        }
    }

    public static void deAllocationEDGE(double systemStartTime) {
        for (int i = 0; i < edge.length; i++) {
            for (int j = 0; j < edge[i].req.length; j++) {
                if (edge[i].endTime[j] <= (System.currentTimeMillis() - systemStartTime) / 1000) {
                    edge[i].freeCapacity = edge[i].freeCapacity + edge[i].reqResource[j];
                    edge[i].usedCapacity = edge[i].resourceCapacity - edge[i].freeCapacity;
                    edge[i].reqResource[j] = 0;
                }
            }
        }
    }

    public static void deAllocationCLOUD(double systemStartTime) {
        for (int i = 0; i < cloud.length; i++) {
            for (int j = 0; j < cloud[i].req.length; j++) {
                if (cloud[i].endTime[j] <= (System.currentTimeMillis() - systemStartTime) / 1000) {
                    cloud[i].freeCapacity = cloud[i].freeCapacity + cloud[i].reqResource[j];
                    cloud[i].usedCapacity = cloud[i].resourceCapacity - cloud[i].freeCapacity;
                    cloud[i].reqResource[j] = 0;
                }
            }
        }
    }

    public static void deAllocationFOG(double systemStartTime) {
        for (int i = 0; i < fogNode.length; i++) {
            for (int j = 0; j < fogNode[i].req.length; j++) {
                if (fogNode[i].endTime[j] <= (System.currentTimeMillis() - systemStartTime) / 1000) {
                    fogNode[i].freeCapacity = fogNode[i].freeCapacity + fogNode[i].reqResource[j];
                    fogNode[i].usedCapacity = fogNode[i].resourceCapacity - fogNode[i].freeCapacity;
                    fogNode[i].reqResource[j] = 0;
                }
            }
        }
    }
}
