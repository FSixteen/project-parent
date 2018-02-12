package com.xyshzh.dht.bencode;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.Test;

import com.xyshzh.dht.query.Request;

public class BEncodeTest {

  @Test
  public void t2() {
    try {
      DatagramSocket datagramSocket=new DatagramSocket();
      InetAddress address=InetAddress.getByName("router.utorrent.com");
      
      //为接受的数据包创建空间
      ByteBuffer data = Request.find_node("t", "12345678901234567890", "12345678901234567890");
      byte [] buffer= data.array();
      DatagramPacket packet=new DatagramPacket(buffer, buffer.length, address, 6881);
      datagramSocket.send(packet);

      //接收数据
      DatagramPacket inputPacket = new DatagramPacket(new byte[512], 512);  
      datagramSocket.receive(inputPacket);  
      System.out.println(new String(inputPacket.getData(), 0 , inputPacket.getLength(), Charset.DEFAULT));
      datagramSocket.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void t1() {
    System.out.println("F2CD762B8343780D10BAD5B05B9055001FB5498C".getBytes().length);
  }

  @Test
  public void t() {
    int port = 65534;

    ByteBuffer bb = ByteBuffer.allocate(4).putInt(port);
    bb.flip();
    byte[] portData = Arrays.copyOfRange(bb.array(), 0, 4);
    for (byte b : portData) {
      System.out.println(b);
    }
    for (byte b : intToBytes(port)) {
      System.out.println(b);
    }
  }

  public static byte[] intToBytes(int value) {
    byte[] src = new byte[4];
    src[3] = (byte) ((value >> 24) & 0xFF);
    src[2] = (byte) ((value >> 16) & 0xFF);
    src[1] = (byte) ((value >> 8) & 0xFF);
    src[0] = (byte) (value & 0xFF);
    return src;
  }

  @Test
  public void test() {
    
  }
}
