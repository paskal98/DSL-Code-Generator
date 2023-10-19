package fei.tuke.sk.stmlang;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class Generator {
    private final StateMachineDefinition stateMachine;
    private final Writer writer;

    public Generator(StateMachineDefinition stateMachine, Writer writer) {
        this.stateMachine = stateMachine;
        this.writer = writer;
    }

    public void generate() throws IOException {
        writer.write("#include \"common.h\"\n\n");

        //write headers function names
        for (Map.Entry<String, StateDefinition> entry : stateMachine.getStates().entrySet()) {
            String stateName = entry.getKey();
            writer.write("void state_" + stateName + "();\n");
        }

        //write function state
        for (Map.Entry<String, StateDefinition> entry : stateMachine.getStates().entrySet()) {
            String stateName = entry.getKey();
            StateDefinition state = entry.getValue();

            writeState(stateName, state);
        }

        //write main for start state
        writer.write("void main() {\n");
        writer.write("\tstate_" + stateMachine.getInitialStateName() + "();\n");
        writer.write("}\n");
    }

    private void writeState(String name, StateDefinition state) throws IOException {
        //state name
        writer.write("void state_" + name + "() {\n");

        //setup an event if exists
        for (String action : state.getActions()) {
            writer.write("\tsend_event('" + stateMachine.getEventSymbol(action) + "');\n");
        }

        //main dsl switcher sate (transition, commands)
        //pre write of seitch block
        writer.write("\tchar ev;\n");
        writer.write("\twhile (ev = read_command()) {\n");
        writer.write("\t\tswitch (ev) {\n");

        //case state for transitions
        for (TransitionDefinition transition : state.getTransitions()) {
            writer.write("\t\t\tcase '" + stateMachine.getCommandSymbol(transition.commandName()) + "':\n");
            writer.write("\t\t\t\treturn state_" + transition.targetName() + "();\n");
        }

        //case state for comnmands
        for (String resetCommand : stateMachine.getResetCommands()) {
            writer.write("\t\t\tcase '" + stateMachine.getCommandSymbol(resetCommand) + "':\n");
            writer.write("\t\t\t\treturn state_" + stateMachine.getInitialStateName() + "();\n");
        }

        writer.write("\t\t}\n");
        writer.write("\t}\n");
        writer.write("}\n\n");
    }
}