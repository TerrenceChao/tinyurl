package com.lintcode.tinyurl.domain;

import com.lintcode.tinyurl.domain._entity.Record;
import com.lintcode.tinyurl.domain._repository.MockDBRepository;
import com.lintcode.tinyurl.util.Converter;
import com.lintcode.tinyurl.util.MsgDigest;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

//@Service
public class TransformService {

    private final Integer DB_CODE_LENGTH = 2;
    private final Integer DB_ID_LENGTH = 4;
    private final Integer TINY_URL_LENGTH = DB_CODE_LENGTH + DB_ID_LENGTH;

    private MsgDigest msgDigest;
    private MockDBRepository repo;

//    @Autowired
    public TransformService(MockDBRepository repo) throws NoSuchAlgorithmException {
        this.repo = repo;
        msgDigest = MsgDigest.getInstance();
    }

    /**
     * 透過 message digest 擷取 longUrl 的摘要，產生 DB 的部署代碼
     * @param longUrl longUrl
     * @return DB 的部署代碼
     */
    public String genDBCode(String longUrl) {
        Integer total = MockDBRepository.TOTAL_DB_COUNT;
        String md5 = msgDigest.stringMD5(longUrl);

        String mod = Converter.mod(md5, total);
        while (mod.length() < DB_CODE_LENGTH) {
            mod = "0" + mod;
        }
        System.out.println("INFO: result DB Code: " + mod + "\t\t(total DB amount = " + total + ", md5(longUrl) = " + md5 + ") ");

        return mod;
    }

    /**
     * entireID 由 DB 的部署代碼 ＋ rowID 組成
     * @return (no. Nth DB + rowID)
     */
    public Integer genEntireID(String dbCode, Record record) {
        // dbCode 有 2 位數 (DB_CODE_LENGTH)
        while (dbCode.length() < DB_CODE_LENGTH) {
            dbCode = "0" + dbCode;
        }

        // rowID 有 4 位數 (DB_ID_LENGTH)
        String rowID = record.getId().toString();
        while (rowID.length() < DB_ID_LENGTH) {
            rowID = "0" + rowID;
        }

        return Integer.parseInt(dbCode + rowID);
    }

    /**
     * 將 longUrl 轉換成 tinyUrl
     * @param longUrl longUrl
     * @return tinyUrl
     */
    public String longUrlToTinyUrl(String longUrl) {
        // msg digest, make longUrl lives in the DB(the Nth DB) in DBCluster
        String dbCode = genDBCode(longUrl);

        // find longUrl's record; Or create new record for new-longUrl if not found
        Record target = repo.findOrCreateRecord(dbCode, longUrl);
        Integer entireID = genEntireID(dbCode, target);

        // entireID (no. Nth DB + ID) -> tinyUrl and saved
        String tinyUrl = Converter.idToTinyUrl(entireID);
        // System.out.println("entireID: " + entireID + " >>> idToTinyUrl >>> " + tinyUrl);

        repo.updateTinyUrl(dbCode, target.getId(), tinyUrl);

        return tinyUrl;
    }

    /**
     * 將 tinyUrl 轉換成 longUrl
     * @param tinyUrl tinyUrl
     * @return longUrl
     */
    public String tinyUrlToLongUrl(String tinyUrl) {
        // tinyUrl -> entireID (no. Nth DB + ID)
        Integer entireID = Converter.tinyUrlToID(tinyUrl);
        // System.out.println("tinyUrl: " + tinyUrl + " >>> tinyUrlToID >>> " + entireID);

        // 從 entireID 解析出 DB 的部署代碼, rowID
        Map<String, String> result = parseEntireID(entireID);

        // find the no. Nth DB in DBCluster and return tinyUrl by "entireID".
        String dbCode = result.get("dbCode");
        Integer rowID = Integer.parseInt(result.get("rowID"));
        return repo.getLongUrl(dbCode, rowID);
    }

    /**
     * 從 entireID 解析出 DB 的部署代碼, rowID
     * @param entireID (no. Nth DB + ID)
     * @return
     */
    private Map<String, String> parseEntireID(Integer entireID) {
        Map<String, String> map = new HashMap<>();

        String strEntireID = entireID.toString();
        // entireID 應該有 6 位數
        while (strEntireID.length() < TINY_URL_LENGTH) {
            strEntireID = "0" + strEntireID;
        }

        // System.out.println(" strEntireID >>> " + strEntireID);

        String dbCode = strEntireID.substring(0, DB_CODE_LENGTH);
        String rowID = strEntireID.substring(DB_CODE_LENGTH, TINY_URL_LENGTH);

        map.put("dbCode", dbCode);
        map.put("rowID", rowID);

        return map;
    }
}
