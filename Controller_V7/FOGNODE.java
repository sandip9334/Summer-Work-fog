package Controller_V7;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FOGNODE {
    double RAM, freeRam, usedRam;
    double latitude, longitude;
    int fogid;
    int type[];
    int req[];
    boolean flag[];
    Calendar startTime[];
    Calendar endTime[];
    public FOGNODE(int fogid, double RAM, double latitude, double longitude){
        this.fogid = fogid;
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("----------Configuring the FOG Node_"+fogid+"----------");
        System.out.print("Enter the RAM: ");
        RAM = scanner.nextDouble();
        System.out.print("Enter the Latitude: ");
        latitude = scanner.nextDouble();
        System.out.print("Enter the Longitude: ");
        longitude = scanner.nextDouble();*/
        this.RAM = RAM;
        this.latitude = latitude;
        this.longitude = longitude;
        freeRam = RAM;
        usedRam = 0;
        type = new int[0];
        startTime = new GregorianCalendar[0];
        endTime = new GregorianCalendar[0];
        req = new int[0];
        flag = new boolean[0];
    }


    public double getRAM() {
        return RAM;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getFogid() {
        return fogid;
    }

    public void setRam(double RAM) {
        this.RAM = RAM;
    }

    public double getFreeRam() {
        return freeRam;
    }

    public void setFreeRam(double freeRam){
        this.freeRam = freeRam;
    }

    public void setUsedRam(double usedRam) {
        this.usedRam = usedRam;
    }

    public void setReq(int reqId, int type, int fogid, String request)throws Exception {
        Calendar tempst[] = new GregorianCalendar[startTime.length + 1];
        Calendar tempet[] = new GregorianCalendar[endTime.length + 1];
        int counter;
        for (counter = 0; counter < startTime.length; counter++) {
            tempst[counter] = startTime[counter];
        }
        tempst[counter] = new GregorianCalendar();
        startTime = tempst;

        for (counter = 0; counter < endTime.length; counter++) {
            tempet[counter] = endTime[counter];
        }
        tempet[tempet.length-1] = startTime[startTime.length-1];
        int t = startTime[startTime.length-1].get(Calendar.SECOND);
        //System.out.println("iST--" + startTime[startTime.length-1].getTime());
        /* ----------------------------------------------------------------------------------*/
        if(type == 1){
            tempet[tempet.length-1].set(Calendar.SECOND, t + 5);
        }
        else if(type == 2){
            tempet[tempet.length-1].set(Calendar.SECOND, t + 3);
        }
        else if(type == 3){
            tempet[tempet.length-1].set(Calendar.SECOND, t + 1);
        }

        /* ----------------------------------------------------------------------------------*/
        endTime = tempet;
        //System.out.println("ST--" + startTime[startTime.length-1].getTime());
        //System.out.println("ET--" + endTime[endTime.length-1].getTime());


        int tempreq[] = new int[req.length+1];
        for(counter=0;counter<req.length;counter++){
            tempreq[counter] = req[counter];
        }
        tempreq[counter] = reqId;
        req = tempreq;


        boolean tempflag[] = new boolean[flag.length+1];
        for(counter=0;counter<flag.length;counter++){
            tempflag[counter] = flag[counter];
        }
        tempflag[counter] = true;
        flag = tempflag;


        int temptype[] = new int[this.type.length+1];
        for(counter=0;counter<this.type.length;counter++){
            temptype[counter] = this.type[counter];
        }
        temptype[counter] = type;
        this.type = temptype;

        //System.out.println("The Request_"+reqId+" is assigned to FOGNODE_"+fogid +  " [" + request + "] ");
    }

    public static void display(FOGNODE fog[]){
        for(int i=0;i<fog.length;i++){
            System.out.println("FOGNODE_" + i);
            for(int j=0;j<fog[i].req.length;j++){
                System.out.println(fog[i].getStartTime(j) + "---------" + fog[i].getEndTime(j));
            }
        }
    }

    public Calendar getEndTime(int reqId){
        return endTime[reqId];
    }

    public Calendar getStartTime(int reqId){
        return startTime[reqId];
    }

    public void setFlag(int reqId){
        flag[reqId] = false;
    }

    public int getTotalReq() {
        return req.length;
    }
}
