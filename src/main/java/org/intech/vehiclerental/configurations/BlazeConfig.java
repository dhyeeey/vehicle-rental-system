package org.intech.vehiclerental.configurations;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Objects;
import java.util.Set;

@Configuration
public class BlazeConfig {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    /*
    @Bean
    EntityViewConfiguration entityViewConfiguration() {

        System.out.println("Initializing Blaze Entity Views");

        EntityViewConfiguration cfg = EntityViews.createDefaultConfiguration();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false) {
                    @Override
                    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                        return beanDefinition.getMetadata().isIndependent();
                    }
                };

        scanner.addIncludeFilter(new AnnotationTypeFilter(EntityView.class));

        Set<BeanDefinition> components =
                scanner.findCandidateComponents("org.intech.vehiclerental.dto");

        System.out.println("Found entity views: " + components.size());

        for (BeanDefinition component : components) {
            try {
                Class<?> clazz = ClassUtils.forName(
                        Objects.requireNonNull(component.getBeanClassName()),
                        Thread.currentThread().getContextClassLoader()
                );

                System.out.println("Registering EntityView: " + clazz.getName());
                cfg.addEntityView(clazz);

            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to load entity view", e);
            }
        }

        return cfg;
    }
   */

    @Bean
    EntityViewConfiguration entityViewConfiguration() {

        System.out.println("Initializing Blaze Entity Views");
        EntityViewConfiguration cfg = EntityViews.createDefaultConfiguration();
        Reflections reflections = new Reflections("org.intech.vehiclerental.dto");
        Set<Class<?>> entityViews =
                reflections.getTypesAnnotatedWith(EntityView.class);
        System.out.println("Found entity views: " + entityViews.size());
        for (Class<?> view : entityViews) {
            System.out.println("Registering EntityView: " + view.getName());
            cfg.addEntityView(view);
        }
        return cfg;
    }

    @Bean
    public CriteriaBuilderFactory createCriteriaBuilderFactory() {
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        return config.createCriteriaBuilderFactory(entityManagerFactory);
    }

    @Bean
    public EntityViewManager createEntityViewManager(CriteriaBuilderFactory cbf,
                                                     EntityViewConfiguration entityViewConfiguration) {
        return entityViewConfiguration.createEntityViewManager(cbf);
    }
}
