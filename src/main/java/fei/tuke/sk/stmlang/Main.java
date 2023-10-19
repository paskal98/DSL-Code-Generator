package fei.tuke.sk.stmlang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static fei.tuke.sk.stmlang.BracesFormater.format;
import static fei.tuke.sk.stmlang.BracesFormater.revertFormat;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/files/input.txt";

        //pre format input
        try {
            String content = Files.readString(Paths.get(filePath));
            String formatted = format(content);
            Files.writeString(Paths.get(filePath), formatted);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // main of generate c file
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath)); BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/files/state_machine.c"))) {

            Lexer lexer = new Lexer(reader);

            Parser parser = new Parser(lexer);


            StateMachineDefinition stateMachine = parser.stateMachine();

            Generator generator = new Generator(stateMachine, writer);
            generator.generate();

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        //reverse foramt changes
        try {
            String content = Files.readString(Paths.get(filePath));
            String formatted = revertFormat(content);
            Files.writeString(Paths.get(filePath), formatted);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}