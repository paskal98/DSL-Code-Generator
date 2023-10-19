package fei.tuke.sk.stmlang;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void testEmptyInput() {
        Lexer lexer = new Lexer(new StringReader(""));
        Token token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.tokenType());
    }

    @Test
    void testSingleTokens() {
        String input = "commands resetCommands events state actions";
        Lexer lexer = new Lexer(new StringReader(input));

        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMANDS, token.tokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.RESET_COMMANDS, token.tokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.EVENTS, token.tokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.STATE, token.tokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.ACTIONS, token.tokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.tokenType());
    }

    @Test
    void testCommands() {
        String input = "commands {doorClosed 'd'}";
        input=BracesFormater.format(input);
        Lexer lexer = new Lexer(new StringReader(input));

        assertEquals(TokenType.COMMANDS, lexer.nextToken().tokenType());
        assertEquals(TokenType.LEFT_BRACE, lexer.nextToken().tokenType());
        assertEquals(TokenType.NAME, lexer.nextToken().tokenType());
        assertEquals(TokenType.CHAR, lexer.nextToken().tokenType());
        assertEquals(TokenType.RIGHT_BRACE, lexer.nextToken().tokenType());
        assertEquals(TokenType.EOF, lexer.nextToken().tokenType());
    }

    @Test
    void testStateWithActionsAndTransitions() {
        String input = "" +
                "state idle { " +
                    "actions {unlockDoor lockPanel} " +
                    "doorClosed => active " +
                "}";
        input=BracesFormater.format(input);
        Lexer lexer = new Lexer(new StringReader(input));

        assertEquals(TokenType.STATE, lexer.nextToken().tokenType());
        assertEquals(TokenType.NAME, lexer.nextToken().tokenType());
        assertEquals(TokenType.LEFT_BRACE, lexer.nextToken().tokenType());
        assertEquals(TokenType.ACTIONS, lexer.nextToken().tokenType());
        assertEquals(TokenType.LEFT_BRACE, lexer.nextToken().tokenType());
        assertEquals(TokenType.NAME, lexer.nextToken().tokenType());
        assertEquals(TokenType.NAME, lexer.nextToken().tokenType());
        assertEquals(TokenType.RIGHT_BRACE, lexer.nextToken().tokenType());
        assertEquals(TokenType.NAME, lexer.nextToken().tokenType());
        assertEquals(TokenType.ARROW, lexer.nextToken().tokenType());
        assertEquals(TokenType.NAME, lexer.nextToken().tokenType());
        assertEquals(TokenType.RIGHT_BRACE, lexer.nextToken().tokenType());
        assertEquals(TokenType.EOF, lexer.nextToken().tokenType());
    }

    @Test
    void testInvalidCharacterException() {
        Lexer lexer = new Lexer(new StringReader("@=@"));

        assertThrows(LexerException.class, lexer::nextToken);
    }




}