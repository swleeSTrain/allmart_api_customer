package org.sunbong.allmart_api.member.repository.search;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.allmart_api.member.domain.MemberEntity;
import org.sunbong.allmart_api.member.domain.QMemberEntity;

public class MemberSearchImpl extends QuerydslRepositorySupport implements MemberSearch {
    public MemberSearchImpl() {
        super(MemberEntity.class);
    }

    @Override
    public MemberEntity findByEmail(String email) {
        return from(QMemberEntity.memberEntity)
                .where(QMemberEntity.memberEntity.email.eq(email))
                .fetchOne();
    }



}
