package com.sljl.core.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * 随机工具类
 *
 * @author L.Y.F
 */
public class RandomUtil {

    /**
     * 获取两个区域内的随机数
     *
     * @param beginNum
     * @param endNum
     *
     * @return
     */
    public static int getRandRange(int beginNum, int endNum) {
        Random random = new Random();
        int s = random.nextInt(endNum) % (endNum - beginNum + 1) + beginNum;
        return s;
    }

    /**
     * 从自定义种子池中随即获取一个数值
     *
     * @param seedPools
     *
     * @return
     */
    public static <T> T getSeedPoolsRandom(List<T> seedPools) {
        if (CollectionUtils.isNotEmpty(seedPools)) {
            int poolSize = seedPools.size();
            return seedPools.get(getRandRange(0, poolSize - 1));
        }
        return null;
    }

}
