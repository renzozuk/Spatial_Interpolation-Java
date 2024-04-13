package util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MomentConverter {
    public static Instant mapToInstant(String moment){
        Pattern firstPattern = Pattern.compile("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}");
        Matcher firstMatcher = firstPattern.matcher(moment);

        if(firstMatcher.matches()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(moment, dateTimeFormatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Etc/GMT"));
            return zonedDateTime.toInstant();
        }

        Pattern secondPattern = Pattern.compile("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}");
        Matcher secondMatcher = secondPattern.matcher(moment);

        if(secondMatcher.matches()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(moment, dateTimeFormatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Etc/GMT"));
            return zonedDateTime.toInstant();
        }

        Pattern thirdPattern = Pattern.compile("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}");
        Matcher thirdMatcher = thirdPattern.matcher(moment);

        if(thirdMatcher.matches()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(moment, dateTimeFormatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Etc/GMT"));
            return zonedDateTime.toInstant();
        }

        Pattern fourthPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        Matcher fourthMatcher = fourthPattern.matcher(moment);

        if(fourthMatcher.matches()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(moment, dateTimeFormatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Etc/GMT"));
            return zonedDateTime.toInstant();
        }

        throw new IllegalArgumentException("Moment pattern not recognized or not supported.");
    }
}
