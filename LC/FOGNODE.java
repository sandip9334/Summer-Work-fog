package LC;

import java.util.ArrayList;

public class FOGNODE {
    double id,Capacity,RAM,CPU,AW;
    int ns;
    boolean flag;
    double usedRam,freeRam,usedCapacity,freeCapacity,usedCPU,freeCPU;
    double req[];
    double reqRam[],reqCapacity[],reqCPU[];
    double startTime[];
    //Removed the array of bufferStartTime
    double endTime[];
    ArrayList<String> BufferReq = new ArrayList<String>();
    String Status;

    FOGNODE(double id,double Capacity, double RAM, double CPU, double AW, String Status){
        this.id = id;
        this.Capacity = Capacity;
        this.RAM = RAM;
        this.CPU = CPU;
        this.AW = AW;
        this.ns = 0;
        this.Status = Status;

        startTime = new double[0];
        endTime = new double[0];
        req = new double[0];
        reqRam = new double[0];
        reqCapacity = new double[0];
        reqCPU = new double[0];
        freeRam = RAM;
        usedRam = 0;
        freeCapacity = Capacity;
        usedCapacity = 0;
        freeCPU = CPU;
        usedCPU = 0;
    }

    public void setBuffer(String req){
        BufferReq.add(req);
    }

    public void setReq(double reqId, double startTime, double duration, double reqRam, double reqCapacity, double reqCPU)throws Exception {
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

        double tempEndTime[] = new double[this.endTime.length+1];
        for(int i=0;i<this.endTime.length;i++){
            tempEndTime[i] = this.endTime[i];
        }
        tempEndTime[this.endTime.length] = startTime + duration;
        this.endTime = tempEndTime;

        double tempReqRam[] = new double[this.reqRam.length+1];
        for(int i=0;i<this.reqRam.length;i++){
            tempReqRam[i] = this.reqRam[i];
        }
        tempReqRam[this.reqRam.length] = reqRam;
        this.reqRam = tempReqRam;

        this.usedRam = this.usedRam + reqRam;
        this.freeRam = this.RAM - this.usedRam;

        double tempReqCapacity[] = new double[this.reqCapacity.length+1];
        for(int i=0;i<this.reqCapacity.length;i++){
            tempReqCapacity[i] = this.reqCapacity[i];
        }
        tempReqCapacity[this.reqCapacity.length] = reqCapacity;
        this.reqCapacity = tempReqCapacity;

        this.usedCapacity = this.usedCapacity + reqCapacity;
        this.freeCapacity = this.Capacity - this.usedCapacity;

        double tempReqCPU[] = new double[this.reqCPU.length+1];
        for(int i=0;i<this.reqCPU.length;i++){
            tempReqCPU[i] = this.reqCPU[i];
        }
        tempReqCPU[this.reqCPU.length] = freeCPU*(reqCPU/100);
        this.reqCPU = tempReqCPU;

        this.usedCPU = this.usedCPU + freeCPU*(reqCPU/100);
        this.freeCPU = this.CPU - this.usedCPU;
    }
}
