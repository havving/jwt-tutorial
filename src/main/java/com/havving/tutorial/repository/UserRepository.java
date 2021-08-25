package com.havving.tutorial.repository;

import com.havving.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author HAVVING
 * @since 2021-08-25
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * @EntityGraph : 쿼리가 수행될 때 Lazy 조회가 아닌 Eager 조회로 authorities 정보를 같이 get
     **/
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUserName(String userName);
}
