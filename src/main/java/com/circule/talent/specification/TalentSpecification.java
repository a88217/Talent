package com.circule.talent.specification;

import com.circule.talent.model.Talent;
import com.circule.talent.dto.talents.TalentParamsDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TalentSpecification {
    public Specification<Talent> build(TalentParamsDTO params) {
        return withLastNameCont(params.getLastNameCont())
                .and(withProfessionId(params.getProfessionId()));
    }

    private Specification<Talent> withLastNameCont(String lastNameCont) {
        return (root, query, cb) -> lastNameCont == null ? cb.conjunction()
                : cb.like(cb.lower(root.get("lastName")), "%" + lastNameCont.toLowerCase() + "%");
    }

    private Specification<Talent> withProfessionId(Long professionId) {
        return (root, query, cb) -> professionId == null ? cb.conjunction()
                : cb.equal(root.join("professions").get("id"), professionId);
    }
}
