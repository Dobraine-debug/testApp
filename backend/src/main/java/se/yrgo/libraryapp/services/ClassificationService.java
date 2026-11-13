package se.yrgo.libraryapp.services;

import org.pac4j.core.credentials.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.dao.ClassificationDao;
import se.yrgo.libraryapp.entities.DdsClassification;

import javax.inject.Inject;
import java.beans.Encoder;
import java.util.List;

public class ClassificationService {
    private ClassificationDao classificationDao;
    private PasswordEncoder encoder;

    @Inject
    public ClassificationService(ClassificationDao classificationDao, PasswordEncoder encoder) {

        this.classificationDao = classificationDao;
        this.encoder = encoder;
    }

    public List<DdsClassification> getAvailable() {
        return classificationDao.getAvailable();
    }
}
