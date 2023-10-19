package fei.tuke.sk.stmlang;

public class Event {

    private final String name;
    private final char code;

    public Event(String name, char code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public char getCode() {
        return code;
    }

}
