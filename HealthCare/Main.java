package HealthCare;

import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static String generateRequest(){
        int heartBeat = new Random().nextInt(251); //The range of the heartbeat a human can possibly have medically.
        int category = new Random().nextInt(2);
        int latitude = new Random().nextInt(500);
        int longitude = new Random().nextInt(500);
        String request = category + "," + heartBeat + "," + latitude + "," + longitude;
        //0 For Aged (60-100 bpm)
        //1 For Child (60-100 bpm)
        return request;
    }

    public static int getLatitude(String request){
        String temp[] = request.split(",");
        int latitude = Integer.valueOf(temp[2]);
        return latitude;
    }

    public static int getLongitude(String request){
        String temp[] = request.split(",");
        int longitude = Integer.valueOf(temp[3]);
        return longitude;
    }

    public static int getCategory(String request){
        String temp[] = request.split(",");
        int category = Integer.valueOf(temp[0]);
        return category;
    }

    public static int getHeartBeat(String request){
        String temp[] = request.split(",");
        int heartBeat = Integer.valueOf(temp[1]);
        return heartBeat;
    }

    public static String getCondition(String request){
        int category = getCategory(request);
        int heartBeat = getHeartBeat(request);

        if(category == 0){
            if(heartBeat>=60 && heartBeat<=100){
                return "Normal";
            }
            else {
                return "Critical";
            }
        }
        else{
            if(heartBeat>=70 && heartBeat<=100){
                return "Normal";
            }
            else{
                return "Critical";
            }
        }
    }

    public static void resourcesDeAllocation(FOGNODE fog[]){
        for(int i=0;i<fog.length;i++){
            int reqCounter = fog[i].reqCounter;
            for(int j=0;j<=reqCounter;j++) {
                if (fog[i].endTime[j] != null && fog[i].getEndTime(j).compareTo(new GregorianCalendar()) <= 0) {
                    fog[i].reqCounter--;
                    fog[i].startTime[j] = null;
                    fog[i].endTime[j] = null;
                }
            }
        }
    }

    public static void displayResults(FOGNODE fog[], int cloudCounter){
        System.out.println();
        for(int i=0;i<fog.length;i++){
            System.out.println("The total request served by fogNode_" + i + ": " + fog[i].totalRequest);
        }
        System.out.println("The total request served by Cloud is: " + cloudCounter);
    }

    public static void main(String[] args) throws Exception{
        System.out.println("Enter the total requests: ");
        int totalRequest = new Scanner(System.in).nextInt();
        int cloudCounter = 0;

        FOGNODE fogNode[] = new FOGNODE[4];
        fogNode[0] = new FOGNODE(0,0,0);
        fogNode[1] = new FOGNODE(1,100,100);
        fogNode[2] = new FOGNODE(2,200,200);
        fogNode[3] = new FOGNODE(3,300,300);

        for(int i=0;i<totalRequest;i++){
            resourcesDeAllocation(fogNode);
            String request = generateRequest();
            String condition = getCondition(request);
            int k;
            boolean flag = false;
            for(k = 0;k<fogNode.length;k++) {
                if(condition == "Critical"){
                    cloudCounter++;
                    System.out.println("The request_" + i + " has been allocated to cloud...[" + condition + "]");
                    break;
                }
                else {
                    flag = fogNode[k].setReq(condition);
                    if(flag){
                        System.out.println("The request_" + i + " has been allocated to fogNode_" + k + "...[" + condition + "]" );
                        break;
                    }
                }
            }
            if(k == fogNode.length) {
                cloudCounter++;
                System.out.println("The request_" + i + " has been allocated to cloud...[" + condition + "]");
            }
            Thread.sleep(300);
        }
        displayResults(fogNode, cloudCounter);
    }
}

