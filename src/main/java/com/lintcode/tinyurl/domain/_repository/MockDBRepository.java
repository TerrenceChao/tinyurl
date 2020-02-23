package com.lintcode.tinyurl.domain._repository;

import com.lintcode.tinyurl.domain._entity.Record;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class MockDBRepository {
    // 目前 DB Servers 的最大數量
    public static final Integer TOTAL_DB_COUNT = 60;

    private Map<String, List<Record>> dbCluster;

    public MockDBRepository() {
        dbCluster = new HashMap<>();
        initDBCluster(TOTAL_DB_COUNT);
    }

    public MockDBRepository(int total) {
        dbCluster = new HashMap<>();
        initDBCluster(total);
    }

    /**
     * 初始化 dbCluster (包含每個 DB 的部署代碼)
     * @param total total
     */
    private void initDBCluster(int total) {
        int digits = getDigits(total);
        for (int i = 0; i < total; i++) {
//            System.out.println("db code >>> " + genDBCode(i, digits));
            dbCluster.put(setDBCode(i, digits), new ArrayList<>());
        }
//        System.out.println("\n\n\n");
    }

    /**
     * 產生該 DB 部署代碼
     * @param num num
     * @param digits digits
     * @return
     */
    private String setDBCode(int num, int digits) {
        String code = Integer.toString(num);
        while (code.length() < digits) {
            code = "0" + code;
        }
        return code;
    }

    /**
     * 回傳數字總共有幾位數 (base 10)
     * @param number number
     * @return 位數個數
     */
    private int getDigits(int number) {
        int digits = 0;
        while (number > 0) {
            number /= 10;
            digits++;
        }

        return digits;
    }


    /**
     * 在特定 DB 上回傳舊的記錄, 若沒有則建立並回傳新紀錄
     * @param dbCode 特定 DB 的部署代碼
     * @param longUrl longUrl
     * @return rowID
     */
    public Record findOrCreateRecord(String dbCode, String longUrl) {
        List<Record> theDBRecords = dbCluster.get(dbCode);

        // seeking
        List<Record> result = theDBRecords.stream()
                .filter(record -> record.getLongUrl().equals(longUrl))
                .collect(Collectors.toList());

        // return rowID if found
        if (result.size() == 1) {
            return result.get(0);
        }

        // else create a new record
        int autoIncrement = theDBRecords.size() + 1;
        Record newRecord = new Record().setId(autoIncrement).setLongUrl(longUrl);
        theDBRecords.add(newRecord);

        return newRecord;
    }

    /**
     * 更新特定 DB 中的某筆紀錄的 shortUrl
     * @param dbCode dbCode
     * @param rowID rowID
     * @param tinyUrl tinyUrl
     */
    public void updateTinyUrl(String dbCode, Integer rowID, String tinyUrl) {
        List<Record> theDBRecords = dbCluster.get(dbCode);
        theDBRecords.stream()
                .filter(record -> record.getId() == rowID)
                .peek(record -> record.setTinyUrl(tinyUrl));
    }

    /**
     * 取得特定 DB 中的某筆紀錄的 longUrl
     * @param dbCode dbCode
     * @param rowID rowID
     */
    public String getLongUrl(String dbCode, Integer rowID) {
        List<Record> theDBRecords = dbCluster.get(dbCode)
                .stream()
                .filter(record -> record.getId() == rowID)
                .collect(Collectors.toList());

        if (theDBRecords.size() == 0) {
            return "找不到你要的 tinyUrl";
        }

        return theDBRecords.get(0).getLongUrl();
    }
}
