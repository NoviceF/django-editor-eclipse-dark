package org.kacprzak.eclipse.django_editor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Simple string utilities missing in Java library.
 * @author zbigniew.kacprzak
 */
@SuppressWarnings("javadoc")
public abstract class StringUtils {

    /** */
    public static final String SEP = System.getProperty("file.separator");
    /** */
    public static final String NL = System.getProperty("line.separator");
    /** */
    public static String[] WEEK_DAYS = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
    
    /** */
    public static final String DELIM = " \t\n\r\f";
    
    /**
     * Split on standard delimiters.
     * Equivalent to: <code>split(input, " \t\n\r\f")</code>
     * @param input string to split
     * @return list of strings
     */
    public static List<String> split(String input) {
        return split(input, DELIM);
    }
    /**
     * Split given string based on delimiter
     * @param input
     * @param delim
     * @return
     */
    public static List<String> split(String input, String delim) {
        final List<String> list = new ArrayList<String>();
        final StringTokenizer tokenizer = new StringTokenizer(input, delim);
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }
    
    public static String join(final String iSeparator, final Object[] iObjectList) {
        if (iObjectList.length == 0)
            return "";
        
        String aResult = "";
        for (final Object aObject : iObjectList) 
               aResult += (iSeparator + aObject.toString());
        
        // now remove leading separator
        return aResult.substring(iSeparator.length());
    }
    
    /**
     * TODO: add description
     *
     * @param iSeparator
     * @param iObjectSet
     * @return
     */
    public static String join(String iSeparator, Collection<?> iObjectSet) {
        if (iObjectSet.isEmpty())
            return "";
        
        String aResult = "";
        for (final Object aObject : iObjectSet) 
               aResult += (iSeparator + aObject );
        
        // now remove leading separator
        return aResult.substring(iSeparator.length());
    }
    
    /**
     * Count number of words within specified string
     * @param input
     * @return
     */
    public static int countWords(String input) {
        return countWords(input, DELIM);
    }

    /**
     * Count number of words within specified string
     * @param input
     * @param delim delimiters to be used for words separation
     * @return
     */
    public static int countWords(String input, String delim) {
        final StringTokenizer st = new StringTokenizer(input, delim);
        return st.countTokens();
    }

    /**
     * Get specified word from string using standard delimiters.
     * Word numbers start with 1.
     * @param input
     * @param word
     * @return
     */
    public static String getWord(String input, int word) {
        return getWord(input, word, DELIM);
    }
    
    /**
     * Get specified word from string using specified delimiters.
     * @param input
     * @param word
     * @param delim characters on which string will be tokenized
     * @return
     */
    public static String getWord(String input, int word, String delim) {
        final StringTokenizer st = new StringTokenizer(input, delim);
        if (st.countTokens() < word || word < 1)
            return null;
        int idx=0;
        while (st.hasMoreTokens()) {// is there stuff to get?
            final String wrd = st.nextToken();
            if (++idx == word)
                return wrd;
        }
        return null;
    }
    
    /**
     * Get last word using default delimiters.
     * @param input
     * @return
     */
    public static String getLastWord(String input) {
        return getWord(input, countWords(input, DELIM), DELIM);
    }
    
    /**
     * Get last word using specified delimiters.
     * @param input
     * @param delim characters on which string will be tokenized
     * @return
     */
    public static String getLastWord(String input, String delim) {
        return getWord(input, countWords(input, delim), delim);
    }

    /**
     * Get last word using default delimiters.
     * @param input
     * @return
     */
    public static String getFirstWord(String input) {
        return getWord(input, 1, DELIM);
    }
    
    /**
     * Get last word using specified delimiters.
     * @param input
     * @param delim characters on which string will be tokenized
     * @return
     */
    public static String getFirstWord(String input, String delim) {
        return getWord(input, 1, delim);
    }

    /**
     * Remove everything up to first occurrence of specified pattern including pattern itself.
     * If pattern not found, then original string is returned.
     * @param input
     * @param pattern
     * @return new string with removed substring
     */
    public static String getFromFirst(String input, String pattern) {
        return getFromFirst(input, pattern, false);
    }
    /**
     * 
     * @param input
     * @param pattern
     * @param iIncludePattern
     * @return
     */
    public static String getFromFirst(String input, String pattern, boolean iIncludePattern) {
        if (input == null || pattern == null)
            return input;
        final int pos = input.indexOf(pattern);
        final int suf = (iIncludePattern ? 0 : pattern.length());
        if (pos < 0)
            return input;
        try {
            return input.substring(pos + suf);
        } catch (final IndexOutOfBoundsException ex) {
            return input;
        }
    }
    
    /**
     * Remove everything starting from first occurrence of specified pattern including pattern itself.
     * If pattern not found, then original string is returned.
     * @param input
     * @param pattern
     * @return new string with removed substring
     */
    public static String getToFirst(String input, String pattern) {
        return getToFirst(input, pattern, false);
    }
    public static String getToFirst(String input, String pattern, boolean iIncludePattern) {
        if (input == null || pattern == null)
            return input;
        final int pos = input.indexOf(pattern);
        final int suf = (iIncludePattern ? pattern.length() : 0);
        if (pos < 0)
            return input;
        try {
            return input.substring(0, pos + suf);
        } catch (final IndexOutOfBoundsException ex) {
            return input;
        }
    }
    
