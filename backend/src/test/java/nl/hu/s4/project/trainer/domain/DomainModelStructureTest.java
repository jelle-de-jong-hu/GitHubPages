package nl.hu.s4.project.trainer.domain;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelStructureTest {

    private Class<?> loadClass(String fqcn) {
        try {
            return Class.forName(fqcn);
        } catch (ClassNotFoundException e) {
            fail("Expected class '" + fqcn + "' was not found. Did you create it in the correct package?");
            return null; // unreachable
        }
    }

    private Field getField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            fail("Field '" + name + "' not found in " + clazz.getSimpleName());
            return null; // unreachable
        }
    }

    private void assertHasField(Class<?> clazz, String name, Class<?> type) {
        Field field = getField(clazz, name);
        assertEquals(type, field.getType(),
                "Field '" + name + "' in " + clazz.getSimpleName() + " has wrong type");
    }

    private void assertHasListFieldOf(Class<?> clazz, String name, Class<?> elementType) {
        Field field = getField(clazz, name);

        assertEquals(List.class, field.getType(),
                "Field '" + name + "' in " + clazz.getSimpleName() + " must be a List");

        Type genericType = field.getGenericType();
        assertTrue(genericType instanceof ParameterizedType,
                "Field '" + name + "' in " + clazz.getSimpleName() + " must be generic");

        ParameterizedType pt = (ParameterizedType) genericType;
        Type actualType = pt.getActualTypeArguments()[0];
        assertEquals(elementType, actualType,
                "List<...> for field '" + name + "' in " + clazz.getSimpleName()
                        + " must be List<" + elementType.getSimpleName() + ">");
    }

    @Test
    void gameHasExpectedFields() {
        Class<?> gameClass = loadClass("nl.hu.s4.project.trainer.domain.Game");
        Class<?> gameStatusClass = loadClass("nl.hu.s4.project.trainer.domain.GameStatus");
        Class<?> roundClass = loadClass("nl.hu.s4.project.trainer.domain.Round");

        assertHasField(gameClass, "status", gameStatusClass);
        assertHasListFieldOf(gameClass, "rounds", roundClass);
    }

    @Test
    void roundHasExpectedFields() {
        Class<?> roundClass = loadClass("nl.hu.s4.project.trainer.domain.Round");
        Class<?> feedbackClass = loadClass("nl.hu.s4.project.trainer.domain.Feedback");

        assertHasListFieldOf(roundClass, "attempts", feedbackClass);
    }

    @Test
    void feedbackHasExpectedFields() {
        Class<?> feedbackClass = loadClass("nl.hu.s4.project.trainer.domain.Feedback");
        Class<?> markClass = loadClass("nl.hu.s4.project.trainer.domain.Mark");

        assertHasField(feedbackClass, "guess", String.class);
        assertHasListFieldOf(feedbackClass, "marks", markClass);
    }

    @Test
    void progressHasExpectedFields() {
        Class<?> progressClass = loadClass("nl.hu.s4.project.trainer.domain.Progress");
        Class<?> gameStatusClass = loadClass("nl.hu.s4.project.trainer.domain.GameStatus");
        Class<?> feedbackClass = loadClass("nl.hu.s4.project.trainer.domain.Feedback");

        try {
            assertHasField(progressClass, "roundNumber", int.class);
            assertHasField(progressClass, "attempts", int.class);
            assertHasField(progressClass, "status", gameStatusClass);
            assertHasListFieldOf(progressClass, "feedback", feedbackClass);
        } catch (AssertionError e) {
            fail("Progress DTO is not correctly defined: " + e.getMessage());
        }
    }

    @Test
    void gameStatusEnumHasExpectedValues() {
        Class<?> statusClass = loadClass("nl.hu.s4.project.trainer.domain.GameStatus");
        assertTrue(statusClass.isEnum(), "GameStatus must be an enum");

        Object[] constants = statusClass.getEnumConstants();
        String[] names = new String[constants.length];
        for (int i = 0; i < constants.length; i++) {
            names[i] = ((Enum<?>) constants[i]).name();
        }

        assertArrayEquals(
                new String[]{"NEW", "IN_PROGRESS", "WON", "LOST"},
                names,
                "GameStatus enum must contain NEW, IN_PROGRESS, WON, LOST in that order"
        );
    }

    @Test
    void markEnumHasExpectedValues() {
        Class<?> markClass = loadClass("nl.hu.s4.project.trainer.domain.Mark");
        assertTrue(markClass.isEnum(), "Mark must be an enum");

        Object[] constants = markClass.getEnumConstants();
        String[] names = new String[constants.length];
        for (int i = 0; i < constants.length; i++) {
            names[i] = ((Enum<?>) constants[i]).name();
        }

        assertArrayEquals(
                new String[]{"CORRECT", "PRESENT", "ABSENT", "INVALID"},
                names,
                "Mark enum must contain CORRECT, PRESENT, ABSENT in that order"
        );
    }
}
