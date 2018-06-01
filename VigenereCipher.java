import java.util.Arrays;

/**
 * @author cassini
 * @version 3.0 using overlap
 * realize Vigenere encryption
 * */

public class VigenereCipher{
    /*
    * the frequency of letter
    * */
    public static final double[] LETTER_FREQUENCY = {
            0.08167, 0.01492, 0.02782, 0.04253, 0.12702,
            0.02228, 0.02015, 0.06094, 0.06966, 0.00153,
            0.00772, 0.04025, 0.02406, 0.06749, 0.07507,
            0.01929, 0.00095, 0.05987, 0.06327, 0.09056,
            0.02758, 0.00978, 0.02360, 0.00150, 0.01974,
            0.00074,
    };

    public static final double LETTER_MEAN = 10.65796;
    public static final double LETTER_VARIANCE = 47.49736863840005;
    public static final double WRITING_LETTER_OVERLAP = 0.06549669949999998;
    public static final double NATRUAL_LETTER_OVERLAP = 0.038461538461538464;

    private static final double ACCURACY = .005;
    /**
     * @param plainText the plain text ready to encrypt
     * @param key the encrypt key
     * @return the string, cipher text
     * */
    public static String encrypt(String plainText, String key){
        plainText = plainText.replaceAll("[^A-Za-z]", "");
        StringBuilder cipherText = new StringBuilder(plainText.toLowerCase());
        key = key.replaceAll("[^a-zA-Z]", "").toLowerCase();

        int offset;
        for (int i = 0; i < cipherText.length(); ++i){
                offset = 'a' + ((cipherText.charAt(i) - 'a') + (key.charAt(i % key.length()) - 'a')) % 26;
                cipherText.setCharAt(i, (char) offset);
            }
        return cipherText.toString();
    }

    /**
     * @param cipherText the cipher text ready to be decrypt
     * @param key the decrypt key needed
     * @return the plain text
     * */
    public static String decrypt(String cipherText, String key){
        cipherText = cipherText.replaceAll("[^A-Za-z]", "");
        StringBuilder plainText = new StringBuilder(cipherText.toLowerCase());
        key = key.replaceAll("[^a-zA-Z]", "").toLowerCase();

        int offset;
        for (int i = 0; i < plainText.length(); ++i){
            offset = (plainText.charAt(i) + 26 - key.charAt(i % key.length()) ) % 26 + 'a';
            plainText.setCharAt(i, (char) offset);
        }
        return plainText.toString();
    }

    /**
     * @param cipherText the cipher text is ready to be deciphered
     * @return the plain text and key "KEY-PLAINTEXT"
     * */
    public static String decipher(String cipherText){
        cipherText = cipherText.replaceAll("[^a-zA-Z]", "").toLowerCase();
        StringBuilder plainText = new StringBuilder(cipherText);
        StringBuilder keyText = new StringBuilder();

        //get the key length
        int keyLength = new VigenereCipher().decideKeyLength(cipherText);
        if (keyLength == -1)
            return "Sorry, we can't decipher it";

        //get key's each element by caesarDecipher
        for (int n = 0; n < keyLength; ++n){
            StringBuilder substr = new StringBuilder();
            for (int a = n; a < plainText.length(); a += keyLength)
                substr.append(plainText.charAt(a));

            keyText.append(intToLetter(caesarDecipher(substr.toString())));
        }

        //return the plaintext
        return keyText.toString() + "-" + decrypt(cipherText, keyText.toString());
    }

    //tool for decipher: using variance to find the key's length
    private int decideKeyLength(String cipherText){
        cipherText = (cipherText.replaceAll("[^a-zA-Z]", "")).toLowerCase();

        //store the possible number between the real one
        int criticPoint = -1;

        /*find the close one*/
        for (int possibleLength = 1; possibleLength < cipherText.length(); ++possibleLength){
            int column = cipherText.length() / possibleLength;
            double[] containOI = new double[possibleLength];

            //get each row's overlap index
            for (int begin = 0; begin < possibleLength; ++begin){
                //produce the row
                StringBuilder stringBuilder = new StringBuilder(column);
                int curr = begin;
                for (int t = 0; t < column; curr += possibleLength, ++t)
                    stringBuilder.append(cipherText.charAt(curr));

                containOI[begin] = getOverlapIndex(stringBuilder.toString());
            }

            //who is closer?
            double tempOI = 0;
            for (double temp : containOI)
                tempOI += temp;
            tempOI /= containOI.length;

            if (Math.abs(tempOI - WRITING_LETTER_OVERLAP) < ACCURACY) {
                criticPoint = possibleLength;
                break;
            }
        }

        return criticPoint;
    }

