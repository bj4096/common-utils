package com.sljl.core.utils;

import net.jpountz.lz4.*;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.xerial.snappy.Snappy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 多种压缩算法工具类
 *
 * <pre>
 * LZ4、Snappy、Gzip、Deflate
 * </pre>
 *
 * @author L.Y.F
 */
public class CompressUtil {

    public final static int BLOCK_64K = 64 * 1024;
    public final static LZ4Compressor NATIVE_FAST_COMPRESSOR = LZ4Factory.fastestInstance().fastCompressor();
    public final static LZ4FastDecompressor FAST_DECOMPRESSOR = LZ4Factory.fastestInstance().fastDecompressor();

    /**
     * 采用LZ4算法压缩字节数组
     *
     * @param data
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] lz4Compress(byte[] data) throws IOException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        LZ4BlockOutputStream compressedOutput = new LZ4BlockOutputStream(byteOutput, BLOCK_64K, NATIVE_FAST_COMPRESSOR);
        compressedOutput.write(data);
        IOUtils.closeQuietly(compressedOutput);
        return byteOutput.toByteArray();
    }

    /**
     * 采用LZ4算法加压缩字节数组
     *
     * @param data
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] lz4Decompress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BLOCK_64K);
        LZ4BlockInputStream lzis = new LZ4BlockInputStream(new ByteArrayInputStream(data), FAST_DECOMPRESSOR);
        int count;
        byte[] buffer = new byte[BLOCK_64K];
        while ((count = lzis.read(buffer)) != -1) {
            baos.write(buffer, 0, count);
        }
        IOUtils.closeQuietly(lzis);
        return baos.toByteArray();
    }

    /**
     * 采用Snappy算法压缩字节数组
     *
     * @param data
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] snappyCompress(byte[] data) throws IOException {
        return Snappy.compress(data);
    }

    /**
     * 采用Snappy算法解压缩字节数组
     *
     * @param data
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] snappyDecompress(byte[] data) throws IOException {
        return Snappy.uncompress(data);
    }

    /**
     * 采用GZip算法压缩字节数组
     *
     * @param data
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] commonGzipCompress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BLOCK_64K);
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos);
        gcos.write(data);
        IOUtils.closeQuietly(gcos);
        return baos.toByteArray();
    }

    /**
     * 采用采用GZip算法解压缩字节数组
     *
     * @param data
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] commonGzipDecompress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BLOCK_64K);
        GzipCompressorInputStream gcis = new GzipCompressorInputStream(new ByteArrayInputStream(data));
        int count;
        byte[] buffer = new byte[BLOCK_64K];
        while ((count = gcis.read(buffer)) != -1) {
            baos.write(buffer, 0, count);
        }
        IOUtils.closeQuietly(gcis);
        return baos.toByteArray();
    }

    /**
     * 采用Deflate算法压缩字节数组
     *
     * @param data
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] commonDeflaterCompress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BLOCK_64K);
        DeflateParameters dp = new DeflateParameters();
        dp.setCompressionLevel(1);
        DeflateCompressorOutputStream dcos = new DeflateCompressorOutputStream(baos, dp);
        dcos.write(data);
        IOUtils.closeQuietly(dcos);
        return baos.toByteArray();
    }

    /**
     * 采用Deflate算法解压缩字节数组
     *
     * @param data
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] commonDeflaterDecompress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BLOCK_64K);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        int count;
        byte[] buffer = new byte[BLOCK_64K];
        while ((count = dcis.read(buffer)) != -1) {
            baos.write(buffer, 0, count);
        }
        IOUtils.closeQuietly(dcis);
        return baos.toByteArray();
    }
}
