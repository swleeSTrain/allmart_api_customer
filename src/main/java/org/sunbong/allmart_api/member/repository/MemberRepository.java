package org.sunbong.allmart_api.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.member.domain.MemberEntity;
import org.sunbong.allmart_api.member.repository.search.MemberSearch;

/**/
public interface MemberRepository extends JpaRepository<MemberEntity, String/*pk타입 문자열*/>, MemberSearch {
    
    /* 내가직접다해야함 */

}
