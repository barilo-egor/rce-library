package tgb.btc.library.interfaces.service.bot;

import tgb.btc.library.bean.bot.Contact;

import java.util.List;

public interface IContactService {

    List<Contact> findAll();
}
