package nextstep.session.domain;

import nextstep.payments.domain.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static nextstep.session.domain.fixture.SessionImageFixture.sessionImageFixture;
import static nextstep.users.domain.fixture.NsUserFixture.STUDENT_1;
import static nextstep.users.domain.fixture.NsUserFixture.STUDENT_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaidSessionTest {
    LocalDate today;

    @BeforeEach
    void setup() {
        // given
        today = LocalDate.now();
    }

    @Test
    @DisplayName("수강신청 정원 검증 / 유료 정원 2명, 2명 신청 / 성공")
    void 수강신청_유료_성공() {
        // given
        PaidSession session = PaidSession.create(1L, today, today.plusDays(1), sessionImageFixture, 2, 1000L);
        session.changeRecruit(SessionRecruitStatus.OPEN);
        STUDENT_1.addPayment(new Payment(1000L, session, STUDENT_1));
        STUDENT_2.addPayment(new Payment(1000L, session, STUDENT_2));

        // when
        session.enroll(STUDENT_1);
        session.enroll(STUDENT_2);

        // then
        assertThat(session.enrolledNumber()).isEqualTo(2);
    }

    @Test
    @DisplayName("수강신청 정원 검증 / 유료 정원 1명, 2명 신청 / IllegalStateException")
    void 수강신청_유료_정원초과_실패() {
        // given
        PaidSession session = PaidSession.create(1L, today, today.plusDays(1), sessionImageFixture, 1, 1000L);
        session.changeRecruit(SessionRecruitStatus.OPEN);
        STUDENT_1.addPayment(new Payment(1000L, session, STUDENT_1));
        STUDENT_2.addPayment(new Payment(1000L, session, STUDENT_2));

        // when
        session.enroll(STUDENT_1);

        // then
        assertThatThrownBy(() -> session.enroll(STUDENT_2))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("수강신청 결제 검증 / 결제금액 같은 유료강의 / 성공")
    void 수강신청_결제금액같은_유료강의_성공() {
        // given
        PaidSession session = PaidSession.create(1L, today, today.plusDays(1), sessionImageFixture, 1, 1000L);
        session.changeRecruit(SessionRecruitStatus.OPEN);
        STUDENT_1.addPayment(new Payment(1000L, session, STUDENT_1));

        // when
        session.enroll(STUDENT_1);

        // then
        assertThat(session.enrolledNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("수강신청 결제 검증 / 결제금액 다른 유료강의 / IllegalArgumentException")
    void 수강신청_결제금액_다른_유료강의_실패() {
        // given
        PaidSession session = PaidSession.create(1L, today, today.plusDays(1), sessionImageFixture, 1, 1000L);
        session.changeRecruit(SessionRecruitStatus.OPEN);
        STUDENT_1.addPayment(new Payment(1200L, session, STUDENT_1));

        // expect
        assertThatThrownBy(() -> session.enroll(STUDENT_1))
                .isInstanceOf(IllegalArgumentException.class);
    }

}