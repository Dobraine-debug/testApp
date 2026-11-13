package se.yrgo.libraryapp.services;

import org.pac4j.core.credentials.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.entities.UserId;

import javax.inject.Inject;
import java.util.UUID;

public class SessionService {
    private SessionDao sessionDao;
    private PasswordEncoder encoder;

    @Inject
    public SessionService(SessionDao sessionDao, PasswordEncoder encoder) {
        this.sessionDao = sessionDao;
        this.encoder = encoder;
    }

    public UUID create(UserId userId){
        return sessionDao.create(userId);
    }

    public void delete(UUID session){
        sessionDao.delete(session);
    }

    public UserId validate(UUID session){
        return sessionDao.validate(session);
    }
}
