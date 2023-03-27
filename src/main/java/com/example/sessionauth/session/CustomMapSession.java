package com.example.sessionauth.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.session.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record CustomMapSession(SpringSession session) implements Session {

    @Value(value = "${session.expiry.time}")
    private static Long maxSessionValidTime;

    @Override
    public String getId() {
        return this.session.getSessionID();
    }

    @Override
    public String changeSessionId() {
        String changeID = generateID();
        this.session.setSessionID(changeID);
        return changeID;
    }

    @Override
    public <T> T getAttribute(String attributeName) {
        return (T) this
                .session
                .getAttributes() //
                .stream() //
                .filter(attributes -> attributes.getAttributeName().equals(attributeName))
                .findFirst() //
                .orElse(null);
    }

    @Override
    public Set<String> getAttributeNames() {
        return this
                .session
                .getAttributes() //
                .stream() //
                .map(SessionAttributes::getAttributeName)
                .collect(Collectors.toSet());
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            removeAttribute(attributeName);
            return;
        }

        this
                .session
                .getAttributes() //
                .stream() //
                .filter( attributes -> attributes.getAttributeName().equals(attributeName))
                .findAny() //
                .ifPresent(this.session::removeAttribute);
    }

    @Override
    public void removeAttribute(String attributeName) {
        this
                .session
                .getAttributes() //
                .stream() //
                .filter(attributes -> attributes.getAttributeName().equals(attributeName))
                .findFirst() //
                .ifPresent(this.session::removeAttribute);
    }

    @Override
    public Instant getCreationTime() {
        return Instant.ofEpochSecond(this.session.getCreationTime());
    }

    @Override
    public void setLastAccessedTime(Instant instant) {
        this.session.setLastAccessedTime(instant.toEpochMilli());
    }

    @Override
    public Instant getLastAccessedTime() {
        return Instant.ofEpochSecond(this.session.getLastAccessedTime());
    }

    @Override
    public void setMaxInactiveInterval(Duration duration) {
        this.session.setMaxInActive(duration.toMillis());
    }

    @Override
    public Duration getMaxInactiveInterval() {
        return Duration.ofSeconds(this.session.getMaxInActive());
    }

    @Override
    public boolean isExpired() {
        return expired(Instant.now());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Session && this.session.getSessionID().equals(((Session) obj).getId());
    }

    private static String generateID() {
        return UUID.randomUUID().toString();
    }

    /**
     * A session is expired based on the following:
     * * If Max Interval is expired
     * * If now is greater than creation time plus max session valid time (milliseconds)
     *
     * @param now
     * @return boolean
     * */
    private boolean expired(Instant now) {
        if (getMaxInactiveInterval().isNegative()) {
            return false;
        }
        return (Instant.now().toEpochMilli() > (getCreationTime().toEpochMilli() + maxSessionValidTime)) ||
                now.minus(getMaxInactiveInterval()).compareTo(getLastAccessedTime()) >= 0;
    }

}