    /**
     * caesarCipher decipher
     * */
    public static int caesarDecipher(String cipherText){
        cipherText = cipherText.replaceAll("[^a-zA-Z]", "").toLowerCase();

        double[] errors = new double[26];
        double[] frequency = getLetterFrequency(cipherText);
        double overlap;

        //find the letter mean 26 rolls
        for (int offset = 0; offset < 26; ++offset){
            overlap = 0;

            for (int i = 0; i < 26; ++i) {
                overlap += frequency[i] *
                        LETTER_FREQUENCY[letterMoveToInt((char) ('a' + i), 0 - offset)];
            }

            errors[offset] = Math.abs(overlap - WRITING_LETTER_OVERLAP);
        }

        return getMinimumIndex(errors);
    }


    /**
     * @param numbers the numbers
     * @return index, where the minimum number is
     * */
    public static int getMinimumIndex(double[] numbers){
        int index = 0;
        double minimum = numbers[0];
        for (int m = 1; m < numbers.length; ++m)
            if (minimum - numbers[m] > 0){
                minimum = numbers[m];
                index = m;
            }
        Arrays.sort(numbers);

        return index;
    }

    /**
     * @param originLetter the English letter
     * @param offset modular addition number
     * @return the letter after movement
     * */
    public static int letterMoveToInt(char originLetter, int offset){
        int result = (Character.toLowerCase(originLetter) - 'a' + 26 + offset)
                % 26;
        try{
            if (result < 0 || result > 25)
                throw new Exception("The character is not a English letter or move incorrectly.");
            return result;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return result;
        }
    }

    /**
     * @param row the original English letter
     * @return integer 0-25
     * */
    public static int letterToInt(char row) {
        int offset = Character.toLowerCase(row) - 'a';
        try{
            if (offset < 0 || offset > 25)
                throw new Exception("The character is not a English letter.");
            return offset;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return -1;
        }
    }


    /**
     * @param offset 0-25
     * @return lowercase English letter
     */
    public static char intToLetter(int offset){
        char letter = (char) ('a' + offset);
        try{
            if (offset < 0 || offset > 25)
                throw new Exception("The offset must lower than 26 and greater less than 0");
            return letter;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return '\0';
        }
    }

    /**
     * @param string string contain English letters
     * @return int[26] each letter occurrence in the string
     * */
    public static int[] getLetterOccurrence(String string){
        string = string.replaceAll("[^a-zA-Z]", "").toLowerCase();
        int[] frequency = new int[26];

        for (int i = 0; i < string.length(); ++i)
            ++frequency[string.charAt(i) - 'a'];

        return frequency;
    }

    /**
     * @param string expect pure string with English letters
     * @return double[26] each letter appear frequency in the string
     * */
    public static double[] getLetterFrequency(String string){
        string = string.replaceAll("[^a-zA-Z]", "").toLowerCase();

        int[] occurrence = getLetterOccurrence(string);
        double[] frequency = new double[26];

        for (int i = 0; i < 26; ++i)
            frequency[i] = (double) occurrence[i] / (double) string.length();

        return frequency;
    }

    /**
     * @param letterString the English letter string needed
     *                    to compute its overlap index
     * @return the overlap of the string
     * */
    public static double getOverlapIndex(String letterString){
        letterString = letterString.replaceAll("[^A-Za-z]", "").toLowerCase();

        int n = letterString.length();
        int[] occurrance = getLetterOccurrence(letterString);
        double overlap = 0;

        for (int temp : occurrance)
            overlap += (temp * temp - temp) * 1.0 / (n * n - n);

        return overlap;
    }
}
