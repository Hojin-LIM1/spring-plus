package org.example.expert.domain.log;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;


// 음 로그 테이블을 만들라고 했으니 entity를 작성해주고
// 작성할거 : id, 로그내용, 생성일
// 생성자는 메세지만

@Entity
@Getter
@NoArgsConstructor
@Table(name = "log")
@EntityListeners(AuditingEntityListener.class)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Log(String message) {
        this.message = message;
    }
}
