package fei.tuke.sk.stmlang;

import java.util.ArrayList;
import java.util.List;

public class StateDefinition {
    private final List<String> actions = new ArrayList<>();
    private final List<TransitionDefinition> transitions = new ArrayList<>();

    public void addAction(String name) {
        actions.add(name);
    }

    public void addTransition(TransitionDefinition transition) {
        transitions.add(transition);
    }

    public List<String> getActions() {
        return actions;
    }

    public List<TransitionDefinition> getTransitions() {
        return transitions;
    }
}
