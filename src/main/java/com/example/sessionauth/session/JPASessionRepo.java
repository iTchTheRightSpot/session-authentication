//package com.example.sessionauth.session;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository(value = "jpaSessionRepo")
//public interface JPASessionRepo extends JpaRepository<SpringSession, String> {
//
//    @Query("""
//        SELECT s FROM SpringSession s WHERE s.sessionID = :id
//    """)
//    Optional<SpringSession> findBySessionID(@Param(value = "id") String id);
//
//}
