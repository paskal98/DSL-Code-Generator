package fei.tuke.sk.stmlang;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest {

    @Test
    void testGenerate() throws IOException {
        String input = """
            commands {
                doorClosed   'd'
                drawerOpened 'w'
                lightOn      'l'
                doorOpened   'o'
                panelClosed  'p'
            }

            resetCommands {
                doorOpened
            }

            events {
                unlockPanel 'U'
                lockPanel   'L'
                lockDoor    'C'
                unlockDoor  'D'
            }

            state idle {
                actions {unlockDoor lockPanel}
                doorClosed => active
            }

            state active {
                drawerOpened => waitingForLight
                lightOn => waitingForDrawer
            }

            state waitingForLight {
                lightOn => unlockedPanel
            }

            state waitingForDrawer {
                drawerOpened => unlockedPanel
            }

            state unlockedPanel {
                actions {unlockPanel lockDoor}
                panelClosed => idle
            }
            """;

        input=BracesFormater.format(input);

        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        StateMachineDefinition stateMachine = parser.stateMachine();
        StringWriter writer = new StringWriter();
        Generator generator = new Generator(stateMachine, writer);

        try {
            generator.generate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String expectedOutput = "#include \"common.h\"\n" +
                "\n" +
                "void state_idle();\n" +
                "void state_active();\n" +
                "void state_waitingForLight();\n" +
                "void state_waitingForDrawer();\n" +
                "void state_unlockedPanel();\n" +
                "void state_idle() {\n" +
                "\tsend_event('D');\n" +
                "\tsend_event('L');\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) {\n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 'd':\n" +
                "\t\t\t\treturn state_active();\n" +
                "\t\t\tcase 'o':\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void state_active() {\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) {\n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 'w':\n" +
                "\t\t\t\treturn state_waitingForLight();\n" +
                "\t\t\tcase 'l':\n" +
                "\t\t\t\treturn state_waitingForDrawer();\n" +
                "\t\t\tcase 'o':\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void state_waitingForLight() {\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) {\n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 'l':\n" +
                "\t\t\t\treturn state_unlockedPanel();\n" +
                "\t\t\tcase 'o':\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void state_waitingForDrawer() {\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) {\n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 'w':\n" +
                "\t\t\t\treturn state_unlockedPanel();\n" +
                "\t\t\tcase 'o':\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void state_unlockedPanel() {\n" +
                "\tsend_event('U');\n" +
                "\tsend_event('C');\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) {\n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 'p':\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\tcase 'o':\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void main() {\n" +
                "\tstate_idle();\n" +
                "}\n";
                assertEquals(expectedOutput.strip(), writer.toString().strip());
    }

    @Test
    void testGenerateException() throws IOException {
        String input = """
            commands {
                doorClosed   'd'
                drawerOpened 'w'
                lightOn      'l'
                doorOpened   'o'
                panelClosed  'p'
            }

            resetCommands {
                doorOpened
            }

            events 
                unlockPanel 'U'
                lockPanel   'L'
                lockDoor    'C'
                unlockDoor  'D'
            }

            state idle {
                actions {unlockDoor lockPanel}
                doorClosed => active
            }

            state active {
                drawerOpened => waitingForLight
                lightOn => waitingForDrawer
            }

            state waitingForLight {
                lightOn => unlockedPanel
            }

            state waitingForDrawer {
                drawerOpened => unlockedPanel
            }

            state unlockedPanel {
                actions {unlockPanel lockDoor}
                panelClosed => idle
            }
            """;

        input=BracesFormater.format(input);

        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        assertThrows(ParserException.class,  parser::stateMachine);


    }
}