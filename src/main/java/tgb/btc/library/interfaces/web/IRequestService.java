package tgb.btc.library.interfaces.web;

import org.springframework.http.ResponseEntity;
import tgb.btc.library.vo.web.RequestHeader;
import tgb.btc.library.vo.web.RequestParam;

import java.util.List;

public interface IRequestService {

    <T> ResponseEntity<T> post(String url, List<RequestParam> params, Class<T> clazz);

    <T> ResponseEntity<T> get(String url, RequestHeader requestHeader, RequestParam... requestParams);
}