    /**
     * Remove everything up to last occurrence of specified pattern including pattern itself.
     * If pattern not found, then original string is returned.
     * @param input
     * @param pattern
     * @return copy without cut off substring
     */
    public static String getFromLast(String input, String pattern) {
        return getFromLast(input, pattern, false);
    }
    public static String getFromLast(String input, String pattern, boolean iIncludePattern) {
        if (input == null || pattern == null)
            return input;
        final int pos = input.lastIndexOf(pattern);
        final int suf = (iIncludePattern ? 0 : pattern.length());
        if (pos < 0)
            return input;
        try {
            return input.substring(pos + suf);
        } catch (final IndexOutOfBoundsException ex) {
            return input;
        }
    }

    /**
     * Remove everything starting from last occurrence of specified pattern including pattern itself.
     * If pattern not found, then original string is returned.
     * @param input
     * @param pattern
     * @return copy without cut off substring
     */
    public static String getToLast(String input, String pattern) {
        return getToLast(input, pattern, false);
    }
    public static String getToLast(String input, String pattern, boolean iIncludePattern) {
        if (input == null || pattern == null)
            return input;
        final int pos = input.lastIndexOf(pattern);
        final int suf = (iIncludePattern ? pattern.length() : 0);
        try {
            return input.substring(0, pos + suf);
        } catch (final IndexOutOfBoundsException ex) {
            return input;
        }
    }
    
    /**
     * Function to right pad a string with a token to a specified length.
     * e.g. pad("1",2,"0") returns "10".
     * @param iString string to be right padded. 
     * @param length length to pad the string to.
     * @param padToken Token to pad the string with.
     * @return new converted string
     */
    public static String pad(String iString, int length, String padToken)
    {
        if (iString == null)
            return "";
        String out = iString;
        while(out.length() < length)
            out += padToken;
        return out;
    }
    public static String pad(int length, String padToken) {
        return pad("", length, padToken);
    }
    
    /**
     * Function to Left pad a string with a token to a specified length.
     * e.g. lpad("1", 3, "0") returns "001".
     * @param theString String to be right padded. 
     * @param length length to pad the string to.
     * @param padToken Token to pad the string with.
     * @return new converted string
     */
    public static String lpad(String iString, int length, String padToken)
    {
        if (iString == null)
            return "";
        String out = iString;
        while(out.length() < length)
            out = padToken + out;
        return out;
    }

    private static boolean inside(char iCh, String iDelim) {
        if (iDelim == null || iDelim.isEmpty())
            return false;
        for (int i=0; i<iDelim.length(); i++)
            if (iCh == iDelim.charAt(i))
                return true;
        return false;
    }
    /**
     * Returns a copy of the string, with leading characters omitted.
     * @param iString
     * @return
     */
    public static String ltrim(String iString, String iToDel) {
        if (iString == null || iString.isEmpty())
            return iString;
        int len = iString.length();
        int st = 0;
        char[] val = iString.toCharArray();
        while ( st < len && inside(val[st], iToDel))
            st++;
        return st > 0 ? iString.substring(st) : iString;
    }

    /**
     * Return number formatted in SI units (*1000) or BINARY units (*1024)
     * @param bytes
     * @param si
     * @return
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        // based on http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java/3758880#3758880
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
    
    /**
     * Convert collection of String objects to String[] array
     * @param iList
     * @return
     */
    public static String[] asStrings(Collection<String> iList) {
        return iList.toArray(new String[0]);
    }

    public static String getStackTraceAsString(final Exception iException) {
        // REMARK: Logger (Apache log4j) has .error("...", iException) method for logging stack trace
        if (iException == null)
            return "Cannot get stack trace; Exception object is NULL!";
        Writer aWriter = new StringWriter();
        PrintWriter aPrintWriter = new PrintWriter(aWriter);
        iException.printStackTrace(aPrintWriter);
        return aWriter.toString();
    }

    // ===================================================================================
    
    public static void main(String[] args) {
        
        // == split
        String aInStr = "First Second<br>Third<br>Fourth";
        for (String aOut : split(aInStr, "<br>"))
            System.out.println("WRONG; " + aOut);
        System.out.println("");

        for (String aOut : split(aInStr.replace("<br>", "\n")))
            System.out.println("CORRECT1; " + aOut);
        System.out.println("");

        String[] aLines = StringUtils.asStrings(StringUtils.split(aInStr.replace("<br>", "\n")));
        //String[] aLines = StringUtils.split(aInStr.replace("<br>", "\n")).toArray(new String[0]);
        for (String aOut : aLines)
            System.out.println("CORRECT2; " + aOut);
        System.out.println("");

        // == join
        
        // == ltrim
        String aTrim = "00:02.240";
        System.out.println(aTrim + " ==> " + ltrim(aTrim, "0:"));
    }
}
