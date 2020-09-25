/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.coldzera.demo4j;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class 给老婆的 {
    public static void main(String[] args) {

        // 这里有一个问题，需求是，在 list1 和 list2 的基础上要得到 addList 和 updateList，
        // addList 里面的内容是，list1 name 存在，list2 不存在
        // updateList 的内容是，list1 和 list2 name 都存在，但 value 值不一样
        List<Env> list1 = new ArrayList<>();
        list1.add(new Env("A", "a2"));
        list1.add(new Env("B", "b1"));
        List<Env> list2 = new ArrayList<>();
        list2.add(new Env("A", "a1"));
        list2.add(new Env("JOB", "j1"));
        list2.add(new Env("JOB", "j2"));
        List<Env> addList = new ArrayList<>(); // new Env("B","b1","STRING")
        List<Env> updateList = new ArrayList<>(); // new Env("A","a2","STRING")

        // Map<String, Env> map1 = list1.stream().collect(Collectors.toMap(Env::getName, Function.identity()));
        // Map<String, Env> map2 = list2.stream().collect(Collectors.toMap(Env::getName, Function.identity()));
        //
        // map1.keySet();

        MultiValueMap<String, Env> map1 = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> map2 = new LinkedMultiValueMap<>();
        for (Env env : list1) {
            map1.add(env.getName(), env);
        }

        for (Env env : list2) {
            map2.add(env.getName(), env.getValue()); // TODO 只需要list2的value（由updateList结果示例推测出的[new
                                                     // Env("A","a2","STRING")] ）
        }

        // 取addList
        HashSet<String> addKeySet = new HashSet<>(map1.keySet());
        addKeySet.removeAll(map2.keySet());
        addKeySet.forEach(key -> addList.addAll(map1.get(key)));

        // 取updateList
        HashSet<String> updateKeySet = new HashSet<>(map1.keySet());
        updateKeySet.retainAll(map2.keySet());
        updateKeySet.forEach(key -> updateList.addAll(map1.get(key)
            .stream()
            .filter(env -> !map2.get(key).contains(env.getValue()))
            .collect(Collectors.toList()))); // 判断同一个key list1的value 不在 list2 value集中
    }
}

class Env {
    String name;

    String value;

    Env(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
