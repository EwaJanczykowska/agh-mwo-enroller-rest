package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

    private Session session;

    public MeetingService() {
        session = DatabaseConnector.getInstance().getSession();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting ORDER BY LOWER(title)";
        Query query = session.createQuery(hql);
        return query.list();
    }

    public Meeting findById(long id) {
        return (Meeting) session.get(Meeting.class, id);
    }

    public Meeting add(Meeting meeting) {
        Transaction transaction = this.session.beginTransaction();
        session.save(meeting);
        transaction.commit();
        return meeting;
    }

    public Meeting updateMeeting(Meeting meeting) {
        Transaction transaction = this.session.beginTransaction();
        session.update(meeting);
        transaction.commit();
        return meeting;
    }

    public void delete(Meeting meeting) {
        Transaction transaction = this.session.beginTransaction();
        session.delete(meeting);
        transaction.commit();
    }

    public Collection<Meeting> findMeetings(String title, String description) {
        if (title==null) {
            title = "";
        }
        if (description==null) {
            description = "";
        }
        String hql = "FROM Meeting WHERE title LIKE :titleParam AND description LIKE :descriptionParam ORDER BY LOWER(title)";
        Query query = session.createQuery(hql);
        query.setString("titleParam", "%" + title + "%");
        query.setString("descriptionParam", "%" + description + "%");
        return query.list();
    }

    public Collection<Meeting> findByParticipant(String participant) {
        String hql = "SELECT m FROM Meeting AS m INNER JOIN m.participants AS mp WHERE mp.login = :participantParam";
        Query query = session.createQuery(hql);
        query.setString("participantParam", participant);
        return query.list();
    }
}
