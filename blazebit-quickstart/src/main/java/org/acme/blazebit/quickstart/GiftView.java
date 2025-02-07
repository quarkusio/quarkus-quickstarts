package org.acme.blazebit.quickstart;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

@EntityView(Gift.class)
public interface GiftView {

	@IdMapping
	Long getId();

	String getName();
}