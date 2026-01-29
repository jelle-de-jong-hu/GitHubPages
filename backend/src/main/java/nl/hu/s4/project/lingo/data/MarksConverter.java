package nl.hu.s4.project.lingo.data;

import jakarta.persistence.AttributeConverter;
import nl.hu.s4.project.lingo.domain.Mark;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MarksConverter implements AttributeConverter<List<Mark>, String> {
    @Override
    public String convertToDatabaseColumn(List<Mark> marks) {
        if (marks == null) {
            return "";
        }
        return marks.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Mark> convertToEntityAttribute(String s) {
        if (s == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(this::isValidMark)
                .map(Mark::valueOf)
                .toList();
    }

    private boolean isValidMark(String mark) {
        try {
            Mark.valueOf(mark);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
