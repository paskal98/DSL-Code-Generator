StateMachine  -> { Statement }
Statement     -> Commands | ResetCommands | Events | State
Commands      -> "commands" "{" { NAME CHAR } "}"
Events        -> "events" "{" { NAME CHAR } "}"
ResetCommands -> "resetCommands" "{" { NAME } "}"
State         -> "state" "{" [Actions] { Transition } "}"
Actions       -> "actions" "{" { NAME } "}"
Transition    -> NAME "->" NAME