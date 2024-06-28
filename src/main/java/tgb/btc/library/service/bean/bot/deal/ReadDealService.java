package tgb.btc.library.service.bean.bot.deal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.repository.bot.deal.ReadDealRepository;

import java.util.List;

@Service
public class ReadDealService implements IReadDealService {

    private ReadDealRepository readDealRepository;

    @Autowired
    public void setReadDealRepository(ReadDealRepository readDealRepository) {
        this.readDealRepository = readDealRepository;
    }

    @Override
    public Deal findByPid(Long pid) {
        return readDealRepository.findByPid(pid);
    }

    @Override
    public List<Deal> getDealsByPids(List<Long> pids) {
        return readDealRepository.getDealsByPids(pids);
    }

    @Override
    public List<Long> getListNewDeal(Long chatId, DealStatus dealStatus) {
        return readDealRepository.getListNewDeal(chatId, dealStatus);
    }

    @Override
    public List<Long> getPaidDealsPids() {
        return readDealRepository.getPaidDealsPids();
    }

    @Override
    public boolean dealsByUserChatIdIsExist(Long chatId, DealStatus dealStatus, Long countDeals) {
        return readDealRepository.dealsByUserChatIdIsExist(chatId, dealStatus, countDeals);
    }

    @Override
    public String getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType,
            CryptoCurrency cryptoCurrency) {
        return readDealRepository.getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency(chatId, dealType, cryptoCurrency);
    }

    @Override
    public void findAllByDealStatusNot(DealStatus dealStatus) {
        readDealRepository.findAllByDealStatusNot(dealStatus);
    }

}
