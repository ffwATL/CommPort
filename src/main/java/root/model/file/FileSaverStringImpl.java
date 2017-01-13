package root.model.file;

import java.io.*;


public class FileSaverStringImpl implements FileSaver<String> {


    @Override
    public boolean writeToFile(File file, Message<String> message) {
        try(FileWriter writer = new FileWriter(file, true)) {

            writer.append(message.getMessage()).append("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}