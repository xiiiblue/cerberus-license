package cn.telchina.cerberus.cypher;

import java.nio.file.Path;

public interface LicenseSigner {
    /**
     * 生成公私钥对
     */
    void generate();

    /**
     * 签名
     *
     * @param data 被签名数据
     * @return 签名
     */
    String sign(String data);

    /**
     * 验签
     *
     * @param data 被签名数据
     * @param sign 签名
     * @return boolean
     */
    boolean verify(String data, String sign);

    /**
     * 获取私钥
     *
     * @return 私钥编码
     */
    String getPrivateKey();


    /**
     * 获取公钥
     *
     * @return 公钥编码
     */
    String getPublicKey();

    /**
     * 设置私钥
     *
     * @param key 私钥编码
     */
    void setPrivateKey(String key);

    /**
     * 设置公钥
     *
     * @param key 公钥编码
     */
    void setPublicKey(String key);

    /**
     * 保存私钥至文件
     *
     * @param path 路径
     */
    void savePrivateKey(Path path);

    /**
     * 保存公钥至文件
     *
     * @param path 路径
     */
    void savePublicKey(Path path);

    /**
     * 从文件读取私钥
     *
     * @param path 路径
     */
    void loadPrivateKey(Path path);

    /**
     * 从文件读取公钥
     *
     * @param path 路径
     */
    void loadPublicKey(Path path);
}
