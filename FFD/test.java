package FFD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class test {
    public static void main(String[] args) throws Exception{
        File file = new File("C:\\Users\\Radhey\\OneDrive\\Desktop\\newData500.txt");
        String[] tempSubset = subset(file);
        String[][] subset = new String[tempSubset.length][];
        for(int i=0;i<tempSubset.length;i++){
            subset[i] = tempSubset[i].split("-");
        }
        for(int i=0;i<subset.length;i++){
            subset[i] = dec(subset[i]);
        }

        for(int i=0;i<subset.length;i++){
            for(int j=0;j<subset[i].length;j++){
                System.out.print(subset[i][j] + " -- ");
            }
            System.out.println();
        }

    }

    public static String[] dec(String str[]){

        double idRes[][] = new double[str.length][2];
        for(int i=0;i<str.length;i++){
            idRes[i][0] = getReqId(str[i]);
            idRes[i][1] = getRequiredResources(str[i]);
        }

        for(int i=0;i<idRes.length;i++){
            for(int j=0;j<idRes.length-1-i;j++){
                if(idRes[j][1] < idRes[j+1][1]){
                    double temp = idRes[j][0];
                    idRes[j][0] = idRes[j+1][0];
                    idRes[j+1][0] = temp;

                    temp = idRes[j][1];
                    idRes[j][1] = idRes[j+1][1];
                    idRes[j+1][1] = temp;
                }
            }
        }
        String string = "";
        for(int i=0;i<idRes.length;i++){
            for(int j=0;j<str.length;j++){
                if(getReqId(str[j]) == idRes[i][0]){
                    string = string + str[j] + " - ";
                }
            }
        }
        return string.split("-");

    }

    public static String[] subset(File file)throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String request = br.readLine();
        String[] result = new String[1];
        result[0] = "";
        int counter = 0;
        double st = getStartTime(request);
        result[counter] = result[counter] + request +"-";
        request = br.readLine();
        try{
            while(request!=null){
                if(st == getStartTime(request)){
                    result[counter] = result[counter] + request + "-";
                }
                else{
                    String[] temp = new String[result.length+1];
                    int i;
                    for(i=0;i<result.length;i++){
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
        }catch (Exception e){
            ;
        }
        return result;
    }
    public static double getRequiredResources(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[2]);
    }
    public static double getStartTime(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[3]);
    }
    public static double getNodeType(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[1]);
    }
    public static double getReqId(String request){
        String temp[] = request.split(",");
        return Double.parseDouble(temp[0]);
    }
}
