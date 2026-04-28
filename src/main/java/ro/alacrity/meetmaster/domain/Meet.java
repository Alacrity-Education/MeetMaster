package ro.alacrity.meetmaster.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meets", indexes = @Index(name = "idx_meets_active", columnList = "active"))
public class Meet {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "meet_participants", joinColumns = @JoinColumn(name = "meet_id"))
    @Column(name = "participant")
    private Set<String> participants = new HashSet<>();

    @Column(nullable = false)
    private String creator;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private long createTime;

    @PrePersist
    private void prePersist() {
        createTime = Instant.now().getEpochSecond(); // TODO: check if this is correct, as persist might fuck with edits.
    }

    public Meet() {}

    public Meet(String id, String name, Set<String> participants, String creator, boolean active) {
        this.id = id;
        this.name = name;
        this.participants = participants != null ? participants : new HashSet<>();
        this.creator = creator;
        this.active = active;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<String> getParticipants() { return participants; }
    public void setParticipants(Set<String> participants) { this.participants = participants; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public long getCreateTime() { return createTime; }
}
