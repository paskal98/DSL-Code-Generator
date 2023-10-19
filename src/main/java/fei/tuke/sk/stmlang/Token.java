package fei.tuke.sk.stmlang;

public class Token {
    private final TokenType type;
    private final Object value;

    public Token(TokenType type) {
        this(type, null);
    }

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public TokenType tokenType() {
        return getType();
    }
}