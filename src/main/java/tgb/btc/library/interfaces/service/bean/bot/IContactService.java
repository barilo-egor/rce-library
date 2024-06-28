package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.Contact;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IContactService extends IBasePersistService<Contact> {

    List<Contact> findAll();
}
