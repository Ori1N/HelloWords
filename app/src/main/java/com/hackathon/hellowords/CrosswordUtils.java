package com.hackathon.hellowords;

/**
 * Created by Ori on 10/03/2017.
 */

public class CrosswordUtils {

    public static char[][] getCrossword() {
        return new char[][] {
                { 0,    0,  'T',    0,      0,      0,      0,      0,      0,      0 },
                { 0,    0,  'I',    0,      0,      0,      0,      'B',    0,      'C' },
                { 'D', 'O', 'G',    0,      0,      0,      0,      'E',    0,      'A' },
                { 0,    0,  'E',    'L',    'E',    'P',    'H',    'A',    'N',    'T' },
                { 0,    0,  'R',    0,      0,      0,      0,      'R',    0,      0 }
        };

    }

    public static boolean checkInput(char[][] crossword, int i, int j, char input) {
        return (crossword[i][j] == input);
    }
}
