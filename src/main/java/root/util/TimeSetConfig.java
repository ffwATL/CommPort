package root.util;



public class TimeSetConfig {

    private String displayName;
    private int value;

    public TimeSetConfig(String displayName, int value){
        this.displayName = displayName;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString(){
        return this.displayName;
    }
}
