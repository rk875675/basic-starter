package com.rahul;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test 
    public void youKnowRandomName(){
        assertEquals("Hello World!", App.buildGreeting(new String[]{}));
    }

    @Test
    public void defaultGreeting_whenNoArgs() {
        assertEquals("Hello World!", App.buildGreeting(new String[]{}));
    }

    @Test
    public void greetingWithName_whenNameProvided() {
        assertEquals("Hello Rahul", App.buildGreeting(new String[]{"--name", "Rahul"}));
    }

    @Test
    public void defaultGreeting_whenNameFlagMissingValue() {
        assertEquals("Hello World!", App.buildGreeting(new String[]{"--name"}));
    }

    @Test
    public void defaultGreeting_whenNameIsBlank() {
        assertEquals("Hello World!", App.buildGreeting(new String[]{"--name", ""}));
    }

    @Test
    public void parseName_returnsNull_whenNoFlagPresent() {
        assertNull(App.parseName(new String[]{"x", "y"}));
    }

    @Test
    public void parseName_returnsFirstMatch_ifMultipleFlags() {
        assertEquals("A", App.parseName(new String[]{"--name", "A", "--name", "B"}));
    }

    @Test
    public void ignoresUnknownArgs_stillParsesName() {
        assertEquals("Hello Rahul", App.buildGreeting(new String[]{"--foo", "bar", "--name", "Rahul"}));
    }

    @Test
    public void nameWithSpaces_whenQuotedInShell_isOneArg() {
        // In real terminal, "Rahul Kumar" would come in as one arg if quoted.
        assertEquals("Hello Rahul Kumar", App.buildGreeting(new String[]{"--name", "Rahul Kumar"}));
    }
}
