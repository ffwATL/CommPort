package root.model;


public class Command {

    public static final int[] RESET_COMMAND_FIRST = {16, 0};
    public static final int[] RESET_COMMAND_SECOND = {16, 1};
    public static final int[] START_FREQ = {8};
    public static final int[] STOP_FREQ = {9};
    public static final int[] UP_COMMAND = {16, 1};
    public static final int[] DOWN_COMMAND = {16, 0};
    public static final int[] GET_COUNTER = {1};
    public static final int SET_IMPULSE_COUNT = 3;
    public static final int SET_FREQUENCY = 4;
}