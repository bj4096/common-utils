package com.sljl.core.signature;

import com.google.common.collect.Maps;
import com.sljl.core.utils.JsonUtil;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author L.Y.F
 * @create 2019-03-13 18:58
 */
public class SignatureUtils {

    /**
     * 时间戳，单位: ms
     */
    private String timestamp;
    /**
     * 流水号【防止重复提交】; (备注：针对查询接口，流水号只用于日志落地，便于后期日志核查； 针对办理类接口需校验流水号在有效期内的唯一性，以避免重复请求)
     */
    private String nonce;
    /**
     * 签名
     */
    private String signature;

    private static final byte XOR_TOKEN = (byte) 0xFFF;
    private static final String NOT_FOUND = "$_$";
    private static final String DELIMETER = "^_^";
    // 线下分配的值 客户端和服务端各自保存appId对应的appSecret
    private static final String appId = "";
    // 线下分配的值 客户端和服务端各自保存，与appId对应
    private static final String appSecret = "fdsafdsfsdfdsafsdfsafsdfffssssfffffffsaaaaaa";

//    /**
//     * 客户端调用
//     * @param  signatureHeaders header中需要的参数
//     * @param  pathParams @PathVariable 需要的参数
//     * @param  requestParamMap @RequestParam需要的参数
//     * @param entity  @ModelAttribute 或者 @RequestBody需要的参数
//     */
//    public static String signature(com.sljl.basicjava.signature.SignatureHeaders signatureHeaders, List<String> pathParams, Map<String, Object> requestParamMap, Object entity) {
//        List<String> requestParams = Collections.EMPTY_LIST;
//        List<String> pathVariables = Collections.EMPTY_LIST;
//        String beanParams = StringUtils.EMPTY;
//        if (!CollectionUtils.isEmpty(pathParams)) {
//            pathVariables = pathParams;
//        }
//        if (null != requestParamMap && !requestParamMap.isEmpty()) {
//            requestParams = new ArrayList<>();
//            for (Map.Entry<String, Object> entry : requestParamMap.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//                List<String> values = Lists.newArrayList();
//                if (value.getClass().isArray()) {
//                    //数组
//                    for (int j = 0; j < Array.getLength(value); ++j) {
//                        values.add(Array.get(value, j).toString());
//                    }
//                } else if (ClassUtils.isAssignable(Collection.class, value.getClass())) {
//                    //集合
//                    for (Object o : (Collection<?>) value) {
//                        values.add(o.toString());
//                    }
//                } else {
//                    //单个值
//                    values.add(value.toString());
//                }
//                values.sort(Comparator.naturalOrder());
//                requestParams.add(key + "=" + StringUtils.join(values));
//            }
//        }
//        if (!Objects.isNull(entity)) {
//            beanParams = toSplice(entity);
//        }
//        String headersToSplice = SignatureUtils.toSplice(signatureHeaders);
//
//        List<String> toSplices = Lists.newArrayList();
//        toSplices.add(headersToSplice);
//        toSplices.addAll(pathVariables);
//        requestParams.sort(Comparator.naturalOrder());
//        toSplices.addAll(requestParams);
//        toSplices.add(beanParams);
//        return SignatureUtils.signature(toSplices.toArray(new String[]{}), signatureHeaders.getAppsecret());
//    }

    public static String encode(String text, String appsecret) {
        byte token = (byte) (appsecret.hashCode() & XOR_TOKEN);
        byte[] tb = text.getBytes();
        for (int i = 0; i < tb.length; ++i) {
            tb[i] ^= token;
        }
        return Base64.getEncoder().encodeToString(tb);
    }

