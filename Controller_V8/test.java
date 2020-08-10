package Controller_V8;

class test{
    public static void main(String[] args) throws Exception{
        double s1 = System.currentTimeMillis();
        Thread.sleep(1);
        double s2 = System.currentTimeMillis();
        System.out.println((s2-s1));
    }
}