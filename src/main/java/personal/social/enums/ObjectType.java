package personal.social.enums;

public enum ObjectType {
    POST, COMMENT, REPLY;

    @Override
    public String toString() {
        return name();
    }
}
