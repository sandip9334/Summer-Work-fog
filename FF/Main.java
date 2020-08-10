package FF;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static FOGNODE[] fogNode = new FOGNODE[0];
    public static EDGE[] edge = new EDGE[0];
    public static CLOUD[] cloud = new CLOUD[0];
    public static double cloudCounter = 0, rcloud = 0, ca = 0, cRU = 0, cLB = 0, scRU = 0, scLB = 0, cMakeSpan = 0;
    public static double edgeCounter = 0, redge = 0, ea = 0, eRU = 0, eLB = 0, seRU =0, seLB = 0, eMakeSpan = 0;
    public static double fogCounter = 0, rfog = 0, fa = 0, fRU = 0, fLB = 0, sfRU = 0, sfLB = 0, fMakeSpan = 0;

    public static void main(String[] args)throws Exception {

        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Radhey\\OneDrive\\Desktop\\newData10.txt"));
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
                    if(edge.length == 0){
                        EDGE temp[] = new EDGE[edge.length+1];
                        temp[0] = new EDGE();
                        edge = temp;
                    }
                    edgeAllocation(request);
                    edgeCalc();
                } else if (getNodeType(request) == 1) {
                    if(fogNode.length == 0){
                        FOGNODE temp[] = new FOGNODE[fogNode.length+1];
                        temp[0] = new FOGNODE();
                        fogNode = temp;
                    }
                    fogAllocation(request);
                    fogNodeCalc();
                } else if (getNodeType(request) == 2) {
                    if(cloud.length == 0){
                        CLOUD temp[] = new CLOUD[cloud.length+1];
                        temp[0] = new CLOUD();
                        cloud = temp;
                    }
                    cloudAllocation(request);
                    cloudCalc();
                }
                duration = System.currentTimeMillis() - duration;
                request = br.readLine();
                if (request == null) {
                    break;
                }
                startTime = getStartTime(request);
            }
        }
        catch (Exception e){
            ;
        }

        System.out.println((System.currentTimeMillis() - systemStartTime) / 1000);
        finalStatus();
    }


    public static void edgeCalc(){
        redge++;
        //Equation 2
        for(int i=0;i<edge.length;i++){
            edge[i].ru = edge[i].usedCapacity/7;
        }

        //Equation 3
        for(int i=0;i<edge.length;i++){
            for(int j=0;j<edge[i].reqResource.length;j++){
                if(edge[i].reqResource[j] > 0){
                    edge[i].ns ++;
                }
            }
        }

        //Equation 4
        for(int i=0;i<edge.length;i++){
            int fm = 0;
            if(edge[i].ns>0){
                fm = 1;
            }
            ea = ea + (fm);
        }

        //Equation 7
        for(int i=0; i<edge.length;i++){
            for(int j=0;j<edge[i].reqResource.length;j++){
                int fm = 0;
                int im = 0;
                if(edge[i].reqResource[j]>0){
                    im = 1;
                }
                if(edge[i].ns>0){
                    fm = 1;
                }
                eRU = eRU + (im*fm*edge[i].ru);
            }
        }
        eRU = eRU/ea;
        seRU = seRU + eRU;

        //Equation 8
        for(int i=0;i<edge.length;i++){
            edge[i].lb = Math.pow((edge[i].ru - eRU), 2);
        }

        //Equation 9
        for(int i=0;i<edge.length;i++){
            for(int j=0;j<edge[i].reqResource.length;j++){
                int im = 0;
                int fm = 0;
                if(edge[i].ns>0){
                    fm = 1;
                }
                if(edge[i].reqResource[j]>0){
                    im = 1;
                }
                eLB = eLB + (im*fm*edge[i].lb);
            }
        }
        eLB = eLB/ea;
        seLB = seLB + eLB;
        System.out.println("eru: " + eRU);
        System.out.println("eLB: " + eLB);
        System.out.println("a: " + ea);
        eLB = 0;
        ea = 0;
        eRU = 0;


        //Reassigning 0
        for(int i=0;i<edge.length;i++){
            System.out.print("lb: " + edge[i].lb + " ns: " + edge[i].ns + " ru: " + edge[i].ru + "\n");
            edge[i].lb = 0;
            edge[i].ns = 0;
            edge[i].ru = 0;
        }
    }
    public static void fogNodeCalc(){
        rfog++;
        //Equation 2
        for(int i=0;i<fogNode.length;i++){
            fogNode[i].ru = fogNode[i].usedCapacity/7;
        }

        //Equation 3
        for(int i=0;i<fogNode.length;i++){
            for(int j=0;j<fogNode[i].reqResource.length;j++){
                if(fogNode[i].reqResource[j] > 0){
                    fogNode[i].ns ++;
                }
            }
        }

        //Equation 4
        for(int i=0;i<fogNode.length;i++){
            int fm = 0;
            if(fogNode[i].ns>0){
                fm = 1;
            }
            fa = fa + (fm);
        }

        //Equation 7
        for(int i=0; i<fogNode.length;i++){
            for(int j=0;j<fogNode[i].reqResource.length;j++){
                int fm = 0;
                int im = 0;
                if(fogNode[i].reqResource[j]>0){
                    im = 1;
                }
                if(fogNode[i].ns>0){
                    fm = 1;
                }
                fRU = fRU + (im*fm*fogNode[i].ru);
            }
        }
        fRU = fRU/fa;
        sfRU = sfRU + fRU;

        //Equation 8
        for(int i=0;i<fogNode.length;i++){
            fogNode[i].lb = Math.pow((fogNode[i].ru - fRU), 2);
        }

        //Equation 9
        for(int i=0;i<fogNode.length;i++){
            for(int j=0;j<fogNode[i].reqResource.length;j++){
                int im = 0;
                int fm = 0;
                if(fogNode[i].ns>0){
                    fm = 1;
                }
                if(fogNode[i].reqResource[j]>0){
                    im = 1;
                }
                fLB = fLB + (im*fm*fogNode[i].lb);
            }
        }
        fLB = fLB/fa;
        sfLB = sfLB + fLB;
        fLB = 0;
        fRU = 0;


        //Rfassigning 0
        for(int i=0;i<fogNode.length;i++){
            fogNode[i].lb = 0;
            fogNode[i].ns = 0;
            fogNode[i].ru = 0;
        }
    }
    public static void cloudCalc(){
        rcloud++;
        //Equation 2
        for(int i=0;i<cloud.length;i++){
            cloud[i].ru = cloud[i].usedCapacity/7;
        }

        //Equation 3
        for(int i=0;i<cloud.length;i++){
            for(int j=0;j<cloud[i].reqResource.length;j++){
                if(cloud[i].reqResource[j] > 0){
                    cloud[i].ns ++;
                }
            }
        }

        //Equation 4
        for(int i=0;i<cloud.length;i++){
            int fm = 0;
            if(cloud[i].ns>0){
                fm = 1;
            }
            ca = ca + (fm);
        }

        //Equation 7
        for(int i=0; i<cloud.length;i++){
            for(int j=0;j<cloud[i].reqResource.length;j++){
                int fm = 0;
                int im = 0;
                if(cloud[i].reqResource[j]>0){
                    im = 1;
                }
                if(cloud[i].ns>0){
                    fm = 1;
                }
                cRU = cRU + (im*fm*cloud[i].ru);
            }
        }
        cRU = cRU/fa;
        scRU = scRU + cRU;

        //Equation 8
        for(int i=0;i<cloud.length;i++){
            cloud[i].lb = Math.pow((cloud[i].ru - cRU), 2);
        }

        //Equation 9
        for(int i=0;i<cloud.length;i++){
            for(int j=0;j<cloud[i].reqResource.length;j++){
                int im = 0;
                int fm = 0;
                if(cloud[i].ns>0){
                    fm = 1;
                }
                if(cloud[i].reqResource[j]>0){
                    im = 1;
                }
                cLB = cLB + (im*fm*cloud[i].lb);
            }
        }
        cLB = cLB/fa;
        scLB = scLB + cLB;
        cLB = 0;
        cRU = 0;


        //Rfassigning 0
        for(int i=0;i<cloud.length;i++){
            cloud[i].lb = 0;
            cloud[i].ns = 0;
            cloud[i].ru = 0;
        }
    }


    public static void finalStatus(){
        System.out.println();
        /*for(int i=0;i<fogNode.length;i++){
            System.out.println("fogNode_" + i + " served " + fogNode[i].req.length + " total requests.");
        }
        for (int i=0;i<edge.length;i++){
            System.out.println("edgeNode_" + i + " served " + edge[i].req.length + " total requests.");
        }
        for (int i=0;i<cloud.length;i++){
            System.out.println("cloudNode_" + i + " served " + cloud[i].req.length + " total requests.");
        }*/

        System.out.println("Resource Utilisation");
        System.out.println("Edge RU: " + (seRU/redge)*100 + " Edge LB: " + (seLB/redge)*100);
        System.out.println("Fog RU: " + (sfRU/rfog)*100 + " Fog LB: " + (sfLB/rfog)*100);
        System.out.println("Cloud RU: " + (scRU/rcloud)*100 + " Cloud LB: " + (scLB/rcloud)*100);
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

    public static void fogAllocation(String request) throws Exception {
        double reqResource = getRequiredResources(request);
        int i;
        for (i = 0; i < fogNode.length; i++){
            if (fogNode[i].freeCapacity >= reqResource) {
                fogNode[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                fogCounter = fogCounter + (fogNode[i].usedCapacity/12)*100;
                System.out.println("Request_" + getReqId(request) + " is allocated to fogNode_" + i);
                break;
            }
        }

        if(i == fogNode.length){
            FOGNODE[] temp = new FOGNODE[fogNode.length+1];
            for(i=0;i<fogNode.length;i++){
                temp[i] = fogNode[i];
            }
            temp[i] = new FOGNODE();
            temp[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
            fogCounter = fogCounter + (temp[i].usedCapacity/12)*100;
            System.out.println("Request_" + getReqId(request) + " is allocated to fogNode_" + i);
            fogNode = temp;
        }
    }

    public static void cloudAllocation(String request)throws Exception{
        double reqResource = getRequiredResources(request);
        int i;
        for (i = 0; i < cloud.length; i++){
            if (cloud[i].freeCapacity >= reqResource) {
                cloud[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                cloudCounter = cloudCounter + (cloud[i].usedCapacity/18)*100;
                System.out.println("Request_" + getReqId(request) + " is allocated to cloudNode_" + i);
                break;
            }
        }

        if(i == cloud.length){
            CLOUD[] temp = new CLOUD[cloud.length+1];
            for(i=0;i<cloud.length;i++){
                temp[i] = cloud[i];
            }
            temp[i] = new CLOUD();
            temp[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
            cloudCounter = cloudCounter + (temp[i].usedCapacity/18)*100;
            System.out.println("Request_" + getReqId(request) + " is allocated to cloudNode_" + i);
            cloud = temp;
        }
    }

    public static void edgeAllocation(String request)throws Exception{
        double reqResource = getRequiredResources(request);
        int i;
        for (i = 0; i < edge.length; i++){
            if (edge[i].freeCapacity >= reqResource) {
                edge[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
                edgeCounter = edgeCounter + (edge[i].usedCapacity/7)*100;
                System.out.println("Request_" + getReqId(request) + " is allocated to edgeNode_" + i);
                break;
            }
        }

        if(i == edge.length){
            EDGE[] temp = new EDGE[edge.length+1];
            for(i=0;i<edge.length;i++){
                temp[i] = edge[i];
            }
            temp[i] = new EDGE();
            temp[i].setReq(getReqId(request), getStartTime(request), getTimeDuration(request), getRequiredResources(request));
            edgeCounter = edgeCounter + (temp[i].usedCapacity/7)*100;
            System.out.println("Request_" + getReqId(request) + " is allocated to edgeNode_" + i);
            edge = temp;
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
}