    public static String decode(String text, String appsecret) {
        byte token = (byte) (appsecret.hashCode() & XOR_TOKEN);
        byte[] tb = Base64.getDecoder().decode(text);
        for (int i = 0; i < tb.length; ++i) {
            tb[i] ^= token;
        }
        return new String(tb);
    }

//    /**     * 生成签名1     */
//    public static String signature(String[] args, String appsecret) {
//        String splice = StringUtils.join(args, DELIMETER);
//        System.out.println("拼接结果: " + splice);
//        String signature = HmacUtils.hmacSha256Hex(appsecret, splice);
//        return signature;
//    }

//    /**     * 生成签名2     */
//    public static String signature(Object object, String appsecret) {
//        if (Objects.isNull(object)) {
//            return StringUtils.EMPTY;
//        }
//        String splice = toSplice(object);
//        System.out.println("拼接结果: " + splice);
//        if (StringUtils.isBlank(splice)) {
//            return splice;
//        }
//        String signature = HmacUtils.hmacSha256Hex(appsecret, splice);
//        return signature;
//    }
//    /**
//     * 生成所有注有 SignatureField属性 key=value的 拼接         */
//    public static String toSplice(Object object) {
//        if (Objects.isNull(object)) {
//            return StringUtils.EMPTY;
//        }
////        return orderSignature(object);
//        // 默认按字典顺序排序
//        return alphaSignature(object);
//    }
    /**
     * 生成唯一nonce随机数
     * 仅供参考，不一定非得使用该方法
     */
    public static String generateNonce() {
        return HmacUtils.hmacSha256Hex(UUID.randomUUID().toString(), RandomStringUtils.random(10, true, true));
    }

//    // 字典序排序
//    private static String alphaSignature(Object object) {
//        StringBuilder result = new StringBuilder();
//        Map<String, String> map = new TreeMap<>();
//        for (Field field : getAllFields(object.getClass())) {
//            try {
//                map.put(field.getName(), toSplice(field.get(object)));
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//                System.out.println("签名拼接(alphaSignature)异常");
//            }
//            //            if (field.isAnnotationPresent(com.sljl.basicjava.signature.SignatureField.class)) {
////                field.setAccessible(true);
////                try {
////                    if (isAnnotated(field.getType(), Signature.class)) {
////                        if (!Objects.isNull(field.get(object))) {
////                            map.put(field.getName(), toSplice(field.get(object)));
////                        }
////                    } else {
//////                        com.sljl.basicjava.signature.SignatureField sgf = field.getAnnotation(com.sljl.basicjava.signature.SignatureField.class);
////                        if (StringUtils.isNotEmpty(sgf.customValue()) || !Objects.isNull(field.get(object))) {
////                            map.put(StringUtils.isNotBlank(sgf.customName()) ? sgf.customName() : field.getName()
////                                    , StringUtils.isNotEmpty(sgf.customValue()) ? sgf.customValue() : toString(field.get(object)));
////                        }
////                    }
////                } catch (Exception e) {
////                    e.printStackTrace();
////                    System.out.println("签名拼接(alphaSignature)异常");
////                }
////            }
//        }
//
//        for (Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
//            Map.Entry<String, String> entry = iterator.next();
//            result.append(entry.getKey()).append("=").append(entry.getValue());
//            if (iterator.hasNext()) {
//                result.append(DELIMETER);
//            }
//        }
//        return result.toString();
//    }

//    private static String toString(Object object) {
//        Class<?> type = object.getClass();
////        if (BeanUtils.isSimpleProperty(type)) {
////            return object.toString();
////        }
//        if (type.isArray()) {
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < Array.getLength(object); ++i) {
//                sb.append(toSplice(Array.get(object, i)));
//            }
//            return sb.toString();
//        }
//        if (ClassUtils.isAssignable(Collection.class, type)) {
//            StringBuilder sb = new StringBuilder();
//            for (Iterator<?> iterator = ((Collection<?>) object).iterator(); iterator.hasNext(); ) {
//                sb.append(toSplice(iterator.next()));
//                if (iterator.hasNext()) {
//                    sb.append(DELIMETER);
//                }
//            }
//            return sb.toString();
//        }
//        if (ClassUtils.isAssignable(Map.class, type)) {
//            StringBuilder sb = new StringBuilder();
//            for (Iterator<? extends Map.Entry<String, ?>> iterator = ((Map<String, ?>) object).entrySet().iterator(); iterator.hasNext(); ) {
//                Map.Entry<String, ?> entry = iterator.next();
//                if (Objects.isNull(entry.getValue())) {
//                    continue;
//                }
//                sb.append(entry.getKey()).append("=").append(toSplice(entry.getValue()));
//                if (iterator.hasNext()) {
//                    sb.append(DELIMETER);
//                }
//            }
//            return sb.toString();
//        }
//        return object.toString();
//
//    }

    private static <A extends Annotation> A findAnnotation(AnnotatedElement element, Class<A> annotationType) {
        A annotation = element.getAnnotation(annotationType);
        return annotation;
    }

    private static boolean isAnnotated(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        return element.isAnnotationPresent(annotationType);
    }

    public static Set<Field> getAllFields(final Class<?> type) {
        Set<Field> result = new HashSet<>(16);
        for (Class<?> t : getAllSuperTypes(type)) result.addAll(Arrays.asList(t.getDeclaredFields()));
        return result;
    }

    private static Set<Class<?>> getAllSuperTypes(final Class<?> type) {
        Set<Class<?>> result = new LinkedHashSet<>(16);
        if (type != null && !type.equals(Object.class)) {
            result.add(type);
            for (Class<?> supertype : getSuperTypes(type)) {
                result.addAll(getAllSuperTypes(supertype));
            }
        }
        return result;
    }

    private static Set<Class<?>> getSuperTypes(Class<?> type) {
        Set<Class<?>> result = new LinkedHashSet<>();
        Class<?> superclass = type.getSuperclass();
        Class<?>[] interfaces = type.getInterfaces();
        if (superclass != null && !superclass.equals(Object.class)) result.add(superclass);
        if (interfaces != null && interfaces.length > 0) result.addAll(Arrays.asList(interfaces));
        return result;
    }

    public static void main(String[] args) {
        StringBuilder result = new StringBuilder();

        Map<String, String> requestParam = Maps.newHashMap();
        requestParam.put("appname", "appname");
        requestParam.put("keyborad", "keyborad");
        requestParam.put("username", "哈士奇");
        requestParam.put("pjknsj", "pjknsj");
        requestParam.put("userage", "14");
        requestParam.put("address", "北京市朝阳区东三环中路");
        requestParam.put("email", "nb114917778@163.com");
        requestParam.put("signature", "");
        requestParam.put("timestamp", "1552533230");
        requestParam.put("nonce", SignatureUtils.generateNonce());
        System.out.println(JsonUtil.toJson(requestParam));
        Map<String, String> sortMap = Maps.newTreeMap();
        sortMap.putAll(requestParam);
        sortMap.remove("signature");
        for (Iterator<Map.Entry<String, String>> iterator = sortMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry = iterator.next();
            result.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                result.append(DELIMETER);
            }
        }
        String paramStr = result.toString();
        System.out.println(paramStr);
        String signature = HmacUtils.hmacSha256Hex(appSecret, paramStr);
        System.out.println(signature);
    }

    //同一个请求多长时间内有效
    private static final Long EXPIRE_TIME = 60 * 1000 * 10L;
    //同一个nonce 请求多长时间内不允许重复请求
    private static final Long RESUBMIT_DURATION = 2000L;


    //    /**
    //     * 根据request 中 header值生成SignatureHeaders实体
    //     */
    //    private com.sljl.basicjava.signature.SignatureHeaders generateSignatureHeaders(Signature signature, HttpServletRequest request) throws Exception {//NOSONAR
    //        Map<String, Object> headerMap = Collections.list(request.getHeaderNames()).stream().filter(headerName -> com.sljl.basicjava.signature.SignatureHeaders.HEADER_NAME_SET.contains(headerName)).collect(Collectors.toMap(headerName -> headerName.replaceAll("-", "."), headerName -> request.getHeader(headerName)));
    //        PropertySource propertySource = new MapPropertySource("signatureHeaders", headerMap);
    //        com.sljl.basicjava.signature.SignatureHeaders signatureHeaders = RelaxedConfigurationBinder.with(com.sljl.basicjava.signature.SignatureHeaders.class).setPropertySources(propertySource).doBind();
    //        Optional<String> result = ValidatorUtils.validateResultProcess(signatureHeaders);
    //        if (result.isPresent()) {
    //            throw new ServiceException("WMH5000", result.get());
    //        }
    //        String appSecret = limitConstants.getSignatureLimit().get(signatureHeaders.getAppid());
    //        if (StringUtils.isBlank(appSecret)) {
    //            System.out.println("未找到appId对应的appSecret, appId=" + signatureHeaders.getAppid());
    //        }
    //
    //        //其他合法性校验
    //        Long now = System.currentTimeMillis();
    //        Long requestTimestamp = Long.parseLong(signatureHeaders.getTimestamp());
    //        if ((now - requestTimestamp) > EXPIRE_TIME) {
    //            String errMsg = "请求时间超过规定范围时间10分钟, signature=" + signatureHeaders.getSignature();
    //            System.out.println(errMsg);
    //        }
    //        String nonce = signatureHeaders.getNonce();
    //        if (nonce.length() < 10) {
    //            String errMsg = "随机串nonce长度最少为10位, nonce=" + nonce;
    //            System.out.println(errMsg);
    //        }
    ////        if (是否开启重复提交判断) {
    ////            String existNonce = redisCacheService.getString(nonce);
    ////            if (StringUtils.isBlank(existNonce)) {
    ////                redisCacheService.setString(nonce, nonce);
    ////                redisCacheService.expire(nonce, (int) TimeUnit.MILLISECONDS.toSeconds(RESUBMIT_DURATION));
    ////            } else {
    ////                String errMsg = "不允许重复请求, nonce=" + nonce;
    ////                System.out.println(errMsg);
    ////            }
    ////        }
    //
    //        signatureHeaders.setAppsecret(appSecret);
    //        return signatureHeaders;
    //    }

    //    /**
    //     * 生成header中的参数，mehtod中的参数的拼接
    //     */
    //    private List<String> generateAllSplice(Method method, Object[] args, String headersToSplice) {
    //        List<String> pathVariables = Lists.newArrayList(), requestParams = Lists.newArrayList();
    //        String beanParams = StringUtils.EMPTY;
    //        for (int i = 0; i < method.getParameterCount(); ++i) {
    //            MethodParameter mp = new MethodParameter(method, i);
    //            boolean findSignature = false;
    //            for (Annotation anno : mp.getParameterAnnotations()) {
    //                if (anno instanceof PathVariable) {
    //                    if (!Objects.isNull(args[i])) {
    //                        pathVariables.add(args[i].toString());
    //                    }
    //                    findSignature = true;
    //                } else if (anno instanceof RequestParam) {
    //                    RequestParam rp = (RequestParam) anno;
    //                    String name = mp.getParameterName();
    //                    if (StringUtils.isNotBlank(rp.name())) {
    //                        name = rp.name();
    //                    }
    //                    if (!Objects.isNull(args[i])) {
    //                        List<String> values = Lists.newArrayList();
    //                        if (args[i].getClass().isArray()) {
    //                            //数组
    //                            for (int j = 0; j < Array.getLength(args[i]); ++j) {
    //                                values.add(Array.get(args[i], j).toString());
    //                            }
    //                        } else if (ClassUtils.isAssignable(Collection.class, args[i].getClass())) {
    //                            //集合
    //                            for (Object o : (Collection<?>) args[i]) {
    //                                values.add(o.toString());
    //                            }
    //                        } else {
    //                            //单个值
    //                            values.add(args[i].toString());
    //                        }
    //                        values.sort(Comparator.naturalOrder());
    //                        requestParams.add(name + "=" + StringUtils.join(values));
    //                    }
    //                    findSignature = true;
    //                } else if (anno instanceof RequestBody || anno instanceof ModelAttribute) {
    //                    beanParams = SignatureUtils.toSplice(args[i]);
    //                    findSignature = true;
    //                }
    //
    //                if (findSignature) {
    //                    break;
    //                }
    //            }
    //            if (!findSignature) {
    //                System.out.println(String.format("签名未识别的注解, method=%s, parameter=%s, annotations=%s", method.getName(), mp.getParameterName(), StringUtils.join(mp.getMethodAnnotations())));
    //            }
    //        }
    //        List<String> toSplices = Lists.newArrayList();
    //        toSplices.add(headersToSplice);
    //        toSplices.addAll(pathVariables);
    //        requestParams.sort(Comparator.naturalOrder());
    //        toSplices.addAll(requestParams);
    //        toSplices.add(beanParams);
    //        return toSplices;
    //    }

}
