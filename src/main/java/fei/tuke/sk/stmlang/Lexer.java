package fei.tuke.sk.stmlang;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * Lexical analyzer of the state machine language.
 */
public class Lexer {
    private static final Map<String, TokenType> keywords = Map.of(
            "commands", TokenType.COMMANDS,
            "resetCommands", TokenType.RESET_COMMANDS,
            "events", TokenType.EVENTS,
            "state", TokenType.STATE,
            "actions", TokenType.ACTIONS
    );

    private final Reader input;
    private int current;

    public Lexer(Reader input) {
        this.input = input;
    }

    //tokenization of inputed tokens from parser
    // return Token
    // in parser return next token
    public Token nextToken() {
        try {

            while (true) {
                current = input.read();

                if (current == -1) {
                    return new Token(TokenType.EOF);
                }

                //todo
                //skip whitespace, cause problem with {abc abn},
                if (Character.isWhitespace(current)) {
                    continue;
                }

                if (Character.isLetter(current)) {
                    return readNameOrKeyword();
                }

                if (current == '{') {
                    return new Token(TokenType.LEFT_BRACE);
                }

                if (current == '}') {
                    return new Token(TokenType.RIGHT_BRACE);
                }

                if (current == '\'') {
                    consume();
                    char ch = (char) current;
                    consume();
                    return new Token(TokenType.CHAR, ch);
                }

                //todo go
                //go through if = and > for commands
                if (current == '=') {
                    consume();
                    if (current == '>') {
                        return new Token(TokenType.ARROW);
                    }
                    throw new LexerException("Unexpected character: " + (char) current);
                }
            }
        } catch ( IOException e) {
            throw new LexerException("Error reading input", e);
        }
    }

    private Token readNameOrKeyword() {
        //with string builder create name or keword by char
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char) current);
            consume();
        } while (Character.isLetterOrDigit(current) || current == '_');

        //check if name or keyword
        String name = sb.toString();
        TokenType tokenType = keywords.get(name);
        if (tokenType != null) {
            return new Token(tokenType);
        }
        return new Token(TokenType.NAME, name);
    }

    private void consume() {
        try {
            current = input.read();
        } catch (IOException e) {
            throw new LexerException("Error consuming input", e);
        }
    }
}