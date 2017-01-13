package root.response;


import root.controller.Graphics;

import java.util.Arrays;

public class CounterResponseHandler implements ResponseHandler{

    private final Graphics GUI;

    private int[] response;

    public CounterResponseHandler(Graphics gui){
        this.GUI = gui;
    }

    @Override
    public void handle(int[] response) {
        this.response = response;

        int r = response[2] * 256 + response[1];

        GUI.updateFirstTerminal(String.valueOf(r));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CounterResponseHandler that = (CounterResponseHandler) o;

        return Arrays.equals(response, that.response);

    }

    @Override
    public int hashCode() {
        return response != null ? Arrays.hashCode(response) : 0;
    }

    @Override
    public String toString() {
        return "CounterResponseHandler{" +
                "response=" + Arrays.toString(response) +
                " hashcode: "+ hashCode()+
                '}';
    }
}