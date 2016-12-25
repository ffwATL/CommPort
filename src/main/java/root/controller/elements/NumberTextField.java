package root.controller.elements;

import javafx.scene.control.TextField;


public class NumberTextField extends TextField {

    public NumberTextField(){

    }

    @Override
    public void replaceText(int start, int end, String text) {
        if(text.matches("[0-9]") || text.isEmpty()) {
            super.replaceText(start, end, text);
        }
    }
}
