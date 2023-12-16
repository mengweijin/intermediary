## DEPLOY

### jsch jar包连接不上ssh报Algorithm negotiation fail 错误
#### 问题分析：
sshd配置中没有打开支持jsch jar内需求的算法，默认开放的算法在 man sshd_config 中可以看，是 sshd 的高级版本中默认关闭了部分算法。

- kex算法查看：ssh -Q kex
- Host 查看：ssh -Q HostKeyAlgorithms
- hash算法查看：ssh -Q mac
- 传输加密算法查看：ssh -Q cipher

可以先配置 kex 算法。

上面命令查看到的算法是 sshd 支持的算法，并不是已经打开的算法。

#### 查看 jsch 支持的算法(kex 算法 )
```java
public static void printJschJarSupportedAlgorithm() {
    log.info("kex: "+ JSch.getConfig("kex"));
}
```
响应如下：
```bash
kex :ecdh-sha2-nistp256
ecdh-sha2-nistp384,ecdh-sha2-nistp521,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha256,diffie-hellman-group-exchange-sha1,diffie-hellman-group1-sha1
```

#### 解决方案
修改 /etc/ssh/sshd_config 文件的配置，找到同时支持 sshd 和 jsch 的算法。

进入liunx 中，将上面jsch打印出来的值，通过命令: vim /etc/ssh/sshd_config 修改到 KexAlgorithms 和 HostKeyAlgorithms

例如：kexAlgorithms 后面的值：
```bash
KexAlgorithms diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256,ecdh-sha2-nistp256,ecdh-sha2-nistp384,ecdh-sha2-nistp521,diffie-hellman-group1-sha1,curve25519-sha256@libssh.org
# HostKeyAlgorithms ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521
```

#### 最后重启 sshd 服务
```bash
service sshd restart
```