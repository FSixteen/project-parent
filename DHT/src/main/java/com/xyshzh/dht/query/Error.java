package com.xyshzh.dht.query;

/**
 * 一个KRPC消息由一个独立的字典组成，其中有2个关键字是所有的消息都包含的，其余的附加关键字取决于消息类型。
 * 每一个消息都包含t关键字，它是一个代表了transactionID的字符串类型。transactionID由请求node
 * 产生，并且回复中要包含回显该字段，所以回复可能对应一个节点的多个请求。transactionID应当被编码为一个
 * 短的二进制字符串，比如2个字节，这样就可以对应2^16个请求。另一个每个KRPC消息都包含的关键字是y，它由一
 * 个字节组成，表明这个消息的类型。y对应的值有三种情况：q表示请求，r表示回复，e表示错误。
 * 
 * 错误，对应于KPRC消息字典中的y关键字的值是“e”，包含一个附加的关键字e。关键字“e”是一个列表类型。
 * 第一个元素是一个数字类型，表明了错误码。第二个元素是一个字符串类型，表明了错误信息。当一个请求不能
 * 解析或出错时，错误包将被发送。下表描述了可能出现的错误码：
 * 
 * 错误码 错误描述 <br/>
 * 201 一般错误<br/>
 * 202 服务错误<br/>
 * 203 协议错误,比如不规范的包，无效的参数，或者错误的token<br/>
 * 204 未知方法<br/>
 * 
 * @author Shengjun Liu
 * @version 2018-02-12
 *
 */
public class Error {

}