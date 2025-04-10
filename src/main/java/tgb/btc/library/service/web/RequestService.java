package tgb.btc.library.service.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tgb.btc.library.interfaces.web.IRequestService;
import tgb.btc.library.vo.web.ApiResponse;
import tgb.btc.library.vo.web.RequestHeader;
import tgb.btc.library.vo.web.RequestParam;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RequestService implements IRequestService {

    private final RestTemplate restTemplate;

    @Autowired
    public RequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> ResponseEntity<T> post(String url, List<RequestParam> params, Class<T> clazz) {
        return restTemplate.postForEntity(buildUrl(url, params), null, clazz);
    }

    @Override
    public <T, B> ResponseEntity<ApiResponse<T>> post(String url, RequestHeader requestHeader, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(requestHeader.getName(), requestHeader.getValue());
        HttpEntity<B> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});
    }

    @Override
    public <T, B> ResponseEntity<ApiResponse<T>> post(String url, RequestHeader requestHeader, RequestParam requestParam, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(requestHeader.getName(), requestHeader.getValue());
        HttpEntity<B> entity = new HttpEntity<>(headers);
        if (Objects.nonNull(requestParam)) {
            url = buildUrl(url, List.of(requestParam));
        }
        return restTemplate.exchange(url, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});
    }

    @Override
    public <T, B> ResponseEntity<ApiResponse<T>> post(String url, RequestHeader requestHeader, B body, Class<T> responseClazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(requestHeader.getName(), requestHeader.getValue());
        HttpEntity<B> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});
    }

    @Override
    public <T, B> ResponseEntity<ApiResponse<T>> delete(String url, RequestHeader requestHeader, Class<T> responseClazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(requestHeader.getName(), requestHeader.getValue());
        HttpEntity<B> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, new ParameterizedTypeReference<>() {});
    }

    @Override
    public <T, B> ResponseEntity<ApiResponse<T>> delete(String url, RequestHeader requestHeader, B body,
                                                        Class<T> responseClazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(requestHeader.getName(), requestHeader.getValue());
        HttpEntity<B> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, new ParameterizedTypeReference<>() {});
    }

    @Override
    public <T> ResponseEntity<ApiResponse<T>> get(String url, RequestHeader requestHeader,
                                                  ParameterizedTypeReference<ApiResponse<T>> responseClazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(requestHeader.getName(), requestHeader.getValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                responseClazz
        );
    }

    @Override
    public <T> ResponseEntity<ApiResponse<T>> get(String url, RequestHeader requestHeader, RequestParam requestParam,
                                                  Class<T> responseClazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(requestHeader.getName(), requestHeader.getValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                buildUrl(url, Objects.nonNull(requestParam) ? List.of(requestParam) : null),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );
    }

    @Override
    public <T> ResponseEntity<ApiResponse<T>> get(String url, List<RequestHeader> requestHeaders) {
        HttpHeaders headers = new HttpHeaders();
        requestHeaders.forEach(header -> headers.add(header.getName(), header.getValue()));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );
    }

    private String buildUrl(String url, List<RequestParam> params) {
        if (Objects.isNull(params) || params.isEmpty()) {
            return url;
        }
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        params.forEach(param -> uriComponentsBuilder.queryParam(param.getKey(), param.getValue()));
        return uriComponentsBuilder.build().toUriString();
    }
}
