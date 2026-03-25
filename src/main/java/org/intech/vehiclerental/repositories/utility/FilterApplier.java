package org.intech.vehiclerental.repositories.utility;

import com.blazebit.persistence.CriteriaBuilder;

import java.lang.reflect.Field;
import java.util.Collection;

public class FilterApplier {

    public static <T, F> void applyFilters(CriteriaBuilder<T> cb, F filter) {

        if(filter == null){
            return;
        }

        for (Field field : filter.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object value = field.get(filter);

                if (value == null) continue;

                Filter filterMeta = field.getAnnotation(Filter.class);
                if (filterMeta == null) continue;

                String entityField = filterMeta.field();

                switch (filterMeta.op()) {

                    case EQ -> cb.where(entityField).eq(value);

                    case LIKE -> cb.where(entityField)
                            .like()
                            .value("%" + value + "%");

                    case GE -> cb.where(entityField).ge(value);

                    case LE -> cb.where(entityField).le(value);

                    case IN -> {
                        if (value instanceof Collection<?> collection && !collection.isEmpty()) {
                            cb.where(entityField).in(collection);
                        }
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}