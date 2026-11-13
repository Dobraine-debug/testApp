package se.yrgo.libraryapp.services;

import org.pac4j.core.credentials.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;

import javax.inject.Inject;
import java.util.List;

public class RoleService {
    private RoleDao roleDao;
    private PasswordEncoder encoder;

    @Inject
    public RoleService(RoleDao roleDao, PasswordEncoder encoder) {
        this.roleDao = roleDao;
        this.encoder = encoder;
    }

    public List<Role> get(UserId userId){
        return roleDao.get(userId);
    }
}
