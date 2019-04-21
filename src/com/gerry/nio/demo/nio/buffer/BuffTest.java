package com.gerry.nio.demo.nio.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * @Title: BuffTest.java
 * @Description: Buffer ByteBuffer LongBuffer IntegerBuffer FloatBuffer DoubleBuffer
 * 缓存区分为两种，一种是直接缓冲区  另一种非缓冲区
 * 非直接缓冲区主要存放在jvm缓存区中，来回拷贝
 * 直接缓冲区--存放在物理内存中
 * @author gerry
 * @version V1.0
 */
public class BuffTest {
    public static void main(String[] args) throws IOException {

       BuffTest bt = new BuffTest();
        bt.test001();
        bt.test002();
        bt.test003();
        bt.test004();
    }

    /**
     * Position 缓冲区正在操作的位置默认从0开始
     * limit 写数据时，limit表示可对Buffer最多写入多少个数据。 读数据时，limit表示Buffer里有多少可读数据（not null的数据），
     * capacity 缓冲区最大容量 一旦声明不能改变
     * 核心方法：
     * put() 往buff存放数据
     * get() 获取数据
     */
    //@Test
    public void test001() {

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("position:"+byteBuffer.position());
        System.out.println("limit:"+byteBuffer.limit());
        System.out.println("capacity:"+byteBuffer.capacity());
        System.out.println("往bytebuffer里存放数据。。。");
        byteBuffer.put("A23abc一二三".getBytes());
        System.out.println("position:"+byteBuffer.position());
        System.out.println("limit:"+byteBuffer.limit());
        System.out.println("capacity:"+byteBuffer.capacity());

        System.out.println("往bytebuffer里读取数据。。。");
        byteBuffer.flip();// 开启读取模式
        System.out.println("position:"+byteBuffer.position());
        System.out.println("limit:"+byteBuffer.limit());
        System.out.println("capacity:"+byteBuffer.capacity());
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes, 0, bytes.length));
        System.out.println("读取完成后---");
        System.out.println("position:"+byteBuffer.position());
        System.out.println("limit:"+byteBuffer.limit());
        System.out.println("capacity:"+byteBuffer.capacity());

        System.out.println("往bytebuffer里重复读取数据。。。");
        byteBuffer.rewind();// 重复读取
        System.out.println("position:"+byteBuffer.position());
        System.out.println("limit:"+byteBuffer.limit());
        System.out.println("capacity:"+byteBuffer.capacity());
        byte[] bytes2 = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes2);
        System.out.println(new String(bytes2, 0, bytes.length));

        System.out.println("清空缓存区。。。");
        byteBuffer.clear();//// 值不清空 下标清空
        System.out.println("position:"+byteBuffer.position());
        System.out.println("limit:"+byteBuffer.limit());
        System.out.println("capacity:"+byteBuffer.capacity());
        System.out.println((char) byteBuffer.get());
    }

    /**
     * @return void
     * @throws
     * @Title: test002
     * @Description: mark 和rest的用法
     */
    //@Test
    public void test002() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10); // 直接缓冲区
        String str = "abcd";
        byteBuffer.put(str.getBytes());
        // 开启读的模式
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes, 0, 2);
        byteBuffer.mark(); // 打印标记
        System.out.println(new String(bytes, 0, 2));
        System.out.println(byteBuffer.position());
        System.out.println("=======================");
        byteBuffer.reset();// 还原到mark位置
        byteBuffer.get(bytes, 2, 2);
        System.out.println(new String(bytes, 0, 2));
        byteBuffer.reset();
        System.out.println("重置还原到mark标记");
        System.out.println(byteBuffer.position());
    }

    /**
     * @return void
     * @throws IOException
     * @throws
     * @Title: test003
     * @Description: 分散读取聚集写入
     */
    //@Test
    public void test003() throws IOException {
        // 随机访问
        RandomAccessFile raf = new RandomAccessFile("d:/abc.txt", "rw");
        // 获取通信
        FileChannel channel = raf.getChannel();
        // 分配指定大小指定缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(6);
        ByteBuffer buf2 = ByteBuffer.allocate(10);
        // 分散读取
        ByteBuffer[] bufs = {buf1, buf2};
        channel.read(bufs);
        for (ByteBuffer buf : bufs) {
            // 切换成读模式
            buf.flip();
        }
        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println("-----------------------------");
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));
        System.out.println("-----------重复读取-------------");
        RandomAccessFile raf2 = new RandomAccessFile("d:/abc.txt", "rw");
        // 获取通道
        FileChannel channel2 = raf2.getChannel();
        channel2.write(bufs);
        raf2.close();
        raf.close();
    }

    /**
     * @return void
     * @throws CharacterCodingException
     * @throws
     * @Title: test004
     * @Description: 编码格式
     */

    //@Test
    public void test004() throws CharacterCodingException {
        // 获取编码器
        Charset charset = Charset.forName("GBK");
        CharsetEncoder ce = charset.newEncoder();
        // 获取解码器
        CharsetDecoder cd = charset.newDecoder();
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("解码器。。。");
        charBuffer.flip();
        // 编码
        ByteBuffer buffer = ce.encode(charBuffer);
        for (int i = 0; i < 6; i++) {
            System.out.println(buffer.get());
        }
        // 解码
        buffer.flip();
        // 编码解密
        CharBuffer decode = cd.decode(buffer);
        System.out.println(decode.toString());
        // Charset c2 = Charset.forName("utf-8");
        // CharBuffer decode2 = c2.newDecoder().decode(buffer);
        // System.out.println(decode2.toString());
    }
}