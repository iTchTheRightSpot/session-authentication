//package com.example.sessionauth.session;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.session.FindByIndexNameSessionRepository;
//import org.springframework.stereotype.Component;
//import org.springframework.util.SerializationUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Component(value = "customFindIndexRepo")
//@Slf4j
//public class CustomFindIndexRepo implements FindByIndexNameSessionRepository<CustomMapSession> {
//    private final JPASessionRepo jpaSessionRepo;
//
//    @Autowired
//    public CustomFindIndexRepo(JPASessionRepo jpaSessionRepo) {
//        this.jpaSessionRepo = jpaSessionRepo;
//    }
//
//    @Override
//    public Map<String, CustomMapSession> findByIndexNameAndIndexValue(String s, String s1) {
//        return null;
//    }
//
//    @Override
//    public CustomMapSession createSession() {
//        return null;
//    }
//
//    @Override
//    public void save(CustomMapSession customMapSession) {
//        log.info("Saved a session from {}", CustomFindIndexRepo.class);
//        SpringSession springSession = convertToSpringSession(customMapSession);
//        this.jpaSessionRepo.save(springSession);
//    }
//
//    @Override
//    public CustomMapSession findById(String sessionID) {
//        SpringSession springSession = this.jpaSessionRepo.findBySessionID(sessionID).orElse(null);
//        if (springSession != null) { }
//        return null;
//    }
//
//    @Override
//    public void deleteById(String s) {
//
//    }
//
//    private SpringSession convertToSpringSession(CustomMapSession customMapSession) {
//        SpringSession springSession = new SpringSession();
//        springSession.setSessionID(customMapSession.changeSessionId());
//        springSession.setLastAccessedTime(customMapSession.getLastAccessedTime().toEpochMilli());
//        springSession.setMaxInActive(customMapSession.getMaxInactiveInterval().toMillis());
//        springSession.setCreationTime(customMapSession.getCreationTime().toEpochMilli());
//        springSession.setLastAccessedTime(customMapSession.getLastAccessedTime().toEpochMilli());
//        springSession.setExpiryTime(null);
//
//        return springSession;
//    }
//
//    private void serializeAttribute(CustomMapSession customMapSession, SpringSession springSession) {
//        Map<String, Object> map = new HashMap<>();
//
//        for (String key : customMapSession.getAttributeNames()) {
//            Object value = map.get(key);
//            map.put(key, value);
//        }
//
//        // Using Spring Framework to Serialize Session
//        byte[] bytes = SerializationUtils.serialize(map);
////        springSession.addAttribute();
//
//    }
//
//
//
//}
