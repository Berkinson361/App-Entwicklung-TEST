package com.example.appentwicklung;

import java.util.Random;

public class Motivation {
    private static final String[] QUOTES = new String[] {
            "Jeder kleine Schritt zählt – heute fängst du an.",
            "Du bist näher am Ziel als du denkst.",
            "Konzentrier dich auf den nächsten guten Schritt.",
            "Beständigkeit schlägt Perfektion.",
            "Fehler sind Beweise, dass du es versuchst.",
            "Du schaffst das – ich glaub an dich.",
            "Ausdauer bringt dich weiter als Talent.",
            "Heute ist ein guter Tag für Fortschritt.",
            "Mach’s einfach – einfach machen.",
            "Kleine Gewohnheiten, große Wirkung.",
            "Du bist genau so gut wie du bist"
    };
    private static final Random RNG = new Random();
    public static String randomQuote() { return QUOTES[RNG.nextInt(QUOTES.length)]; }
}

