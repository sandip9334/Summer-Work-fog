package Controller_V13;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static FOGNODE[] fogNode = new FOGNODE[1];
    public static EDGE[] edge = new EDGE[1];
    public static CLOUD[] cloud = new CLOUD[1];
    public static double cloudCounter = 0, rcloud = 0;
    public static double edgeCounter = 0, redge = 0;
    public static double fogCounter = 0, rfog = 0;

    public static void main(String[] args)throws Exception {

        edge[0] = new EDGE();
        fogNode[0] = new FOGNODE();
        cloud[0] = new CLOUD();

        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Radhey\\OneDrive\\Desktop\\newData500.txt"));
        double systemStartTime = System.currentTimeMillis();
        String request = br.readLine();
        double duration = 0;
        double startTime = getStartTime(request);
        try {
            while (true) {
                double intermediate = System.currentTimeMillis();
                deAllocationEDGE(systemStartTime);
                deAllocationFOG(systemStartTime);
                deAllocationCLOUD(systemStartTime);
                intermediate = System.currentTimeMillis() - intermediate;
                while ((System.currentTimeMillis() - systemStartTime - duration - intermediate) / 1000 < startTime) {
                    ;
                }
                duration = System.currentTimeMillis();
                if (getNodeType(request) == 0) {
                    redge++;
                    edgeAllocation(request);
                } else if (getNodeType(request) == 1) {
                    rfog++;
                    fogAllocation(request);
                } else if (getNodeType(request) == 2) {
                    rcloud++;
                    cloudAllocation(request);
                }
                request = br.readLine();
                if (request == null) {
                    break;
                }
                startTime = getStartTime(request);
                edgeMigration(systemStartTime);
                fogNodeMigration(systemStartTime);
                cloudMigration(systemStartTime);
                duration = System.currentTimeMillis() - duration;
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        System.out.println((System.currentTimeMillis() - systemStartTime) / 1000);
        finalStatus();
    }

    public static void finalStatus(){
        System.out.println();
        System.out.println("Resource Utilisation");
        redge = edgeCounter/redge;
        System.out.println("Edge: " + redge);
        rfog = fogCounter/rfog;
        System.out.println("Fog: " + rfog);
        rcloud = cloudCounter/rcloud;
        System.out.println("Cloud: " + rcloud);
        System.out.println();
        System.out.println("Total Nodes created: ");
        System.out.println("|------>");
        System.out.println("\t\tTotal edgeNode created: " + edge.length);
        System.out.println("\t\tTotal fogNode created: " + fogNode.length);
        System.out.println("\t\tTotal cloudNode created: " + cloud.length);
    }

    public static double getReqId(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[0]);
    }

    public static double getNodeType(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[1]);
    }

    public static double getRequiredResources(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[2]);
    }

    public static double getStartTime(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[3]);
    }

    public static double getTimeDuration(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[4]);
    }

    public static void edgeAllocation(String request)throws Exception{
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
        if(!flag){
            EDGE[] temp = new EDGE[edge.length+1];
            for(i=0;i<edge.length;i++){
                temp[i] = edge[i];
            }
            temp[i] = new EDGE();
            temp[i].setReq(getReqId(request),getStartTime(request),getTimeDuration(request),getRequiredResources(request));
            edgeCounter = edgeCounter + (temp[i].usedCapacity/7)*100;
            System.out.println("Request_" + getReqId(request) + " is allocated to edgeNode_" + i);
            edge = temp;
        }
        else{
            edge[id].setReq(getReqId(request),getStartTime(request),getTimeDuration(request),getRequiredResources(request));
            edgeCounter = edgeCounter + (edge[id].usedCapacity/7)*100;
            System.out.println("Request_" + getReqId(request) + " is allocated to edgeNode_" + id);
        }
    }

    public static void fogAllocation(String request) throws Exception {
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
        if(!flag){
            FOGNODE[] temp = new FOGNODE[fogNode.length+1];
            for(i=0;i<fogNode.length;i++){
                temp[i] = fogNode[i];
            }
            temp[i] = new FOGNODE();
            temp[i].setReq(getReqId(request),getStartTime(request),getTimeDuration(request),getRequiredResources(request));
            fogCounter = fogCounter + 100*(temp[i].usedCapacity/12);
            System.out.println("Request_" + getReqId(request) + " is allocated to fogNode_" + i);
            fogNode = temp;
        }
        else{
            fogNode[id].setReq(getReqId(request),getStartTime(request),getTimeDuration(request),getRequiredResources(request));
            fogCounter = fogCounter + 100*(fogNode[id].usedCapacity/12);
            System.out.println("Request_" + getReqId(request) + " is allocated to fogNode_" + id);
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
        if(!flag){
            CLOUD[] temp = new CLOUD[cloud.length+1];
            for(i=0;i<cloud.length;i++){
                temp[i] = cloud[i];
            }
            temp[i] = new CLOUD();
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

    public static void deAllocationEDGE(double systemStartTime){
        for(int i=0;i<edge.length;i++){
            for(int j=0;j<edge[i].req.length;j++){
                if(edge[i].endTime[j] <= (System.currentTimeMillis()-systemStartTime)/1000){
                    edge[i].freeCapacity = edge[i].freeCapacity + edge[i].reqResource[j];
                    edge[i].usedCapacity = edge[i].resourceCapacity - edge[i].freeCapacity;
                    edge[i].reqResource[j] = 0;
                }
            }
        }
    }

    public static void deAllocationCLOUD(double systemStartTime){
        for(int i=0;i<cloud.length;i++){
            for(int j=0;j<cloud[i].req.length;j++){
                if(cloud[i].endTime[j] <= (System.currentTimeMillis()-systemStartTime)/1000){
                    cloud[i].freeCapacity = cloud[i].freeCapacity + cloud[i].reqResource[j];
                    cloud[i].usedCapacity = cloud[i].resourceCapacity - cloud[i].freeCapacity;
                    cloud[i].reqResource[j] = 0;
                }
            }
        }
    }

    public static void deAllocationFOG(double systemStartTime){
        for(int i=0;i<fogNode.length;i++){
            for(int j=0;j<fogNode[i].req.length;j++){
                if(fogNode[i].endTime[j] <= (System.currentTimeMillis()-systemStartTime)/1000){
                    fogNode[i].freeCapacity = fogNode[i].freeCapacity + fogNode[i].reqResource[j];
                    fogNode[i].usedCapacity = fogNode[i].resourceCapacity - fogNode[i].freeCapacity;
                    fogNode[i].reqResource[j] = 0;
                }
            }
        }
    }

    public static void edgeMigration(double systemStartTime)throws Exception{
        double currentTime = System.currentTimeMillis();
        double[][] nodesOverUtilised = new double[0][2];
        double[][] nodesUnderUtilised = new double[0][2];
        double average = 0;

        //finding the average....
        for(int i=0;i<edge.length;i++){
            average = average + edge[i].usedCapacity/edge[i].resourceCapacity;
        }
        if(edge.length!=0)
            average = average/edge.length;

        //finding the overly utilised node....
        for(int i=0;i<edge.length;i++){
            if(edge[i].usedCapacity/edge[i].resourceCapacity > average){
                double[][] temp = new double[nodesOverUtilised.length+1][2];
                for(int j=0;j<nodesOverUtilised.length;j++){
                    temp[j][0] = nodesOverUtilised[j][0];
                    temp[j][1] = nodesOverUtilised[j][1];
                }
                temp[nodesOverUtilised.length][0] = i;
                temp[nodesOverUtilised.length][1] = edge[i].usedCapacity/edge[i].resourceCapacity;
                nodesOverUtilised = temp;
            }
            else{
                double[][] temp = new double[nodesUnderUtilised.length+1][2];
                for(int j=0;j<nodesUnderUtilised.length;j++){
                    temp[j][0] = nodesUnderUtilised[j][0];
                    temp[j][1] = nodesUnderUtilised[j][1];
                }
                temp[nodesUnderUtilised.length][0] = i;
                temp[nodesUnderUtilised.length][1] = edge[i].usedCapacity/edge[i].resourceCapacity;
                nodesUnderUtilised = temp;
            }
        }

        //arranging the over-utilised-node in the increasing order to its utilisation....
        for(int i=0;i<nodesOverUtilised.length;i++){
            for(int j=0;j<nodesOverUtilised.length-1-i;j++){
                if(nodesOverUtilised[j][1] > nodesOverUtilised[j+1][1]){
                    double temp = nodesOverUtilised[j][0];
                    nodesOverUtilised[j][0] = nodesOverUtilised[j+1][0];
                    nodesOverUtilised[j+1][0] = temp;

                    temp = nodesOverUtilised[j][1];
                    nodesOverUtilised[j][1] = nodesOverUtilised[j+1][1];
                    nodesOverUtilised[j+1][1] = temp;
                }
            }
        }

        //arranging the under-utilised-nodes in the increasing order of utilisation.....
        for(int i=0;i<nodesUnderUtilised.length;i++){
            for(int j=0;j<nodesUnderUtilised.length-1-i;j++){
                if(nodesUnderUtilised[j][1] > nodesUnderUtilised[j+1][1]){
                    double temp = nodesUnderUtilised[j][0];
                    nodesUnderUtilised[j][0] = nodesUnderUtilised[j+1][0];
                    nodesUnderUtilised[j+1][0] = temp;

                    temp = nodesUnderUtilised[j][1];
                    nodesUnderUtilised[j][1] = nodesUnderUtilised[j+1][1];
                    nodesUnderUtilised[j+1][1] = temp;
                }
            }
        }

        //finding the request from the first node that has the lowest completion....
        //Under to Under
        int nodeIndex = -1;
        boolean flag = false;
        double difference = 0;
        int reqId = -1;
        for(int j=0;j<nodesUnderUtilised.length;j++) {
            nodeIndex = (int)nodesUnderUtilised[j][0];
            for (int i = 0; i < edge[nodeIndex].req.length; i++) { //traversing through all the request served by that node...
                if (!flag && edge[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) { //Condition for the request is still running...
                    difference = edge[nodeIndex].endTime[i] - currentTime;
                    reqId = i;
                    flag = true;
                } else if (flag && edge[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) {
                    if (difference > (edge[nodeIndex].endTime[i] - currentTime)) {
                        difference = edge[nodeIndex].endTime[i] - currentTime;
                        reqId = i;
                    }
                }
            }
            break;
        }


        if(nodeIndex != -1 && reqId != -1) {
            String request = edge[nodeIndex].req[reqId] + ",0," + edge[nodeIndex].reqResource[reqId] + "," + (currentTime-systemStartTime)/1000 + "," + (edge[nodeIndex].endTime[reqId] - (currentTime-systemStartTime)/1000);
            int oindex = nodeIndex;
            for (int i = nodesUnderUtilised.length-1; i >= 0; i--) {
                nodeIndex = (int) nodesUnderUtilised[i][0];
                double freeCapacity = edge[nodeIndex].freeCapacity;
                double usedCapacity = edge[nodeIndex].usedCapacity + getRequiredResources(request);
                if (freeCapacity < getRequiredResources(request) || (usedCapacity / 7 <= average) || getRequiredResources(request) == 0) {
                    //Migration not possible...
                } else {
                    redge++;
                    edge[nodeIndex].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                    edgeCounter = edgeCounter + (edge[nodeIndex].usedCapacity/7)*100;

                    //Releasing the memory taken by that request....
                    edge[oindex].freeCapacity = edge[oindex].freeCapacity + edge[oindex].reqResource[reqId];
                    edge[oindex].usedCapacity = edge[oindex].resourceCapacity - edge[oindex].freeCapacity;
                    edge[oindex].reqResource[reqId] = 0;
                    break;
                }
            }
        }
        else{
            //Migration not possible....
        }

        //Over to Under
        nodeIndex = -1;
        flag = false;
        difference = 0;
        reqId = -1;
        for(int j=nodesOverUtilised.length-1;j>=0;j--) {
            nodeIndex = (int)nodesOverUtilised[j][0];
            for (int i = 0; i < edge[nodeIndex].req.length; i++) { //traversing through all the request served by that node...
                if (!flag && edge[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) { //Condition for the request is still running...
                    difference = edge[nodeIndex].endTime[i] - currentTime;
                    reqId = i;
                    flag = true;
                } else if (flag && edge[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) {
                    if (difference > (edge[nodeIndex].endTime[i] - currentTime)) {
                        difference = edge[nodeIndex].endTime[i] - currentTime;
                        reqId = i;
                    }
                }
            }
            break;
        }


        if(nodeIndex != -1 && reqId != -1) {
            String request = edge[nodeIndex].req[reqId] + ",0," + edge[nodeIndex].reqResource[reqId] + "," + (currentTime-systemStartTime)/1000 + "," + (edge[nodeIndex].endTime[reqId] - (currentTime-systemStartTime)/1000);
            int oindex = nodeIndex;
            for (int i = 0; i < nodesUnderUtilised.length; i++) {
                nodeIndex = (int) nodesUnderUtilised[i][0];
                double freeCapacity = edge[nodeIndex].freeCapacity;
                double usedCapacity = edge[nodeIndex].usedCapacity + getRequiredResources(request);
                if (freeCapacity < getRequiredResources(request) || (usedCapacity / 7 <= average) || getRequiredResources(request) == 0) {
                    //Migration not possible...
                } else {
                    redge++;
                    edge[nodeIndex].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                    edgeCounter = edgeCounter + (edge[nodeIndex].usedCapacity/7)*100;

                    //Releasing the memory taken by that request....
                    edge[oindex].freeCapacity = edge[oindex].freeCapacity + edge[oindex].reqResource[reqId];
                    edge[oindex].usedCapacity = edge[oindex].resourceCapacity - edge[oindex].freeCapacity;
                    edge[oindex].reqResource[reqId] = 0;
                    break;
                }
            }
        }
        else{
            //Migration not possible....
        }

    }

    public static void fogNodeMigration(double systemStartTime)throws Exception{
        double currentTime = System.currentTimeMillis();
        double[][] nodesOverUtilised = new double[0][2];
        double[][] nodesUnderUtilised = new double[0][2];
        double average = 0;

        //finding the average....
        for(int i=0;i<fogNode.length;i++){
            average = average + fogNode[i].usedCapacity/fogNode[i].resourceCapacity;
        }
        if(fogNode.length!=0)
            average = average/fogNode.length;

        //finding the overly utilised node....
        for(int i=0;i<fogNode.length;i++){
            if(fogNode[i].usedCapacity/fogNode[i].resourceCapacity > average){
                double[][] temp = new double[nodesOverUtilised.length+1][2];
                for(int j=0;j<nodesOverUtilised.length;j++){
                    temp[j][0] = nodesOverUtilised[j][0];
                    temp[j][1] = nodesOverUtilised[j][1];
                }
                temp[nodesOverUtilised.length][0] = i;
                temp[nodesOverUtilised.length][1] = fogNode[i].usedCapacity/fogNode[i].resourceCapacity;
                nodesOverUtilised = temp;
            }
            else{
                double[][] temp = new double[nodesUnderUtilised.length+1][2];
                for(int j=0;j<nodesUnderUtilised.length;j++){
                    temp[j][0] = nodesUnderUtilised[j][0];
                    temp[j][1] = nodesUnderUtilised[j][1];
                }
                temp[nodesUnderUtilised.length][0] = i;
                temp[nodesUnderUtilised.length][1] = fogNode[i].usedCapacity/fogNode[i].resourceCapacity;
                nodesUnderUtilised = temp;
            }
        }

        //arranging the over-utilised-node in the increasing order to its utilisation....
        for(int i=0;i<nodesOverUtilised.length;i++){
            for(int j=0;j<nodesOverUtilised.length-1-i;j++){
                if(nodesOverUtilised[j][1] > nodesOverUtilised[j+1][1]){
                    double temp = nodesOverUtilised[j][0];
                    nodesOverUtilised[j][0] = nodesOverUtilised[j+1][0];
                    nodesOverUtilised[j+1][0] = temp;

                    temp = nodesOverUtilised[j][1];
                    nodesOverUtilised[j][1] = nodesOverUtilised[j+1][1];
                    nodesOverUtilised[j+1][1] = temp;
                }
            }
        }

        //arranging the under-utilised-nodes in the increasing order of utilisation.....
        for(int i=0;i<nodesUnderUtilised.length;i++){
            for(int j=0;j<nodesUnderUtilised.length-1-i;j++){
                if(nodesUnderUtilised[j][1] > nodesUnderUtilised[j+1][1]){
                    double temp = nodesUnderUtilised[j][0];
                    nodesUnderUtilised[j][0] = nodesUnderUtilised[j+1][0];
                    nodesUnderUtilised[j+1][0] = temp;

                    temp = nodesUnderUtilised[j][1];
                    nodesUnderUtilised[j][1] = nodesUnderUtilised[j+1][1];
                    nodesUnderUtilised[j+1][1] = temp;
                }
            }
        }
        //finding the request from the first node that has the lowest completion....
        //Under to Under
        int nodeIndex = -1;
        boolean flag = false;
        double difference = 0;
        int reqId = -1;
        for(int j=0;j<nodesUnderUtilised.length;j++) {
            nodeIndex = (int)nodesUnderUtilised[j][0];
            for (int i = 0; i < fogNode[nodeIndex].req.length; i++) { //traversing through all the request served by that node...
                if (!flag && fogNode[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) { //Condition for the request is still running...
                    difference = fogNode[nodeIndex].endTime[i] - currentTime;
                    reqId = i;
                    flag = true;
                } else if (flag && fogNode[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) {
                    if (difference > (fogNode[nodeIndex].endTime[i] - currentTime)) {
                        difference = fogNode[nodeIndex].endTime[i] - currentTime;
                        reqId = i;
                    }
                }
            }
            break;
        }

        if(nodeIndex != -1 && reqId != -1) {
            String request = fogNode[nodeIndex].req[reqId] + ",0," + fogNode[nodeIndex].reqResource[reqId] + "," + (currentTime-systemStartTime)/1000 + "," + (fogNode[nodeIndex].endTime[reqId] - (currentTime-systemStartTime)/1000);
            int oindex = nodeIndex;
            for (int i = nodesUnderUtilised.length-1; i >= 0; i--) {
                nodeIndex = (int) nodesUnderUtilised[i][0];
                double freeCapacity = fogNode[nodeIndex].freeCapacity;
                double usedCapacity = fogNode[nodeIndex].usedCapacity + getRequiredResources(request);
                if (freeCapacity < getRequiredResources(request) || (usedCapacity / 12 <= average) || getRequiredResources(request) == 0) {
                    //Migration not possible...
                } else {
                    rfog++;
                    fogNode[nodeIndex].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                    fogCounter = fogCounter + (fogNode[nodeIndex].usedCapacity/12)*100;

                    //Releasing the memory taken by that request....
                    fogNode[oindex].freeCapacity = fogNode[oindex].freeCapacity + fogNode[oindex].reqResource[reqId];
                    fogNode[oindex].usedCapacity = fogNode[oindex].resourceCapacity - fogNode[oindex].freeCapacity;
                    fogNode[oindex].reqResource[reqId] = 0;
                    break;
                }
            }
        }
        else{
            //Migration not possible....
        }
        //Over to Under
        nodeIndex = -1;
        flag = false;
        difference = 0;
        reqId = -1;
        for(int j=nodesOverUtilised.length-1;j>=0;j--) {
            nodeIndex = (int)nodesOverUtilised[j][0];
            for (int i = 0; i < fogNode[nodeIndex].req.length; i++) { //traversing through all the request served by that node...
                if (!flag && fogNode[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) { //Condition for the request is still running...
                    difference = fogNode[nodeIndex].endTime[i] - currentTime;
                    reqId = i;
                    flag = true;
                } else if (flag && fogNode[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) {
                    if (difference > (fogNode[nodeIndex].endTime[i] - currentTime)) {
                        difference = fogNode[nodeIndex].endTime[i] - currentTime;
                        reqId = i;
                    }
                }
            }
            break;
        }


        if(nodeIndex != -1 && reqId != -1) {
            String request = fogNode[nodeIndex].req[reqId] + ",0," + fogNode[nodeIndex].reqResource[reqId] + "," + (currentTime-systemStartTime)/1000 + "," + (fogNode[nodeIndex].endTime[reqId] - (currentTime-systemStartTime)/1000);
            int oindex = nodeIndex;
            for (int i = 0; i < nodesUnderUtilised.length; i++) {
                nodeIndex = (int) nodesUnderUtilised[i][0];
                double freeCapacity = fogNode[nodeIndex].freeCapacity;
                double usedCapacity = fogNode[nodeIndex].usedCapacity + getRequiredResources(request);
                if (freeCapacity < getRequiredResources(request) || (usedCapacity / 12 <= average) || getRequiredResources(request) == 0) {
                    //Migration not possible...
                } else {
                    rfog++;
                    fogNode[nodeIndex].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                    fogCounter = fogCounter + (fogNode[nodeIndex].usedCapacity/12)*100;

                    //Releasing the memory taken by that request....
                    fogNode[oindex].freeCapacity = fogNode[oindex].freeCapacity + fogNode[oindex].reqResource[reqId];
                    fogNode[oindex].usedCapacity = fogNode[oindex].resourceCapacity - fogNode[oindex].freeCapacity;
                    fogNode[oindex].reqResource[reqId] = 0;
                    break;
                }
            }
        }
        else{
            //Migration not possible....
        }

    }

    public static void cloudMigration(double systemStartTime)throws Exception{
        double currentTime = System.currentTimeMillis();
        double[][] nodesOverUtilised = new double[0][2];
        double[][] nodesUnderUtilised = new double[0][2];
        double average = 0;

        //finding the average....
        for(int i=0;i<cloud.length;i++){
            average = average + cloud[i].usedCapacity/cloud[i].resourceCapacity;
        }
        average = average/cloud.length;

        //finding the overly utilised node....
        for(int i=0;i<cloud.length;i++){
            if(cloud[i].usedCapacity/cloud[i].resourceCapacity > average){
                double[][] temp = new double[nodesOverUtilised.length+1][2];
                for(int j=0;j<nodesOverUtilised.length;j++){
                    temp[j][0] = nodesOverUtilised[j][0];
                    temp[j][1] = nodesOverUtilised[j][1];
                }
                temp[nodesOverUtilised.length][0] = i;
                temp[nodesOverUtilised.length][1] = cloud[i].usedCapacity/cloud[i].resourceCapacity;
                nodesOverUtilised = temp;
            }
            else{
                double[][] temp = new double[nodesUnderUtilised.length+1][2];
                for(int j=0;j<nodesUnderUtilised.length;j++){
                    temp[j][0] = nodesUnderUtilised[j][0];
                    temp[j][1] = nodesUnderUtilised[j][1];
                }
                temp[nodesUnderUtilised.length][0] = i;
                temp[nodesUnderUtilised.length][1] = cloud[i].usedCapacity/cloud[i].resourceCapacity;
                nodesUnderUtilised = temp;
            }
        }

        //arranging the over-utilised-node in the increasing order to its utilisation....
        for(int i=0;i<nodesOverUtilised.length;i++){
            for(int j=0;j<nodesOverUtilised.length-1-i;j++){
                if(nodesOverUtilised[j][1] > nodesOverUtilised[j+1][1]){
                    double temp = nodesOverUtilised[j][0];
                    nodesOverUtilised[j][0] = nodesOverUtilised[j+1][0];
                    nodesOverUtilised[j+1][0] = temp;

                    temp = nodesOverUtilised[j][1];
                    nodesOverUtilised[j][1] = nodesOverUtilised[j+1][1];
                    nodesOverUtilised[j+1][1] = temp;
                }
            }
        }

        //arranging the under-utilised-nodes in the increasing order of utilisation.....
        for(int i=0;i<nodesUnderUtilised.length;i++){
            for(int j=0;j<nodesUnderUtilised.length-1-i;j++){
                if(nodesUnderUtilised[j][1] > nodesUnderUtilised[j+1][1]){
                    double temp = nodesUnderUtilised[j][0];
                    nodesUnderUtilised[j][0] = nodesUnderUtilised[j+1][0];
                    nodesUnderUtilised[j+1][0] = temp;

                    temp = nodesUnderUtilised[j][1];
                    nodesUnderUtilised[j][1] = nodesUnderUtilised[j+1][1];
                    nodesUnderUtilised[j+1][1] = temp;
                }
            }
        }

        //finding the request from the first node that has the lowest completion....
        //Under to Under
        int nodeIndex = -1;
        boolean flag = false;
        double difference = 0;
        int reqId = -1;
        for(int j=0;j<nodesUnderUtilised.length;j++) {
            nodeIndex = (int)nodesUnderUtilised[j][0];
            for (int i = 0; i < cloud[nodeIndex].req.length; i++) { //traversing through all the request served by that node...
                if (!flag && cloud[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) { //Condition for the request is still running...
                    difference = cloud[nodeIndex].endTime[i] - currentTime;
                    reqId = i;
                    flag = true;
                } else if (flag && cloud[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) {
                    if (difference > (cloud[nodeIndex].endTime[i] - currentTime)) {
                        difference = cloud[nodeIndex].endTime[i] - currentTime;
                        reqId = i;
                    }
                }
            }
            break;
        }


        if(nodeIndex != -1 && reqId != -1) {
            String request = cloud[nodeIndex].req[reqId] + ",0," + cloud[nodeIndex].reqResource[reqId] + "," + (currentTime-systemStartTime)/1000 + "," + (cloud[nodeIndex].endTime[reqId] - (currentTime-systemStartTime)/1000);
            int oindex = nodeIndex;
            for (int i = nodesUnderUtilised.length-1; i >= 0; i--) {
                nodeIndex = (int) nodesUnderUtilised[i][0];
                double freeCapacity = cloud[nodeIndex].freeCapacity;
                double usedCapacity = cloud[nodeIndex].usedCapacity + getRequiredResources(request);
                if (freeCapacity < getRequiredResources(request) || (usedCapacity / 18 <= average) || getRequiredResources(request) == 0) {
                    //Migration not possible...
                } else {
                    rcloud++;
                    cloud[nodeIndex].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                    cloudCounter = cloudCounter + (cloud[nodeIndex].usedCapacity/18)*100;

                    //Releasing the memory taken by that request....
                    cloud[oindex].freeCapacity = cloud[oindex].freeCapacity + cloud[oindex].reqResource[reqId];
                    cloud[oindex].usedCapacity = cloud[oindex].resourceCapacity - cloud[oindex].freeCapacity;
                    cloud[oindex].reqResource[reqId] = 0;
                    break;
                }
            }
        }
        else{
            //Migration not possible....
        }

        //Over to Under
        nodeIndex = -1;
        flag = false;
        difference = 0;
        reqId = -1;
        for(int j=nodesOverUtilised.length-1;j>=0;j--) {
            nodeIndex = (int)nodesOverUtilised[j][0];
            for (int i = 0; i < cloud[nodeIndex].req.length; i++) { //traversing through all the request served by that node...
                if (!flag && cloud[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) { //Condition for the request is still running...
                    difference = cloud[nodeIndex].endTime[i] - currentTime;
                    reqId = i;
                    flag = true;
                } else if (flag && cloud[nodeIndex].endTime[i] > (currentTime - systemStartTime) / 1000) {
                    if (difference > (cloud[nodeIndex].endTime[i] - currentTime)) {
                        difference = cloud[nodeIndex].endTime[i] - currentTime;
                        reqId = i;
                    }
                }
            }
            break;
        }


        if(nodeIndex != -1 && reqId != -1) {
            String request = cloud[nodeIndex].req[reqId] + ",0," + cloud[nodeIndex].reqResource[reqId] + "," + (currentTime-systemStartTime)/1000 + "," + (cloud[nodeIndex].endTime[reqId] - (currentTime-systemStartTime)/1000);
            int oindex = nodeIndex;
            for (int i = 0; i < nodesUnderUtilised.length; i++) {
                nodeIndex = (int) nodesUnderUtilised[i][0];
                double freeCapacity = cloud[nodeIndex].freeCapacity;
                double usedCapacity = cloud[nodeIndex].usedCapacity + getRequiredResources(request);
                if (freeCapacity < getRequiredResources(request) || (usedCapacity / 18 <= average) || getRequiredResources(request) == 0) {
                    //Migration not possible...
                } else {
                    rcloud++;
                    cloud[nodeIndex].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                    cloudCounter = cloudCounter + (cloud[nodeIndex].usedCapacity/18)*100;

                    //Releasing the memory taken by that request....
                    cloud[oindex].freeCapacity = cloud[oindex].freeCapacity + cloud[oindex].reqResource[reqId];
                    cloud[oindex].usedCapacity = cloud[oindex].resourceCapacity - cloud[oindex].freeCapacity;
                    cloud[oindex].reqResource[reqId] = 0;
                    break;
                }
            }
        }
        else{
            //Migration not possible....
        }

    }


}
