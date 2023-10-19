package fei.tuke.sk.stmlang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BracesFormater {

    public static String format(String input) {
        Pattern pattern = Pattern.compile("(actions\\s*\\{)([^}]*)(})", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        StringBuffer formattedInput = new StringBuffer();

        while (matcher.find()) {
            String actionsSection = matcher.group(2).trim();
            String formattedSection = "{ " + actionsSection + " }";
            matcher.appendReplacement(formattedInput, "actions " + formattedSection);
        }
        matcher.appendTail(formattedInput);

        return formattedInput.toString();
    }

    public static String revertFormat(String input) {
        Pattern pattern = Pattern.compile("(actions\\s*\\{\\s)([^}]*)(\\s})", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        StringBuffer revertedInput = new StringBuffer();

        while (matcher.find()) {
            String actionsSection = matcher.group(2).trim();
            String revertedSection = "{" + actionsSection + "}";
            matcher.appendReplacement(revertedInput, "actions " + revertedSection);
        }
        matcher.appendTail(revertedInput);

        return revertedInput.toString();
    }

}
