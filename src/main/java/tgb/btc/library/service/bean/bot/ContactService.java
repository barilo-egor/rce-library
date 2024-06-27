package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Contact;
import tgb.btc.library.interfaces.service.bot.IContactService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.ContactRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
public class ContactService extends BasePersistService<Contact> implements IContactService {
    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(BaseRepository<Contact> baseRepository, ContactRepository contactRepository) {
        super(baseRepository);
        this.contactRepository = contactRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }
}

