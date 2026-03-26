package org.intech.vehiclerental.repositories.utility;

import com.blazebit.persistence.CriteriaBuilder;

@FunctionalInterface
public interface PredicateApplier<T> {
    void apply(CriteriaBuilder<T> cb);
}