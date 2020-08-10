package Controller_V8;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FOGNODE {
    double RAM, freeRam, usedRam;
    double latitude, longitude;
    int fogid;
    double req[];
    double reqResource[];
    double startTime[];
    double endTime[];
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
        reqResource = new double[0];
        freeRam = RAM;
        usedRam = 0;
        startTime = new double[0];
        endTime = new double[0];
        req = new double[0];
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

    public void setReq(double reqId, double startTime, double duration, double reqResource)throws Exception {
        double tempId[] = new double[this.req.length+1];
        for(int i=0;i<req.length;i++){
            tempId[i] = req[i];
        }
        tempId[req.length] = reqId;
        req = tempId;


        double tempStartTime[] = new double[this.startTime.length+1];
        for(int i=0;i<this.startTime.length;i++){
            tempStartTime[i] = this.startTime[i];
        }
        tempStartTime[this.startTime.length] = startTime;
        this.startTime = tempStartTime;

        double tempReqResource[] = new double[this.reqResource.length+1];
        for(int i=0;i<this.reqResource.length;i++){
            tempReqResource[i] = this.reqResource[i];
        }
        tempReqResource[this.reqResource.length] = reqResource;
        this.reqResource = tempReqResource;

        double tempEndTime[] = new double[this.endTime.length+1];
        for(int i=0;i<this.endTime.length;i++){
            tempEndTime[i] = this.endTime[i];
        }
        tempEndTime[this.endTime.length] = startTime + duration;
        this.endTime = tempEndTime;

        this.usedRam = this.usedRam + reqResource;
        this.freeRam = this.RAM - this.usedRam;
    }

}
