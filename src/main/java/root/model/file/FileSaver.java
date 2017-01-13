package root.model.file;


import java.io.File;

public interface FileSaver<T> {

    boolean writeToFile(File file, Message<T> message);

}