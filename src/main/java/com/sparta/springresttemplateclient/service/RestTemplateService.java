package com.sparta.springresttemplateclient.service;

import com.sparta.springresttemplateclient.dto.ItemDto;
import com.sparta.springresttemplateclient.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RestTemplateService {
    private final RestTemplate restTemplate;

    // RestTemplateBuilder의 build()를 사용하여 RestTemplate을 생성합니다.
    public RestTemplateService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    //1개의 Item 불러오기
    public ItemDto getCallObject(String query) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7070") //서버입장의 서버 주소
                .path("/api/server/get-call-obj")
                .queryParam("query", query)
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);

        ResponseEntity<ItemDto> responseEntity = restTemplate.getForEntity(uri, ItemDto.class); //item에 대한 정보 받음

        log.info("statusCode = " + responseEntity.getStatusCode());

        return responseEntity.getBody(); //ItemDto값 반환
    }

    //여러 개의 Item 불러오기
    public List<ItemDto> getCallList() {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7070")
                .path("/api/server/get-call-list")//query-string 방식
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);

        //여러 개가 넘어오면 String으로 받아서 변환해야함
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        log.info("statusCode = " + responseEntity.getStatusCode());
        log.info("Body = " + responseEntity.getBody());

        //변환 메서드
        return fromJSONtoItems(responseEntity.getBody()); //getBody()에 String 데이터 담겨있음
    }

    public ItemDto postCall(String query) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7070")
                .path("/api/server/post-call/{query}") //pathVariable 방식
                .encode()
                .build()
                .expand(query)
                .toUri();
        log.info("uri = " + uri);

        User user = new User("Robbie", "1234");

        ResponseEntity<ItemDto> responseEntity = restTemplate.postForEntity(uri, user, ItemDto.class);

        log.info("statusCode = " + responseEntity.getStatusCode());

        return responseEntity.getBody();
    }

    public List<ItemDto> exchangeCall(String token) {
        return null;
    }


//JSON 데이터
//    {
//        "items":[
//        {"title":"Mac","price":3888000},
//        {"title":"iPad","price":1230000},
//        {"title":"iPhone","price":1550000},
//        {"title":"Watch","price":450000},
//        {"title":"AirPods","price":350000}
//	]
//    }
    //String으로 받아온 JSON 데이터 변환 메서드
    public List<ItemDto> fromJSONtoItems(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray items  = jsonObject.getJSONArray("items");
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (Object item : items) {
            ItemDto itemDto = new ItemDto((JSONObject) item); //ItemDto로 변환
            itemDtoList.add(itemDto);
        }

        return itemDtoList;
    }
}