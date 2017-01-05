package root.response;


import root.controller.Graphics;

public class AngleResponseHandler implements ResponseHandler {

    private final Graphics GUI;

    private int[] response;

    public AngleResponseHandler(Graphics gui){
        this.GUI = gui;
    }

    @Override
    public void handle(int[] response) {

    }
}
