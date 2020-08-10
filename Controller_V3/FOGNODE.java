package Controller_V3;

import java.util.Scanner;

public class FOGNODE {
    double RAM, freeRam, usedRam;
    double no_of_CPUs;
    double latitude, longitude;
    int fogid;

    public FOGNODE(int fogid){
        this.fogid = fogid;
        Scanner scanner = new Scanner(System.in);
        System.out.println("----------Configuring the FOG Node_"+fogid+"----------");
        System.out.print("Enter the RAM: ");
        RAM = scanner.nextDouble();
        System.out.print("Enter the total no. of CPUs: ");
        no_of_CPUs = scanner.nextDouble();
        System.out.print("Enter the Latitude: ");
        latitude = scanner.nextDouble();
        System.out.print("Enter the Longitude: ");
        longitude = scanner.nextDouble();
        freeRam = RAM;
        usedRam = 0;
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

    public double getNo_of_CPUs() {
        return no_of_CPUs;
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
}
