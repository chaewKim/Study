package com.sparta.springresttemplateserver.dto;

import com.sparta.springresttemplateserver.entity.Item;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemResponseDto {
    private final List<Item> items = new ArrayList<>();

    public void setItems(Item item) {
        items.add(item);
    }

    //ItemResponseDto list형태를 반환하는 것도 있지만
    //클래스 내부에 자바 컬렉션만들어서 반환하는 방법도 있음
}