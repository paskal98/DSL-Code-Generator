package fei.tuke.sk.stmlang;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
Grammar:
        * StateMachine  -> { Statement }
        * Statement     -> Commands | ResetCommands | Events | State
        * Commands      -> "commands" "{" { NAME CHAR } "}"
        * Events        -> "events" "{" { NAME CHAR } "}"
        * ResetCommands -> "resetCommands" "{" { NAME } "}"
        * State         -> "state" "{" [Actions] { Transition } "}"
        * Actions       -> "actions" "{" { NAME } "}"
        * Transition    -> NAME "->" NAME
 */

public class Parser {
    private final Lexer lexer;
    private Token symbol;
    private StateMachineDefinition definition;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public StateMachineDefinition stateMachine() {
        definition = new StateMachineDefinition();
        Set<TokenType> first = Set.of(
                TokenType.COMMANDS,
                TokenType.EVENTS,
                TokenType.RESET_COMMANDS,
                TokenType.STATE);
        consume();
        while (first.contains(symbol.tokenType())) {
            switch (symbol.tokenType()) {
                case COMMANDS -> commands();
                case EVENTS -> events();
                case RESET_COMMANDS -> resetCommands();
                case STATE -> state();
            }
        }
        match(TokenType.EOF);
        return definition;
    }

    // read token by token for commands
    private void commands() {
        match(TokenType.COMMANDS);
        match(TokenType.LEFT_BRACE);
        while (symbol.getType() == TokenType.NAME) {
            String name = (String) symbol.getValue();
            match(TokenType.NAME);
            char code = (char) symbol.getValue();
            match(TokenType.CHAR);
            definition.addCommand(name, code);
        }
        match(TokenType.RIGHT_BRACE);
    }

    // read token by token for events
    private void events() {
        match(TokenType.EVENTS);
        match(TokenType.LEFT_BRACE);
        while (symbol.getType() == TokenType.NAME) {
            String name = (String) symbol.getValue();
            match(TokenType.NAME);
            char code = (char) symbol.getValue();
            match(TokenType.CHAR);
            definition.addEvent(name, code);
        }
        match(TokenType.RIGHT_BRACE);
    }

    // read token by token for resetCommands
    private void resetCommands() {
        match(TokenType.RESET_COMMANDS);
        match(TokenType.LEFT_BRACE);
        while (symbol.getType() == TokenType.NAME) {
            String name = (String) symbol.getValue();
            match(TokenType.NAME);
            definition.addResetCommands(name);
        }
        match(TokenType.RIGHT_BRACE);
    }

    // read token by token state and create transitions or/and comands
    private void state() {
        match(TokenType.STATE);
        String stateName = (String) symbol.getValue();
        match(TokenType.NAME);
        match(TokenType.LEFT_BRACE);
        List<String> actions = new ArrayList<>();
        if (symbol.getType() == TokenType.ACTIONS) {
            actions = actions();
        }

        boolean isEmptyState=true;
        while (symbol.getType() == TokenType.NAME) {
            TransitionDefinition transition = transition();

            StateDefinition stateDefinition = new StateDefinition();
            for (int i = 0; i < actions.size(); i++) {
                stateDefinition.addAction(actions.get(i));
            }
            stateDefinition.addTransition(transition);

            definition.addState(stateName, stateDefinition);
            isEmptyState=false;
        }
        match(TokenType.RIGHT_BRACE);
        if(isEmptyState) definition.addState(stateName, null);
    }

    //create actions if consist in state
    private List<String> actions() {
        List<String> actions = new ArrayList<>();
        match(TokenType.ACTIONS);
        if (symbol.getType() == TokenType.LEFT_BRACE) {

            match(TokenType.LEFT_BRACE);
            while (symbol.getType() == TokenType.NAME) {
                String action = (String) symbol.getValue();
                actions.add(action);
                match(TokenType.NAME);

                if (symbol.getType() == TokenType.RIGHT_BRACE) {
                    break;
                } else if (symbol.getType() != TokenType.NAME) {
                    throw new IllegalStateException("Unexpected token: "+symbol.getType());
                }

            }
            match(TokenType.RIGHT_BRACE);


        } else {

            while (symbol.getType() == TokenType.NAME) {
                String action = (String) symbol.getValue();
                actions.add(action);
                match(TokenType.NAME);
                if (symbol.getType() == TokenType.ARROW) {
                    match(TokenType.ARROW);
                } else {
                    break;
                }
            }

        }
        return actions;
    }

    private TransitionDefinition transition() {
        String trigger = (String) symbol.getValue();
        match(TokenType.NAME);
        match(TokenType.ARROW);
        String targetState = (String) symbol.getValue();
        match(TokenType.NAME);
        return new TransitionDefinition(trigger, targetState);
    }

    private void match(TokenType expectedSymbol) {
        if (symbol.getType() == expectedSymbol) {
            consume();
        } else {
            throw new ParserException("Unexpected token: "+symbol.getType()+", expected: "+expectedSymbol);
        }
    }

    private void consume() {
        symbol = lexer.nextToken();
    }
}