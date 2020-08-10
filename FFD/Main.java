package FFD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {
    public static FOGNODE[] fogNode = new FOGNODE[1];
    public static EDGE[] edge = new EDGE[1];
    public static CLOUD[] cloud = new CLOUD[1];
    public static double cloudCounter = 0, rcloud = 0;
    public static double edgeCounter = 0, redge = 0;
    public static double fogCounter = 0, rfog = 0;

    public static void main(String[] args) throws Exception {

        edge[0] = new EDGE();
        fogNode[0] = new FOGNODE();
        cloud[0] = new CLOUD();

        File file = new File("C:\\Users\\Radhey\\OneDrive\\Desktop\\newData500.txt");
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

    public static void fogAllocation(String request) throws Exception {
        double reqResource = getRequiredResources(request);
        int i;
        for (i = 0; i < fogNode.length; i++) {
            if (fogNode[i].freeCapacity >= reqResource) {
                fogNode[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                fogCounter = fogCounter + (fogNode[i].usedCapacity/12) * 100;
                System.out.println("Request_" + getReqId(request) + " is allocated to fogNode_" + i);
                break;
            }
        }

        if (i == fogNode.length) {
            FOGNODE[] temp = new FOGNODE[fogNode.length + 1];
            for (i = 0; i < fogNode.length; i++) {
                temp[i] = fogNode[i];
            }
            temp[i] = new FOGNODE();
            temp[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
            fogCounter = fogCounter + (temp[i].usedCapacity/12) * 100;
            System.out.println("Request_" + getReqId(request) + " is allocated to fogNode_" + i);
            fogNode = temp;
        }
    }

    public static void cloudAllocation(String request) throws Exception {
        double reqResource = getRequiredResources(request);
        int i;
        for (i = 0; i < cloud.length; i++) {
            if (cloud[i].freeCapacity >= reqResource) {
                cloud[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                cloudCounter = cloudCounter + (cloud[i].usedCapacity/18) * 100;
                System.out.println("Request_" + getReqId(request) + " is allocated to cloudNode_" + i);
                break;
            }
        }

        if (i == cloud.length) {
            CLOUD[] temp = new CLOUD[cloud.length + 1];
            for (i = 0; i < cloud.length; i++) {
                temp[i] = cloud[i];
            }
            temp[i] = new CLOUD();
            temp[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
            cloudCounter = cloudCounter + (temp[i].usedCapacity/18) * 100;
            System.out.println("Request_" + getReqId(request) + " is allocated to cloudNode_" + i);
            cloud = temp;
        }
    }

    public static void edgeAllocation(String request) throws Exception {
        double reqResource = getRequiredResources(request);
        int i;
        for (i = 0; i < edge.length; i++) {
            if (edge[i].freeCapacity >= reqResource) {
                edge[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                edgeCounter = edgeCounter + (edge[i].usedCapacity/7) * 100;
                System.out.println("Request_" + getReqId(request) + " is allocated to edgeNode_" + i);
                break;
            }
        }

        if (i == edge.length) {
            EDGE[] temp = new EDGE[edge.length + 1];
            for (i = 0; i < edge.length; i++) {
                temp[i] = edge[i];
            }
            temp[i] = new EDGE();
            temp[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
            edgeCounter = edgeCounter + (temp[i].usedCapacity/7) * 100;
            System.out.println("Request_" + getReqId(request) + " is allocated to edgeNode_" + i);
            edge = temp;
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
