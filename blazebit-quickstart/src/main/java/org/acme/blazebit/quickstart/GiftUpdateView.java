package org.acme.blazebit.quickstart;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;

@UpdatableEntityView
@CreatableEntityView
@EntityView(Gift.class)
public interface GiftUpdateView extends GiftView {

	void setName(String name);
}