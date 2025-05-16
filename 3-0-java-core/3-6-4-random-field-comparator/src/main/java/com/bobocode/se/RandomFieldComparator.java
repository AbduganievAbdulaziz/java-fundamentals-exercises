package com.bobocode.se;

import com.bobocode.util.ExerciseNotCompletedException;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

import static java.util.Objects.requireNonNull;

/**
 * A generic comparator that is comparing a random field of the given class. The field is either primitive or
 * {@link Comparable}. It is chosen during comparator instance creation and is used for all comparisons.
 * <p>
 * If no field is available to compare, the constructor throws {@link IllegalArgumentException}
 *
 * @param <T> the type of the objects that may be compared by this comparator
 *<p><p>
 *  <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com">visit our website</a></strong>
 *  <p>
 *
 * @author Stanislav Zabramnyi
 */
public class RandomFieldComparator<T> implements Comparator<T> {
    private Class<T> targetType;
    private Field comparingField;

    private Field randomComparableField(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                     .filter(field -> field.getClass().isPrimitive() || Comparable.class.isAssignableFrom(field.getType()))
                     .findAny()
                     .orElseThrow(IllegalArgumentException::new);
    }

    public RandomFieldComparator(Class<T> targetType) {
        this.targetType = requireNonNull(targetType);
        this.comparingField = randomComparableField(targetType);
    }

    /**
     * Compares two objects of the class T by the value of the field that was randomly chosen. It allows null values
     * for the fields, and it treats null value greater than a non-null value.
     *
     * @param o1
     * @param o2
     * @return positive int in case of first parameter {@param o1} is greater than second one {@param o2},
     *         zero if objects are equals,
     *         negative int in case of first parameter {@param o1} is less than second one {@param o2}.
     */
    @Override
    public int compare(T o1, T o2) {
        return compareFieldValues(requireNonNull(o1), requireNonNull(o2));
    }

    /**
     * Returns the name of the randomly-chosen comparing field.
     */
    public String getComparingFieldName() {
        return comparingField.getName();
    }

    /**
     * Returns a statement "Random field comparator of class '%s' is comparing '%s'" where the first param is the name
     * of the type T, and the second parameter is the comparing field name.
     *
     * @return a predefined statement
     */
    @Override
    public String toString() {
        return String.format("Random field comparator of class '%s' is comparing '%s'", targetType.getSimpleName(),
                getComparingFieldName());
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private <U extends Comparable<? super U>> int compareFieldValues(T o1, T o2) {
        comparingField.setAccessible(true);
        var fieldValue1 = (U) comparingField.get(o1);
        var fieldValue2 = (U) comparingField.get(o2);
        Comparator<U> comparator = Comparator.nullsLast(Comparator.naturalOrder());
        return comparator.compare(fieldValue1, fieldValue2);
    }
}
