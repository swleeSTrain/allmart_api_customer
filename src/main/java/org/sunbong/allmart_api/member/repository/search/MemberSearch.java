package org.sunbong.allmart_api.member.repository.search;

import org.sunbong.allmart_api.member.domain.MemberEntity;

public interface MemberSearch {


    MemberEntity findByEmail(String email);


}
