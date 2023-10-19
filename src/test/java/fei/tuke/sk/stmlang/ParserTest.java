package fei.tuke.sk.stmlang;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {


    @Test
    void testMinimalStateMachine() {
        String input = "commands {} resetCommands {} events {} state idle {}";
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        StateMachineDefinition stateMachine = parser.stateMachine();

        assertTrue(stateMachine.getCommands().isEmpty());
        assertTrue(stateMachine.getResetCommands().isEmpty());
        assertTrue(stateMachine.getEvents().isEmpty());
        assertEquals(1, stateMachine.getStates().size());
    }

    @Test
    void testCommandsEventsAndResetCommands() {
        String input = """
                commands {
                    doorClosed   'd'
                }
                resetCommands {
                    doorOpened
                }
                events {
                    unlockPanel 'U'
                }
                state idle {}
                """;
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        StateMachineDefinition stateMachine = parser.stateMachine();

        assertEquals(1, stateMachine.getCommands().size());
        assertEquals(Character.valueOf('d'), stateMachine.getCommands().get("doorClosed"));
        assertEquals(1, stateMachine.getResetCommands().size());
        assertTrue(stateMachine.getResetCommands().contains("doorOpened"));
        assertEquals(1, stateMachine.getEvents().size());
        assertEquals(Character.valueOf('U'), stateMachine.getEvents().get("unlockPanel"));
    }

    @Test
    void testStateWithActionsAndTransitions() {
        String input = """
                commands {
                    doorClosed   'd'
                }
                resetCommands {}
                events {}
                state idle {
                    actions {unlockDoor lockPanel}
                    doorClosed => active
                }
                """;

        input=BracesFormater.format(input);

        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        StateMachineDefinition stateMachine = parser.stateMachine();

        StateDefinition state = stateMachine.getStates().get("idle");
        assertNotNull(state);
        assertEquals(2, state.getActions().size());
        assertTrue(state.getActions().contains("unlockDoor"));
        assertTrue(state.getActions().contains("lockPanel"));
        assertEquals(1, state.getTransitions().size());
        assertEquals("active", state.getTransitions().get(0).targetName());
        assertEquals("doorClosed", state.getTransitions().get(0).commandName());
    }

    @Test
    void testStateWithActionsAndTransitionsException() {
        String input = """
                commands {
                    doorClosed   'd'
                }
                resetCommands {}
                events {}
                state idle {
                    actions {unlockDoor lockPanel
                    doorClosed => active
                }
                """;

        input=BracesFormater.format(input);

        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        assertThrows(IllegalStateException.class,parser::stateMachine);
    }

}