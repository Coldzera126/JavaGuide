
package com.coldzera.demo4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListTest2 {
    public static void main(String[] args) {

        // 根据截图获取的初始情况，目标为更新list1到list2
        List<Env2> list1 = new ArrayList<>();
        list1.add(new Env2("A", "a2", "STRING"));
        list1.add(new Env2("B", "b1", "STRING"));
        List<Env2> list2 = new ArrayList<>();
        list2.add(new Env2("A", "a1", "STRING", 1));
        list2.add(new Env2("M", "m", "STRING", 1));
        list2.add(new Env2("A", "a1", "STRING", 2));

        // 思路：先更新 后新增（避免重复更新）
        Map<String, Env2> map1 =
            list1.stream().collect(Collectors.toMap(Env2::getName, Function.identity(), (v1, v2) -> v2));

        HashSet<String> addKeySet = new HashSet<>(map1.keySet());
        // 1.更新
        String tempName;
        for (Env2 env2 : list2) {
            tempName = env2.getName();
            if (map1.containsKey(tempName)) { // TODO map1 value可能为null，需要考虑这种情况是否需要更新 null value 到list2
                env2.setValue(map1.get(tempName).getValue());
                addKeySet.remove(tempName); // 筛选出需要add的 name
            }
        }

        // 2.新增
        // 找出list2的所有jobId,根据addKeySet做新增
        Set<Integer> jobIdSet = list2.stream().map(Env2::getJobId).collect(Collectors.toSet()); // TODO
        for (String key : addKeySet) {
            Env2 env2 = map1.get(key);
            for (Integer jobId : jobIdSet) {
                list2.add(new Env2(env2.getName(), env2.getValue(), env2.getType(), jobId));
            }
        }
    }
}

class Env2 {
    String name;

    String value;

    String type;

    Integer jobId;

    Env2(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    Env2(String name, String value, String type, Integer jobId) {
        this(name, value, type);
        this.jobId = jobId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }
}
