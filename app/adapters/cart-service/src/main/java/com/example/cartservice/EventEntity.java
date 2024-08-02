package com.example.cartservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter
@Component
@ToString
@NoArgsConstructor
@AllArgsConstructor
class EventEntity implements EventUpdater {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cartId;
    private EventStatus eventStatus;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EventProductEntity> products = new ArrayList<>();

    @Override
    public void updateStatus(EventStatus status) {
        this.eventStatus = status;
    }

    enum EventStatus {
        PROCESS,
        COMPLETE,
        FAILED
    }
}

