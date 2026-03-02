package org.intech.vehiclerental.dto.vehicledto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.VehicleImage;

import java.time.Instant;

@EntityView(VehicleImage.class)
public interface VehicleImageView {
    @IdMapping
    Long getId();

    String getImageUrl();
    Boolean getIsPrimary();
    Instant getCreatedAt();
    String getCaption();
    Integer getDisplayOrder();
}