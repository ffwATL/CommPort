package root.model.file;


public interface Message<T> {

    T getMessage();

    void setMessage(T message);

}