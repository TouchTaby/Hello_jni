package utils;

import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by administrator on 2017-12-29.
 */

public class UhfManager {
    public InputStream is;
    public OutputStream os;
    public ReadThread readThread;
    SerialPort serialPort;
    NewSendCommendManager manager;
    public static int RESERVE = 0;
    public static int EPC = 1;
    public static int TID = 2;
    public static int USER = 3;
    public UhfManager(InputStream in, OutputStream out) {
        this.is = in;
        this.os =  out;
        Log.e("TAG", "UhfManager: new " );
//        readThread = new ReadThread();
//        readThread.start();
//        is = serialPort.getInputStream();
//        os = serialPort.getOutputStream();
        manager = new NewSendCommendManager(is, os);


    }

//    public List<byte[]> inventoryRealTime() {
////            this.unSelectEPC();
//            byte[] cmd = new byte[]{-69, 0, 34, 0, 0, 34, 126};
//            this.sendCMD(cmd);
//            List<byte[]> list = new ArrayList();
//            byte[] response = this.read();
//            if(response != null) {
//                int responseLength = response.length;
//                int start = 0;
//                if(responseLength > 6) {
//                    while(responseLength > 5) {
//                        int paraLen = response[start + 4] & 255;
//                        int singleCardLen = paraLen + 7;
//                        if(singleCardLen > responseLength) {
//                            break;
//                        }
//                        byte[] sigleCard = new byte[singleCardLen];
//                        System.arraycopy(response, start, sigleCard, 0, singleCardLen);
//                        byte[] resolve = this.handlerResponse(sigleCard);
//                        if(resolve != null && paraLen > 5) {
//                            byte[] epcBytes = new byte[paraLen - 5];
//                            System.arraycopy(resolve, 4, epcBytes, 0, paraLen - 5);
//                            list.add(epcBytes);
//                        }
//
//                        start += singleCardLen;
//                        responseLength -= singleCardLen;
//                    }
//                } else {
//                    this.handlerResponse(response);
//                }
//            }
//
//            return list;
//
//    }
//    private void sendCMD(byte[] cmd) {
//        try {
//            this.os.write(StringUtility.hexString2Bytes("BB00270003222710837E"));
//            this.os.flush();
//        } catch (IOException var3) {
//            var3.printStackTrace();
//        }
//
//    }
    private class ReadThread extends Thread {
//        @Override
//        public void run() {
//            byte[] head_buffer = new byte[1];
//            byte[] len_buffer = new byte[4];
//            byte[] buffer = new byte[1024];
//            int dirty_len;
//            synchronized (this) {
//                try {
//                    dirty_len = is.read(buffer);
//                } catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//                while (!isInterrupted()) {
//                    int size;
//                    try {
//                        if (is == null)
//                            return;
//                        size = is.read(head_buffer);
//                        if (size == 0) {
//                            try {
//                                Thread.sleep(10);
//                            } catch (Exception e) {
//                                // TODO: handle exception
//                                e.printStackTrace();
//                            }
//                        } else {
//                            try {
//                                Thread.sleep(1);
//                            } catch (Exception e) {
//                                // TODO: handle exception
//                                e.printStackTrace();
//                            }
//                        }
//
//                        if (head_buffer[0] != -69)
//                            continue;
//
//                        size = is.read(len_buffer);
//                        if (size != 4) continue;
//                        //if(len_buffer[0]!=0x2) continue;
//                        //if(len_buffer[1]!=0x22) continue;
//
//                        int len;
//                        len = (int) (len_buffer[2] & 0xff);
//                        len = len * 0x100;
//                        len = len + (int) len_buffer[3] & 0xff;
//                        if (len > 128) continue;
//                        byte[] msg_buf = new byte[len + 2];
//
//                        size = is.read(msg_buf);
//                        if (size != len + 2)
//                            continue;
//                        if (msg_buf[len + 1] != 0x7e) continue;
//
//                        buffer[0] = head_buffer[0];
//                        for (int i = 0; i < 4; i++)
//                            buffer[i + 1] = len_buffer[i];
//                        for (int i = 0; i < len + 2; i++)
//                            buffer[i + 5] = msg_buf[i];
//                        String result = StringUtility.bytes2HexString(buffer, buffer.length);
//                        Log.e("UhfManager", "manager----- "+ result.substring(16,36) );
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return;
//                    }
//                    try {
//                        Thread.sleep(10);
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }
    }
