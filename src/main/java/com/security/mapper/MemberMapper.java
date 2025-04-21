package com.security.mapper;

import com.security.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    int save(Member member);
    Member findById(String id);
}
