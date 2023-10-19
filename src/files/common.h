#include <stdio.h>

char read_command() {
    return getc(stdin);
}

void send_event(char c) {
    putc(c, stdout);
    putc('\n', stdout);
}