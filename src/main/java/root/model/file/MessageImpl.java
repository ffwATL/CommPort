package root.model.file;


public class MessageImpl<T> implements Message<T> {

    private T message;

    public MessageImpl(){}

    public MessageImpl(T message){
        this.message = message;
    }

    @Override
    public T getMessage() {
        return message;
    }

    @Override
    public void setMessage(T message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageImpl<?> message1 = (MessageImpl<?>) o;

        return !(getMessage() != null ? !getMessage().equals(message1.getMessage()) : message1.getMessage() != null);

    }

    @Override
    public int hashCode() {
        return getMessage() != null ? getMessage().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MessageImpl{" +
                "message=" + message +
                '}';
    }
}