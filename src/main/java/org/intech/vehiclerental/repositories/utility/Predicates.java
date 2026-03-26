package org.intech.vehiclerental.repositories.utility;

import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class Predicates {

    // #### EQUALITY
    public static <T> PredicateApplier<T> eq(String field, Object value) {
        return cb -> cb.where(field).eq(value);
    }

    public static <T> PredicateApplier<T> ne(String field, Object value) {
        return cb -> cb.where(field).notEq(value);
    }

    // #### LIKE
    public static <T> PredicateApplier<T> like(String field, String value) {
        return cb -> cb.where(field).like().value("%" + value + "%");
    }

    public static <T> PredicateApplier<T> likeIgnoreCase(String field, String value) {
        return cb -> cb.where("LOWER(" + field + ")")
                .like().value("%" + value.toLowerCase() + "%");
    }

    // #### COMPARISON
    public static <T> PredicateApplier<T> ge(String field, Comparable<?> value) {
        return cb -> cb.where(field).ge(value);
    }

    public static <T> PredicateApplier<T> le(String field, Comparable<?> value) {
        return cb -> cb.where(field).le(value);
    }

    public static <T> PredicateApplier<T> between(String field, Comparable<?> min, Comparable<?> max) {
        return cb -> cb.where(field).between(min).and(max);
    }

    public static <T> PredicateApplier<T> gt(String field, Comparable<?> value) {
        return cb -> cb.where(field).gt(value);
    }

    public static <T> PredicateApplier<T> lt(String field, Comparable<?> value) {
        return cb -> cb.where(field).lt(value);
    }

    // #### IN
    public static <T> PredicateApplier<T> in(String field, Collection<?> values) {
        return cb -> cb.where(field).in(values);
    }

}
