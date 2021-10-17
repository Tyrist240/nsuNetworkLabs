package domain;

public enum ExecutionStatus {

    SUCCESS("Success", 1),
    READY_FOR_RECEIVE("Ready", 2),
    CANCELLED("Failed", 3);

    private String stringValue;

    private int ordinal;

    ExecutionStatus(String value, int ordinal) {
        this.stringValue = value;
        this.ordinal = ordinal;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public static ExecutionStatus getByOrdinal(int ordinal) {
        for (ExecutionStatus status : ExecutionStatus.values()) {
            if (status.ordinal == ordinal) {
                return status;
            }
        }

        return null;
    }

}
