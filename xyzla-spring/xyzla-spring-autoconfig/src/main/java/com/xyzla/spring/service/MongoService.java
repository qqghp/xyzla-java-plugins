package com.xyzla.spring.service;

import com.mongodb.bulk.BulkWriteResult;
import com.xyzla.spring.exception.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StopWatch;

import java.util.List;

public class MongoService {

    private static Logger logger = LoggerFactory.getLogger(MongoService.class);

    @Autowired
    MongoTemplate mongoTemplate;

    public <T> void saveBatch(List<T> list, Class<T> tClass) {
        if (list == null || list.size() < 1) {
            return;
        }
        String collectionName = getCollectionName(tClass);

        int C = 2000;
        int length = list.size();
        int remain = length % C;
        int count = length / C + (remain == 0 ? 0 : 1);
        for (int i = 0; i < count; ++i) {
            try {
                int fromIndex = i * C;
                int toIndex = i * C + C;

                if ((i + 1) == count & remain != 0) {// 最后一组,不足 C 条记录,有几条分几条
                    toIndex = i * C + remain;
                }
                List<T> tmp = list.subList(fromIndex, toIndex);

                BulkOperations bulkOp = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, collectionName);
                bulkOp.insert(tmp);
                BulkWriteResult bulkWriteResult = bulkOp.execute();

                Boolean flag = bulkWriteResult.getInsertedCount() == tmp.size();
                logger.debug("saveBatch {}, totalCard: {} , exec result: {}", collectionName, tmp.size(), flag);
            } catch (Exception ex) {
                logger.error("{}", ex);
            }
        }
    }


    /**
     * 移除 指定条件下的 卡用量数据
     *
     * @param usageDay     用量日期 yyyyMMdd
     * @param supplierCode 供应商 Code
     */
    public <T> void removeByUsageDayAndSupplierCode(String usageDay, String supplierCode, Class<T> tClass) {
        String collectionName = getCollectionName(tClass);

        StopWatch sw = new StopWatch();
        sw.start();
        Query query = new Query();
        query.addCriteria(Criteria.where("usage_day").is(usageDay).and("supplier_code").is(supplierCode));
        logger.info("remove {}", query.toString());
        long delBeforeCount = mongoTemplate.count(query, collectionName);
        mongoTemplate.remove(query, tClass);
        long delAfterCount = mongoTemplate.count(query, collectionName);
        sw.stop();

        logger.info("remove {} {} {}. 耗时: {} 秒, 记录数量 {} -> {}", supplierCode, usageDay, collectionName, sw.getTotalTimeSeconds(), delBeforeCount, delAfterCount);
    }


    /**
     * 移除 指定条件下的 卡用量数据
     *
     * @param source 用量来源;
     *               目前西安移动 PB FTP 在用, 这里传入的是 ftp 文件的原始名称
     */
    public <T> void removeBySource(String source, Class<T> tClass) {
        StopWatch sw = new StopWatch();
        sw.start();
        Query query = new Query();
        query.addCriteria(Criteria.where("source").is(source.replace(".zip", ".txt")));
        mongoTemplate.remove(query, tClass);
        sw.stop();
        logger.info("remove {} daily_session_usage. 耗时: {} 秒", source, sw.getTotalTimeSeconds());
    }

    public <T> String getCollectionName(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Document.class)) {
            throw new MongoException(String.format("Missing document annotation for class types, [class=%s]",
                    clazz.getSimpleName()));
        }
        Document document = clazz.getDeclaredAnnotation(Document.class);
        return document.collection();
    }
}
