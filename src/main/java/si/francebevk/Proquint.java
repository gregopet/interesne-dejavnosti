package si.francebevk;

/*
    BSD 3-clause "New" or "Revised" License

    Copyright (c) 2009 Daniel S. Wilkerson
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are
    met:

        Redistributions of source code must retain the above copyright
        notice, this list of conditions and the following disclaimer.
        Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in
        the documentation and/or other materials provided with the
        distribution.

        Neither the name of Daniel S. Wilkerson nor the names of its
        contributors may be used to endorse or promote products derived
        from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
    A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
    OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
    SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
    LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
    OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/**
 * A class for generating <a href="https://arxiv.org/html/0901.4016">Proquints</a>.
 * Proquints can encode a numeric key into an easily pronouncible word.
 *
 * This class was copied from https://github.com/dsw/proquint
 * Modifications were added for it to work with strings instead of StringBuffers
 * and to accept Strings (of lower or upper case) for input. Signed ints are now returned and - was
 * hardcoded as the separator.
 */
public class Proquint {

    /** Map uints to consonants. */
    private static final char uint2consonant[] = {
        'b', 'd', 'f', 'g',
        'h', 'j', 'k', 'l',
        'm', 'n', 'p', 'r',
        's', 't', 'v', 'z'
    };

    /** Map uints to vowels. */
    private static final char uint2vowel[] = {
        'a', 'i', 'o', 'u'
    };

    /** Convert an int to a proquint */
    public static String int2quint(int i) {
        char[] charArray = new char[11];
        uint2quint(i, charArray, 0);
        return new String(charArray);
    }

    /** Convert a long to a proquint, shortening the proquint if the value is in the 32bit range */
    public static String long2quint(long i) {
        int upper = (int)(i >>> 32);
        int lower = (int)(i);

        if (upper != 0) {
            char[] charArray = new char[23];
            uint2quint(upper, charArray, 0);
            charArray[11] = '-';
            uint2quint(lower, charArray, 12);
            return new String(charArray);
        } else {
            return int2quint(lower);
        }
    }

    /** Convert a long to a proquint that will always contain 4 words, regardless of the parameter's value */
    public static String long2quint4word(long i) {
        int upper = (int)(i >>> 32);
        int lower = (int)(i);

        char[] charArray = new char[23];
        uint2quint(upper, charArray, 0);
        charArray[11] = '-';
        uint2quint(lower, charArray, 12);
        return new String(charArray);
    }


    /**
     * Convert a proquint to an int.
     */
    public static int quint2int(String quint) {
        int res = 0;

        for (int pos = 0; pos < quint.length(); pos++) {

            // case insensitivity
            char charAtPos = quint.charAt(pos);
            if (charAtPos < 'a') charAtPos += 32;

            switch(charAtPos) {

                /* consonants */
                case 'b': res <<= 4; res +=  0; break;
                case 'd': res <<= 4; res +=  1; break;
                case 'f': res <<= 4; res +=  2; break;
                case 'g': res <<= 4; res +=  3; break;

                case 'h': res <<= 4; res +=  4; break;
                case 'j': res <<= 4; res +=  5; break;
                case 'k': res <<= 4; res +=  6; break;
                case 'l': res <<= 4; res +=  7; break;

                case 'm': res <<= 4; res +=  8; break;
                case 'n': res <<= 4; res +=  9; break;
                case 'p': res <<= 4; res += 10; break;
                case 'r': res <<= 4; res += 11; break;

                case 's': res <<= 4; res += 12; break;
                case 't': res <<= 4; res += 13; break;
                case 'v': res <<= 4; res += 14; break;
                case 'z': res <<= 4; res += 15; break;

                /* vowels */
                case 'a': res <<= 2; res +=  0; break;
                case 'i': res <<= 2; res +=  1; break;
                case 'o': res <<= 2; res +=  2; break;
                case 'u': res <<= 2; res +=  3; break;

                /* separators */
                default: break;
            }
        }

        return res;
    }

    /**
     * Converts a long quint to a long number.
     * @param quint The encoded long number
     * @return A long
     */
    public static long quint2long(String quint) {
        if (quint.length() <= 11) {
            return (long)quint2int(quint);
        } else {
            return Integer.toUnsignedLong(quint2int(quint.substring(0, 11))) << 32 | Integer.toUnsignedLong(quint2int(quint.substring(12)));
        }
    }

