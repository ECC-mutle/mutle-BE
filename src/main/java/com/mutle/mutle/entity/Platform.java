package com.mutle.mutle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;

@Entity
@Table(name="platforms")
@Getter
@NoArgsConstructor

@AllArgsConstructor
@Builder
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platform_id")
    private Long platformId;

    @Column(name = "platform_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlatformName platformName;

    @Column(name = "platform_nickname", length = 50)
    private String platformNickname;

    @ManyToOne
    @JoinColumn(name="id")
    private User user;

    @CreationTimestamp
    @Column(name = "platform_created_at", nullable = false, updatable = false)
    private Timestamp platformCreatedAt;

    @UpdateTimestamp
    @Column(name="platform_updated_at", nullable = false)
    private Timestamp platformUpdatedAt;


}
