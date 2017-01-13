package root.response;


import root.controller.Graphics;

import java.util.Arrays;

public class AngleResponseHandler implements ResponseHandler {

    private final Graphics GUI;

    private int[] response;

    public AngleResponseHandler(Graphics gui){
        this.GUI = gui;
    }

    @Override
    public void handle(int[] response) {
        this.response = response;
        GUI.updateSecondTerminal(Arrays.toString(response));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AngleResponseHandler that = (AngleResponseHandler) o;

        if (GUI != null ? !GUI.equals(that.GUI) : that.GUI != null) return false;
        return Arrays.equals(response, that.response);

    }

    @Override
    public int hashCode() {
        int result = GUI != null ? GUI.hashCode() : 0;
        result = 31 * result + (response != null ? Arrays.hashCode(response) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AngleResponseHandler{" +
                ", response=" + Arrays.toString(response) +
                '}';
    }
}