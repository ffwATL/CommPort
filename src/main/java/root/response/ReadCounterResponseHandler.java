package root.response;


import root.controller.Graphics;

import java.util.Arrays;

public class ReadCounterResponseHandler implements ResponseHandler{

    private final Graphics GUI;

    private int[] response;

    public ReadCounterResponseHandler(Graphics gui){
        this.GUI = gui;
    }

    @Override
    public void handle(int[] response) {
        this.response = response;

        int r = response[2] * 256 + response[1];

        GUI.updateTerminal(String.valueOf(r));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReadCounterResponseHandler that = (ReadCounterResponseHandler) o;

        return Arrays.equals(response, that.response);

    }

    @Override
    public int hashCode() {
        return response != null ? Arrays.hashCode(response) : 0;
    }

    @Override
    public String toString() {
        return "ReadCounterResponseHandler{" +
                "response=" + Arrays.toString(response) +
                " hashcode: "+ hashCode()+
                '}';
    }
}