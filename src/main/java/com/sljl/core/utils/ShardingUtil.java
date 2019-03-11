package com.sljl.core.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.xfs.core.enums.ShardingPolicyEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据库分表工具类
 *
 * @author L.Y.F
 */
public class ShardingUtil {

    // Hash方式分表的基数
    private static final int SHARDING_VALUE16 = 16;
    // Hash方式分表的基数
    private static final int SHARDING_VALUE256 = 256;
    //	// 滚动增长方式分表的基数
    //	private static final long MAX_CNT_VALUE = 4000000l;
    // 系统默认分隔符
    private static final String default_seperator = "_";

    private static int getShardingValue16(String uniqueId) {
        return (uniqueId.hashCode() & 0x7FFFFFFF) % SHARDING_VALUE16;
    }

    private static int getShardingValue256(String uniqueId) {
        return (uniqueId.hashCode() & 0x7FFFFFFF) % SHARDING_VALUE256;
    }

    private static String joinSubKeyBy(Object... objects) {
        if (null != objects) {
            return Joiner.on(default_seperator).join(ImmutableList.builder().add(objects).build());
        } else {
            return "";
        }
    }

    /**
     * 数据库分表
     *
     * @param tableName
     * @param shardingPolicy
     * @param uniqueId
     *
     * @return
     */
    public static String getShardingTable(String tableName, ShardingPolicyEnum shardingPolicy, long uniqueId) {
        String shardingTable = "";
        if (StringUtils.isNotBlank(tableName)) {
            if (ShardingPolicyEnum.SHARDING_FOR_HASH16 == shardingPolicy) {
                // FIXME 临时写法这种散列方式分表的算法要优化，保证每次扩展16张表，可以根据ID自动定位到新扩展的表中
                return joinSubKeyBy(tableName, getShardingValue16(uniqueId + ""));
            } else if (ShardingPolicyEnum.SHARDING_FOR_HASH256 == shardingPolicy) {
                return joinSubKeyBy(tableName, getShardingValue256(uniqueId + ""));
            } else if (ShardingPolicyEnum.SHARDING_FOR_INCREASE == shardingPolicy) {
                // FIXME 临时写法这种滚动增长方式要根据具体情况控制每张表的实际数据量大概在MAX_CNT_VALUE个左右，超过后可以自动定位到新表
                return joinSubKeyBy(tableName, 1);
            }
        }
        return shardingTable;
    }

}