//    public int unSelectEPC() {
//        byte[] cmd = new byte[]{-69, 0, 18, 0, 1, 1, 20, 126};
//        this.sendCMD(cmd);
//        byte[] response = this.read();
//        return 0;
//    }

//    private byte[] read() {
//        byte[] responseData = null;
//        byte[] response = null;
//        int available = 0;
//        int index = 0;
//        int headIndex = 0;
//
//        try {
//            while(index < 10) {
//                Thread.sleep(50L);
//                available = this.is.available();
//                if(available > 7) {
//                    break;
//                }
//
//                ++index;
//            }
//
//            if(available > 0) {
//                responseData = new byte[available];
//                this.is.read(responseData);
//                for(int i = 0; i < available; ++i) {
//                    if(responseData[i] == -69) {
//                        headIndex = i;
//                        break;
//                    }
//                }
//
//                response = new byte[available - headIndex];
//                System.arraycopy(responseData, headIndex, response, 0, response.length);
//            }
//        } catch (Exception var7) {
//            var7.printStackTrace();
//        }
//
//        return response;
//    }
//    private byte[] handlerResponse(byte[] response) {
//        byte[] data = null;
//        int responseLength = response.length;
//        if(response[0] != -69) {
//            return data;
//        } else if(response[responseLength - 1] != 126) {
//            return data;
//        } else if(responseLength < 7) {
//            return data;
//        } else {
//            int lengthHigh = response[3] & 255;
//            int lengthLow = response[4] & 255;
//            int dataLength = lengthHigh * 256 + lengthLow;
//            byte crc = this.checkSum(response);
//            if(crc != response[responseLength - 2]) {
//                return data;
//            } else {
//                if(dataLength != 0 && responseLength == dataLength + 7) {
//                    data = new byte[dataLength + 1];
//                    data[0] = response[2];
//                    System.arraycopy(response, 5, data, 1, dataLength);
//                }
//
//                return data;
//            }
//        }
//    }
//    public byte checkSum(byte[] data) {
//        byte crc = 0;
//
//        for(int i = 1; i < data.length - 2; ++i) {
//            crc += data[i];
//        }
//
//        return crc;
//    }
    public boolean setBaudrate() {
        return manager.setBaudrate();
    }

    public byte[] getFirmware() {
        return manager.getFirmware();
    }

    public boolean setOutputPower(int value) {
        return manager.setOutputPower(value);
    }

    public List<byte[]> inventoryRealTime() {
        return manager.inventoryRealTime();
    }

    public void selectEPC(byte[] epc) {
        manager.selectEPC(epc);
    }

    public byte[] readFrom6C(int memBank, int startAddr, int length, byte[] accessPassword) {
        return manager.readFrom6C(memBank, startAddr, length, accessPassword);
    }

    public boolean writeTo6C(byte[] password, int memBank, int startAddr, int dataLen, byte[] data) {
        return manager.writeTo6C(password, memBank, startAddr, dataLen, data);
    }

    public void setSensitivity(int value) {
        manager.setSensitivity(value);
    }

    public boolean lock6C(byte[] password, int memBank, int lockType) {
        return manager.lock6C(password, memBank, lockType);
    }

    public byte checkSum(byte[] data) {
        return manager.checkSum(data);
    }

    public int setFrequency(int startFrequency, int freqSpace, int freqQuality) {
        return manager.setFrequency(startFrequency, freqSpace, freqQuality);
    }

    public int setWorkArea(int area) {
        return manager.setWorkArea(area);
    }

    public List<byte[]> inventoryMulti() {
        return manager.inventoryMulti();
    }

    public void stopInventoryMulti() {
        manager.stopInventoryMulti();
    }

    public int getFrequency() {
        return manager.getFrequency();
    }

    public int unSelect() {
        return manager.unSelectEPC();
    }

    public void setRecvParam(int mixer_g, int if_g, int trd) {
        manager.setRecvParam(mixer_g, if_g, trd);
    }

    public boolean kill6C(byte[] killPassword) {
        return manager.kill6C(killPassword);
    }
}
