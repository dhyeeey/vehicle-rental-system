package org.intech.vehiclerental.repositories.utility;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import jakarta.persistence.EntityManager;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QueryBuilder {

    @SafeVarargs
    public static <T> CriteriaBuilder<T> build(
            CriteriaBuilderFactory cbf,
            EntityManager em,
            Class<T> clazz,
            PredicateApplier<T>... predicates
    ) {
        CriteriaBuilder<T> cb = cbf.create(em, clazz);

        for (PredicateApplier<T> predicate : predicates) {
            predicate.apply(cb);
        }

        return cb;
    }

    @SafeVarargs
    public static <T, V> CriteriaBuilder<V> buildWithView(
            CriteriaBuilderFactory cbf,
            EntityManager em,
            EntityViewManager evm,
            Class<T> entityClass,
            Class<V> viewClass,
            PredicateApplier<T>... predicates
    ) {
        CriteriaBuilder<T> cb = build(cbf, em, entityClass, predicates);

        var setting = EntityViewSetting.create(viewClass);
        return evm.applySetting(setting, cb);
    }
}
