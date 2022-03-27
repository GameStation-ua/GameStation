package webpage.exceptions;

public class LoadingExc  extends Exception{
    private String msg;
    public LoadingExc(String s) {
        super(s);
    }
}