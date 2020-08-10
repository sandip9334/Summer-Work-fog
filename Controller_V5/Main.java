package Controller_V5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception{
        int cloudCounter = 0;
        Scanner scanner = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Radhey\\OneDrive\\Desktop\\Data.txt"));
        System.out.println("Enter the total FOG NODE: ");
        int total_fog_node = scanner.nextInt();
        System.out.println("----------Configuring the FOG Nodes----------");
        FOGNODE fogNode[] = new FOGNODE[total_fog_node];
        for(int i=0;i<fogNode.length;i++){
            fogNode[i] = new FOGNODE(i);
        }
        System.out.println("Enter the Requests in the form of [Type,Latitude,Longitude]");
        System.out.println("Type is as follows:\n1. Video\n2. Audio\n3.Text\n");
        scanner.nextLine();
        String request = br.readLine();
        for(int i=0;request!=null;i++){
            ResourcesDeAllocation(fogNode);
            if(!RequestAllocationInFOGNODE(request,i, fogNode)){
                System.out.println("The Request_"+i+" is handed over to Cloud... [" + request + "]");
                cloudCounter++;
            }
            request = br.readLine();
            Thread.sleep(300);
        }
        System.out.println("\n\nSIMULATION COMPLETED!!!");
        finalStatus(fogNode);
        //FOGNODE.display(fogNode);
        System.out.println("The total request handed over to the cloud is " + cloudCounter);
    }

    //Still in developing Phase
    public static void ResourcesDeAllocation(FOGNODE fog[]){
        for(int i=0;i<fog.length;i++){
            for(int j=0;j<fog[i].req.length;j++){
                if(fog[i].getEndTime(j).compareTo(new GregorianCalendar()) < 0 && fog[i].flag[j]){
                    fog[i].flag[j] = false;
                    switch (fog[i].type[j]){
                        case 1:
                            fog[i].setFreeRam(fog[i].RAM - (fog[i].usedRam - 512));
                            fog[i].setUsedRam(fog[i].RAM - fog[i].getFreeRam());
                            break;
                        case 2:
                            fog[i].setFreeRam(fog[i].RAM - (fog[i].usedRam - 256));
                            fog[i].setUsedRam(fog[i].RAM - fog[i].getFreeRam());
                            break;
                        case 3:
                            fog[i].setFreeRam(fog[i].RAM - (fog[i].usedRam - 128));
                            fog[i].setUsedRam(fog[i].RAM - fog[i].getFreeRam());
                            break;
                    }
                }
            }
        }
    }

    public static double distanceBetweenFogAndRequest(double x1, double y1, double x2, double y2){
        double result = Math.pow((Math.pow((x2-x1),2) + Math.pow((y2-y1),2)), 0.5);
        return result;
    }

    public static void finalStatus(FOGNODE fog[]){
        for(int i=0;i<fog.length;i++){
            System.out.println("The total request handed over to fognode_" + i + " is " + fog[i].getTotalReq());
        }
    }

    public static double[][] arrangeAccordingToDistance(FOGNODE fog[], String request){
        double latitude = getLatitude(request);
        double longitude = getLongitude(request);
        double dist[][] = new double[fog.length][3];
        for(int i=0;i<fog.length;i++){
            dist[i][0] = distanceBetweenFogAndRequest(latitude,longitude,fog[i].latitude,fog[i].longitude);
            dist[i][1] = i;
            dist[i][2] = fog[i].freeRam;
        }
        for(int i=0;i<fog.length;i++){
            for(int j=0;j<fog.length-1-i;j++){
                if(dist[j][0] > dist[j+1][0]){
                    double td = dist[j][0];
                    double ti = dist[j][1];
                    double tfr = dist[j][2];

                    dist[j][0] = dist[j+1][0];
                    dist[j+1][0] = td;

                    dist[j][1] = dist[j+1][1];
                    dist[j+1][1] = ti;

                    dist[j][2] = dist[j+1][2];
                    dist[j+1][2] = tfr;
                }
                else if(dist[j][0] == dist[j+1][0]){
                    if(dist[j][2] < dist[j+1][2]){
                        double ti = dist[j][1];
                        dist[j][1] = dist[j+1][1];
                        dist[j+1][1] = ti;

                        double tfr = dist[j][2];
                        dist[j][2] = dist[j+1][2];
                        dist[j+1][2] = tfr;
                    }
                }
            }
        }
        return dist;
    }

    public static boolean RequestAllocationInFOGNODE(String request, int requestId, FOGNODE fog[])throws Exception{
        double reqResources = getRequiredResources(request);
        double distance[][] = arrangeAccordingToDistance(fog, request);
        int i;
        for(i=0;i<distance.length;i++){
            if(fog[(int)distance[i][1]].freeRam >= reqResources ){
                fog[(int)distance[i][1]].setFreeRam(fog[(int)distance[i][1]].RAM - fog[(int)distance[i][1]].usedRam - reqResources);
                fog[(int)distance[i][1]].setUsedRam(fog[(int)distance[i][1]].usedRam + reqResources);
                fog[(int)distance[i][1]].setReq(requestId, (int)getType(request), (int)distance[i][1], request);
                System.out.println("The Request_"+requestId+" is assigned to FOGNODE_"+(int)distance[i][1] +  " [" + request + "]");
                break;
            }
        }
        if(i == distance.length){
            return false;
        }
        else{
            return true;
        }
    }


    public static double getRequiredResources(String request){
        double type = getType(request);
        if(type == 1){          //Video
            return 512;         //5 secs
        }
        else if(type == 2){     //Audio
            return 256;         //3 secs
        }
        else if(type == 3){     //Text
            return 128;         //1 sec
        }
        else{
            return 0;
        }
    }

    public static double getLatitude(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[1]);
    }

    public static double getLongitude(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[2]);
    }

    public static double getType(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[0]);
    }
}
