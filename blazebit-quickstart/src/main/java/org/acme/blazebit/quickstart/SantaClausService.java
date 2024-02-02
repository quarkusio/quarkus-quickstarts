package org.acme.blazebit.quickstart;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;

@ApplicationScoped
public class SantaClausService {

	@Inject
	EntityManager em;
	@Inject
	CriteriaBuilderFactory cbf;
	@Inject
	EntityViewManager evm;

	@Transactional
	public List<GiftView> findAllGifts() {
		CriteriaBuilder<Gift> cb = cbf.create(em, Gift.class);
		return evm.applySetting(EntityViewSetting.create(GiftView.class), cb).getResultList();
	}
}