package tgb.btc.library.interfaces.web;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import tgb.btc.library.vo.web.ApiResponse;
import tgb.btc.library.vo.web.RequestHeader;
import tgb.btc.library.vo.web.RequestParam;

import java.util.List;

public interface IRequestService {

    <T> ResponseEntity<T> post(String url, List<RequestParam> params, Class<T> clazz);

    <T, B> ResponseEntity<ApiResponse<T>> post(String url, RequestHeader requestHeader, Class<T> clazz);

    <T, B> ResponseEntity<ApiResponse<T>> post(String url, RequestHeader requestHeader, RequestParam requestParam, Class<T> clazz);

    <T, B> ResponseEntity<ApiResponse<T>> post(String url, RequestHeader requestHeader, B body, Class<T> clazz);

    <T, B> ResponseEntity<ApiResponse<T>> delete(String url, RequestHeader requestHeader, Class<T> responseClazz);

    <T, B> ResponseEntity<ApiResponse<T>> delete(String url, RequestHeader requestHeader, B body, Class<T> responseClazz);

    <T> ResponseEntity<ApiResponse<T>> get(String url, RequestHeader requestHeader,
                                           ParameterizedTypeReference<ApiResponse<T>> responseClazz);

    <T> ResponseEntity<ApiResponse<T>> get(String url, RequestHeader requestHeader,
                                           RequestParam requestParam, Class<T> clazz);

    <T> ResponseEntity<ApiResponse<T>> get(String url, List<RequestHeader> requestHeaders);
}
