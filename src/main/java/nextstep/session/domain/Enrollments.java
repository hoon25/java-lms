package nextstep.session.domain;

import nextstep.users.domain.NsUser;

import java.util.ArrayList;
import java.util.List;

public class Enrollments {

    private final List<Enrollment> enrollments = new ArrayList<>();

    public Enrollments() {
    }

    public Enrollments(List<Enrollment> enrollments) {
        this.enrollments.addAll(enrollments);
    }

    public void add(NsUser user) {
        enrollments.add(new Enrollment(user));
    }

    public int enrolledNumber() {
        return this.enrollments.size();
    }
}