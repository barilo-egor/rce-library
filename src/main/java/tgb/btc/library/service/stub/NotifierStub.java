package tgb.btc.library.service.stub;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import tgb.btc.api.web.INotifier;

import java.io.File;
import java.util.List;

/**
 * Заглушка для тестов. Настоящая реализация в rce.
 */
@Profile({"test", "web"})
@Service
public class NotifierStub implements INotifier {

    @Override
    public void notifyNewApiDeal(Long aLong) {
        // stub
    }

    @Override
    public void notifyDealAutoDeleted(Long aLong, Integer integer) {
        // stub
    }

    @Override
    public void notifyDealDeletedByAdmin(Long aLong) {
        // stub
    }

    @Override
    public void sendNotify(Long aLong, String s) {
        // stub
    }

    @Override
    public void sendLoginRequest(Long aLong) {
        // stub
    }

    @Override
    public void sendChatIdConfirmRequest(Long aLong) {
        // stub
    }

    @Override
    public void sendFile(List<Long> list, File file) {
        // stub
    }

    @Override
    public void notifyAdmins(String s) {
        // stub
    }

    @Override
    public void sendRequestToWithdrawDeal(String s, String s1, Long aLong) {
        // stub
    }

    @Override
    public void sendAutoWithdrawDeal(String s, String s1, Long aLong) {
        // stub
    }

    @Override
    public void sendRequestToWithdrawApiDeal(Long aLong) {
        // stub
    }

    @Override
    public void sendGreetingToNewDealRequestGroup() {
        // stub
    }

    @Override
    public void sendGreetingToNewAutoWithdrawalGroup() {
        // stub
    }

    @Override
    public void sendGreetingToNewApiDealRequestGroup(Long aLong) {
        // stub
    }

    @Override
    public void sendGoodbyeToNewApiDealRequestGroup(Long aLong, String s) {
        // stub
    }
}
