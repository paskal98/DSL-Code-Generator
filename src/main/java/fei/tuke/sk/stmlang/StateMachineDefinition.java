package fei.tuke.sk.stmlang;

import java.util.*;

public class StateMachineDefinition {
    private final Map<String, Character> events = new HashMap<>();
    private final Map<String, Character> commands = new HashMap<>();
    private final List<String> resetCommands = new ArrayList<>();
    private final Map<String, StateDefinition> states = new LinkedHashMap<>();
    private String initialStateName = null;

    public void addEvent(String name, char value) {
        events.put(name, value);
    }

    public void addCommand(String name, Character value) {
        commands.put(name, value);
    }

    public void addState(String name, StateDefinition state) {

        if(states.size()==0){
            initialStateName=name;
        }

       if(states.get(name) != null) {
            StateDefinition updateStateDefinition = states.get(name);
            updateStateDefinition.addTransition(state.getTransitions().get(0));
           states.remove(name);
           states.put(name,updateStateDefinition);
       }
       else
           states.put(name, state);
    }

    public void addResetCommands(String name) {
        resetCommands.add(name);
    }

    public String getInitialStateName() {
        return initialStateName;
    }

    public Map<String, Character> getEvents() {
        return events;
    }

    public Map<String, Character> getCommands() {
        return commands;
    }

    public List<String> getResetCommands() {
        return resetCommands;
    }

    public Map<String, StateDefinition> getStates() {
        return states;
    }

    public Character getEventSymbol(String eventName) {
        return events.get(eventName);
    }

    public Character getCommandSymbol(String commandName) {
        return commands.get(commandName);
    }
}