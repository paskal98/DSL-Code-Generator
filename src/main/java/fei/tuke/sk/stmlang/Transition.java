package fei.tuke.sk.stmlang;

public class Transition {

    private final String trigger;
    private final String targetState;

    public Transition(String trigger, String targetState) {
        this.trigger = trigger;
        this.targetState = targetState;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getTargetState() {
        return targetState;
    }
}
