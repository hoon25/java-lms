package nextstep.session.domain;

import nextstep.common.domain.BaseDomain;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class Admission extends BaseDomain {
    private Long id;
    private Long studentId;
    private Long sessionId;

    public Admission(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Long studentId, Long sessionId) {
        super(createdAt, updatedAt);
        this.id = id;
        this.studentId = studentId;
        this.sessionId = sessionId;
    }

    public Admission(NsUser student, Session session) {
        this.studentId = student.getId();
        this.sessionId = session.getId();
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getSessionId() {
        return sessionId;
    }
}
