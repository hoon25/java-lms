package nextstep.session.domain;

import nextstep.common.domain.BaseDomain;
import nextstep.users.domain.NsUser;

import java.time.LocalDate;
import java.time.LocalDateTime;


public abstract class Session extends BaseDomain implements Sessionable {
    private static final SessionStatus DEFAULT_SESSION_STATUS = SessionStatus.PREPARING;
    private static final SessionRecruitStatus DEFAULT_RECRUIT_STATUS = SessionRecruitStatus.CLOSED;

    private Long id;
    private Long creatorId;

    private SessionDate sessionDate;

    private SessionImage sessionImage;

    private SessionStatus sessionStatus;
    private SessionRecruitStatus sessionRecruitStatus;
    private SessionType sessionType;
    protected Enrollments enrollments = new Enrollments();

    public Session(Long creatorId, LocalDate startDate, LocalDate endDate, SessionImage sessionImage, SessionType sessionType) {
        super();
        this.creatorId = creatorId;
        this.sessionDate = new SessionDate(startDate, endDate);
        this.sessionImage = sessionImage;
        this.sessionStatus = DEFAULT_SESSION_STATUS;
        this.sessionRecruitStatus = DEFAULT_RECRUIT_STATUS;
        this.sessionType = sessionType;
    }

    public Session(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Long creatorId, LocalDate startDate, LocalDate endDate, SessionImage sessionImage, SessionStatus sessionStatus, SessionRecruitStatus sessionRecruitStatus, SessionType sessionType) {
        super(createdAt, updatedAt);
        this.id = id;
        this.creatorId = creatorId;
        this.sessionDate = new SessionDate(startDate, endDate);
        this.sessionImage = sessionImage;
        this.sessionStatus = sessionStatus;
        this.sessionRecruitStatus = sessionRecruitStatus;
        this.sessionType = sessionType;
    }

    @Override
    public void enroll(NsUser user) {
        validateStatus();
        validateCommonEnroll(user);
        enrollments.add(user, this);
    }

    private void validateStatus() {
        if (sessionStatus == SessionStatus.END) {
            throw new IllegalStateException("종료된 강의는 신청 불가능합니다.");
        }
        if (sessionRecruitStatus != SessionRecruitStatus.OPEN) {
            throw new IllegalStateException("모집중인 강의만 신청 가능합니다.");
        }
    }

    abstract protected void validateCommonEnroll(NsUser nsUser);

    @Override
    public int enrolledNumber() {
        return enrollments.enrolledNumber();
    }

    @Override
    public void changeStatus(SessionStatus status) {
        sessionStatus = status;
    }

    public void changeRecruit(SessionRecruitStatus recruitStatus) {
        if (recruitStatus.equals(SessionRecruitStatus.OPEN)) {
            SessionRecruitStatus.enableOpen(sessionStatus);
        }
        this.sessionRecruitStatus = recruitStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public SessionDate getSessionDate() {
        return sessionDate;
    }

    public SessionImage getSessionImage() {
        return sessionImage;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public SessionRecruitStatus getSessionRecruitStatus() {
        return sessionRecruitStatus;
    }

    public SessionType getSessionType() {
        return sessionType;
    }
}
