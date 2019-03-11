package com.sljl.core.utils;

import com.xfs.core.dto.PlatformResult;
import com.xfs.core.exception.PlatformException;
import com.xfs.core.exception.ResponseWithErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;


/**
 * @author L.Y.F
 */
public class PlatformDataUtil {

    private static Logger log = LoggerFactory.getLogger(PlatformDataUtil.class);

    /**
     * 与 {@link #resolveData(PlatformResult, Map)} 类似，唯一区别是针对 {@code
     * PlatformResult<Void>} 这类不需要返回值的result进行后续处理，并且平台返回null时不额外打印日志
     *
     * @param result 平台结果对象，只能能为void
     * @param paramMap 参数集，主要用于打印日志
     *
     * @throws PlatformException
     */
    public static void resolveVoid(PlatformResult<Void> result, Map<String, ?> paramMap) throws PlatformException {
        resolveVoid(result, paramMap, new int[]{});
    }

    /**
     * 与 {@link #resolveData(PlatformResult, Map, Object, int...)} 类似，唯一区别是针对
     * {@code PlatformResult<Void>} 这类不需要返回值的result进行后续处理，并且平台返回null时不额外打印日志
     *
     * @param result 平台结果对象
     * @param paramMap 参数集，主要用于打印日志
     * @param ignoreCodes 用于忽略的bizCode，一般做幂等操作使用
     *
     * @throws PlatformException
     */
    public static void resolveVoid(PlatformResult<Void> result, Map<String, ?> paramMap, int... ignoreCodes) throws PlatformException {
        resolveData(result, paramMap, true, null, ignoreCodes);
    }

    /**
     * 解析平台结果，并直接返回数据，void请不要使用此方法，当data为null时会打印日志，当result不ok时，
     * 抛出PlatformException
     *
     * @param result 平台结果对象
     * @param paramMap 参数集，主要用于打印日志
     *
     * @return
     *
     * @throws PlatformException
     */
    public static <T> T resolveData(PlatformResult<T> result, Map<String, ?> paramMap) throws PlatformException {
        return resolveData(result, paramMap, null);
    }

    /**
     * 解析平台结果，并直接返回数据，void请不要使用此方法，当data为null时会打印日志，当result不ok时，
     * 抛出PlatformException，
     * 如果指定了ignoreCodes，对应code不会抛出异常，但是必须同时指定defaultIfIgnore值
     *
     * @param result 平台结果对象
     * @param paramMap 参数集，主要用于打印日志
     * @param defaultIfIgnore 如果传入了ignoreCodes，则该值必须，并且该值存在时，必须含有ignoreCodes
     * @param ignoreCodes 用于忽略的bizCode，一般做幂等操作使用
     *
     * @return
     *
     * @throws PlatformException
     */
    public static <T> T resolveData(PlatformResult<T> result, Map<String, ?> paramMap, T defaultIfIgnore, int... ignoreCodes) throws PlatformException {
        return resolveData(result, paramMap, false, defaultIfIgnore, ignoreCodes);
    }

    /**
     * 非公开的解析平台结果，并进行处理的方法
     *
     * @param result 平台返回结果
     * @param paramMap 参数Map，用于打印日志
     * @param dataIsVoid 结果的data是否是void型
     * @param defaultIfIgnore 当T不是void，并且指定了ignoreCodes时的默认值
     * @param ignoreCodes 可以忽略的平台错误code，一般用于幂等操作，对于非void返回值，需要配合defaultIfIgnore使用
     *
     * @return
     *
     * @throws PlatformException
     */
    private static <T> T resolveData(PlatformResult<T> result, Map<String, ?> paramMap, boolean dataIsVoid, T defaultIfIgnore, int... ignoreCodes) throws PlatformException {
        // 如果返回值不是void类型，则需要校验ignore参数
        if (!dataIsVoid) {
            if (!((null != defaultIfIgnore && ignoreCodes.length > 0) || (null == defaultIfIgnore && ignoreCodes.length < 1))) {
                throw new PlatformException("返回值非void时，defaultIfIgnore与ignoreCodes必须同时存在，或者同时不存在，当前值，defaultIfIgnore: " + defaultIfIgnore + " , ignoreCodes: " + Arrays.toString(ignoreCodes));
            }
        }
        T data = result.getData();
        if (!result.isOk()) {
            int bizCode = result.getBizCode();
            boolean ignoreError = false;
            for (int code : ignoreCodes) {
                if (code == bizCode) {
                    ignoreError = true;
                    if (!dataIsVoid)
                        data = defaultIfIgnore;
                    log.info("忽略平台结果 {0}，错误code为 {1,number,#}, 并赋data为 {2}", result, code, defaultIfIgnore);
                    break;
                }
            }
            if (!ignoreError)
                throw new ResponseWithErrorException(result, paramMap);
        }
        // 在result不是void型，并且平台返回值为null时，记录日志
        if (null == data && !dataIsVoid) {
            StringBuilder sb = new StringBuilder("Platform response with null, paramMap is ").append(paramMap).append(", stack ");
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            int layers = 4;
            for (StackTraceElement elem : stacks) {
                if (layers < 0)
                    break;
                sb.append("\n    at ").append(elem.getClassName());
                sb.append(".").append(elem.getMethodName());
                sb.append("(").append(elem.getLineNumber()).append(")");
                layers--;
            }
            log.warn(sb.toString());
        }
        return data;
    }

}
