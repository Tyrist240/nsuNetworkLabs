package exceptions;

public class HeaderSerializationException extends Exception {

    public HeaderSerializationException() {
        super("Illegal values in header");
    }

}
