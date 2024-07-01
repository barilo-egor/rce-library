package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Contact;
import tgb.btc.library.interfaces.service.bean.bot.IContactService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.ContactRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
@Transactional
public class ContactService extends BasePersistService<Contact> implements IContactService {
    private ContactRepository contactRepository;

    @Autowired
    public void setContactRepository(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    protected BaseRepository<Contact> getBaseRepository() {
        return contactRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }
}