    /**
     * Returns whether the given string looks like a valid 32 bit Proquint.
     * The method is null safe.
     *
     * @param quint
     * @return True if the word looks like a 32 bit Proquint, false otherwise.
     */
    public static boolean isProquint(String quint) {
        return
            quint != null &&
            quint.length() == 11 &&
            isConsonant(quint.charAt(0)) &&
                isVowel(quint.charAt(1)) &&
            isConsonant(quint.charAt(2)) &&
                isVowel(quint.charAt(3)) &&
            isConsonant(quint.charAt(4)) &&
            isConsonant(quint.charAt(6)) &&
                isVowel(quint.charAt(7)) &&
            isConsonant(quint.charAt(8)) &&
                isVowel(quint.charAt(9)) &&
            isConsonant(quint.charAt(10));
    }

    /**
     * Returns whether the given string looks like a valid 64 bit Proquint.
     * The method is null safe.
     *
     * @param quint The string we would like to check
     * @return True if the word looks like a 64 bit Proquint, false otherwise.
     */
    public static boolean isLongProquint(String quint) {
        return
            quint != null &&
            quint.length() == 23 &&
            isConsonant(quint.charAt(0)) &&
            isVowel(quint.charAt(1)) &&
            isConsonant(quint.charAt(2)) &&
            isVowel(quint.charAt(3)) &&
            isConsonant(quint.charAt(4)) &&
            isConsonant(quint.charAt(6)) &&
            isVowel(quint.charAt(7)) &&
            isConsonant(quint.charAt(8)) &&
            isVowel(quint.charAt(9)) &&
            isConsonant(quint.charAt(10)) &&
            isConsonant(quint.charAt(12)) &&
            isVowel(quint.charAt(13)) &&
            isConsonant(quint.charAt(14)) &&
            isVowel(quint.charAt(15)) &&
            isConsonant(quint.charAt(16)) &&
            isConsonant(quint.charAt(18)) &&
            isVowel(quint.charAt(19)) &&
            isConsonant(quint.charAt(20)) &&
            isVowel(quint.charAt(21)) &&
            isConsonant(quint.charAt(22));
    }

    private static boolean isVowel(char letter) {
        if (letter < 'a') letter += 32; // case insensitivity
        return letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u';
    }

    private static boolean isConsonant(char letter) {
        if (letter < 'a') letter += 32; // case insensitivity
        return letter >= 'a' && letter <= 'z' && !isVowel(letter);
    }

    private static void uint2quint(int i, char[] charArray, int arrayStart) {
        // http://docs.oracle.com/javase/tutorial/java/nutsandbolts/opsummary.html
        // ">>>" Unsigned right shift
        int j;

        final int MASK_FIRST4 = 0xF0000000;
        final int MASK_FIRST2 = 0xC0000000;

        j = i & MASK_FIRST4; i <<= 4; j >>>= 28; charArray[arrayStart] = uint2consonant[j];
        j = i & MASK_FIRST2; i <<= 2; j >>>= 30; charArray[arrayStart + 1] = uint2vowel[j];
        j = i & MASK_FIRST4; i <<= 4; j >>>= 28; charArray[arrayStart + 2] = uint2consonant[j];
        j = i & MASK_FIRST2; i <<= 2; j >>>= 30; charArray[arrayStart + 3] = uint2vowel[j];
        j = i & MASK_FIRST4; i <<= 4; j >>>= 28; charArray[arrayStart + 4] = uint2consonant[j];

        charArray[arrayStart + 5] = '-';

        j = i & MASK_FIRST4; i <<= 4; j >>>= 28; charArray[arrayStart + 6] = uint2consonant[j];
        j = i & MASK_FIRST2; i <<= 2; j >>>= 30; charArray[arrayStart + 7] = uint2vowel[j];
        j = i & MASK_FIRST4; i <<= 4; j >>>= 28; charArray[arrayStart + 8] = uint2consonant[j];
        j = i & MASK_FIRST2; i <<= 2; j >>>= 30; charArray[arrayStart + 9] = uint2vowel[j];
        j = i & MASK_FIRST4; i <<= 4; j >>>= 28; charArray[arrayStart + 10] = uint2consonant[j];
    }
}
