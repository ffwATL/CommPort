package root.response;


import root.controller.Graphics;

public class ResponseHandlerFactory {

    private ResponseHandlerFactory(){}

    public static ResponseHandler getInstance(ResponseHandlerType handlerType, Graphics gui){
        switch (handlerType){
            case COUNTER_HANDLER: return new CounterResponseHandler(gui);
            case ANGLE_HANDLER: return new AngleResponseHandler(gui);
            default: throw new IllegalArgumentException();
        }
    }
}