package exceptions;

public class HeaderDeserializationException extends Exception {

    public HeaderDeserializationException() {
        super("Illegal header interpretation");
    }

}
