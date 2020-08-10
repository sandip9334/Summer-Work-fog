package Controller_V8;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.GregorianCalendar;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception{
        int cloudCounter = 0;
        double cloudUtilisation[][] = new double[1][2];
        FOGNODE fogNode[] = new FOGNODE[3];
        fogNode[0] = new FOGNODE(0,2048,0,0);
        fogNode[1] = new FOGNODE(1,1024,5,5);
        fogNode[2] = new FOGNODE(2,512,10,10);

        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Radhey\\OneDrive\\Desktop\\newData1000.txt"));
        double systemStartTime = System.currentTimeMillis();
        String request = br.readLine();
        double duration = 0;
        double startTime = getStartTime(request);
        while(true){
            double intermediate = System.currentTimeMillis();
            deAllocation(fogNode, systemStartTime);
            intermediate = System.currentTimeMillis() - intermediate;
            while((System.currentTimeMillis() - systemStartTime - duration - intermediate)/1000 < startTime){
                ;
            }
            duration = System.currentTimeMillis();
            double reqResource = getRequiredResources(request);
            int i;
            for(i=0;i<fogNode.length;i++){
                if(fogNode[i].freeRam >= reqResource){
                    fogNode[i].setReq(getReqId(request), startTime, getTimeDuration(request), getRequiredResources(request));
                    System.out.println("Request_" + getReqId(request) + " is allocated to fogNode_" + i);
                    break;
                }
            }
            duration = System.currentTimeMillis() - duration;
            if(i == fogNode.length){
                System.out.println("Request_"+ getReqId(request) + " is allocated to Cloud");
                cloudCounter ++;
            }
            request = br.readLine();
            if(request == null){
                break;
            }
            startTime = getStartTime(request);
        }

        System.out.println((System.currentTimeMillis()-systemStartTime)/1000);
        finalStatus(fogNode, cloudCounter);
    }

    public static void finalStatus(FOGNODE fognode[], int cloudCounter){
        System.out.println();
        for(int i=0;i<fognode.length;i++){
            System.out.println("fogNode_" + i + " served " + fognode[i].req.length + " total requests.");
        }
        System.out.println("Cloud served " + cloudCounter + " total requests.");
    }

    public static double getReqId(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[0]);
    }

    public static double getRequiredResources(String request){
        String temp[] = request.split(",");
        double unit = (double) new Random().nextInt(26) + 25; // Between 25 and 50
        return Double.parseDouble(temp[1])*unit;
    }

    public static double getStartTime(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[2]);
    }

    public static double getTimeDuration(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[3]);
    }

    public static void deAllocation(FOGNODE fognode[], double systemStartTime){
        for(int i=0;i<fognode.length;i++){
            for(int j=0;j<fognode[i].req.length;j++){
                if(fognode[i].endTime[j] <= (System.currentTimeMillis()-systemStartTime)/1000){
                    fognode[i].freeRam = fognode[i].freeRam + fognode[i].reqResource[j];
                    fognode[i].usedRam = fognode[i].RAM - fognode[i].freeRam;
                    fognode[i].reqResource[j] = 0;
                }
            }
        }
    }
}
