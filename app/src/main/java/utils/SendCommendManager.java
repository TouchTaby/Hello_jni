package utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2018-01-02.
 */

public class SendCommendManager {
    private InputStream in;
    private OutputStream out;


    public SendCommendManager(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public boolean setBaudrate() {
        boolean flag = false;
        byte[] cmd = new byte[]{-96, 4, -1, 113, 4, 0};
        cmd[5] = this.checkSum(cmd, cmd.length - 1);
        this.sendToReader(cmd);
        byte[] recv = this.read();
        if(recv != null) {
            if(this.isRecvData(recv) == 0 && Integer.valueOf(recv[3]).intValue() == 113 && Integer.valueOf(recv[4]).intValue() == 16) {
                flag = true;
            }
        } else {
            Log.i("readFrom6C", "指锟筋超时");
        }

        return flag;
    }

    public byte[] getFirmware() {
        byte[] cmd = new byte[]{-96, 3, 1, 114, 0};
        cmd[4] = this.checkSum(cmd, cmd.length - 1);
        this.sendToReader(cmd);
        byte[] recv = this.read();
        if(recv != null) {
            System.out.println(new String(recv));
        }

        return recv;
    }

    public byte[] getFrequency() {
        byte[] cmd = new byte[]{-96, 3, 1, 121, -29};
        this.sendToReader(cmd);
        byte[] recv = this.read();
        if(recv != null) {
            System.out.println(Tools.Bytes2HexString(recv, recv.length));
        }

        return null;
    }

    public int setFrequency(int startFrequency, int freqSpace, int freqQuality) {
        byte[] cmd = new byte[]{-96, 9, 1, 120, 4, (byte)(freqSpace / 10), (byte)freqQuality, (byte)((16711680 & startFrequency) >> 16), (byte)(('\uff00' & startFrequency) >> 8), (byte)(255 & startFrequency), 0};
        cmd[10] = this.checkSum(cmd, cmd.length - 1);
        System.out.println(Tools.Bytes2HexString(cmd, cmd.length));
        this.sendToReader(cmd);
        byte[] recv = this.read();
        if(recv != null) {
            System.out.println(Tools.Bytes2HexString(recv, recv.length));
        }

        return 0;
    }

    public void setWorkAntenna() {
        byte[] cmd = new byte[]{-96, 4, 1, 116, 0, 0};
        cmd[5] = this.checkSum(cmd, cmd.length - 1);
        this.sendToReader(cmd);
    }

    public boolean setOutputPower(int value) {
        byte[] cmd = new byte[]{-96, 4, 1, 118, (byte)value, 0};
        cmd[5] = this.checkSum(cmd, cmd.length - 1);
        this.sendToReader(cmd);
        this.read();
        return true;
    }

    public List<byte[]> inventoryRealTime() {
        List<byte[]> epcList = new ArrayList();
        byte[] cmd = new byte[]{-96, 4, 1, -119, 1, 0};
        cmd[5] = this.checkSum(cmd, cmd.length - 1);
        this.sendToReader(cmd);
        Log.i("", "send inventory real time***");

        try {
            Thread.sleep(50L);
        } catch (InterruptedException var10) {
            var10.printStackTrace();
        }

        byte[] recv = this.read();
        if(recv != null) {
            int length = recv.length;
            new ArrayList();

            byte[] data;
            for(int start = 0; length > 0 && recv[start] == -96; length -= data.length) {
                int dataLen = recv[start + 1] & 255;
                data = new byte[dataLen + 2];
                if(data.length > 12 && data.length <= length) {
                    System.arraycopy(recv, start, data, 0, data.length);
                    byte[] epc = new byte[data.length - 9];
                    System.arraycopy(data, 7, epc, 0, data.length - 9);
                    epcList.add(epc);
                }

                start += data.length;
            }
        } else {
            Log.i("realTimeInventory", "指锟筋超时");
        }

        return epcList;
    }

    public void selectEPC(byte[] epc) {
        int epcLen = epc.length;
        byte[] cmd = new byte[7 + epcLen];
        cmd[0] = -96;
        cmd[1] = 17;
        cmd[2] = 1;
        cmd[3] = -123;
        cmd[4] = 0;
        cmd[5] = 12;
        System.arraycopy(epc, 0, cmd, 6, epcLen);
        cmd[6 + epcLen] = this.checkSum(cmd, cmd.length - 1);
        this.sendToReader(cmd);
        byte[] recv = this.read();
        if(recv == null) {
            Log.i("selectEPC", "指锟筋超时");
        }

    }

    public byte[] readFrom6C(int memBank, int startAddr, int length) {
        byte[] data = null;
        byte[] cmd = new byte[]{-96, 6, 1, -127, (byte)memBank, (byte)startAddr, (byte)length, 0};
        cmd[7] = this.checkSum(cmd, cmd.length - 1);
        this.sendToReader(cmd);

        try {
            Thread.sleep(100L);
        } catch (InterruptedException var13) {
            var13.printStackTrace();
        }

        byte[] recv = this.read();
        if(recv != null) {
            int len = recv.length;
            if(recv[0] != -96) {
                return data;
            }

            byte check = this.checkSum(recv, len - 1);
            if(check != recv[len - 1]) {
                Log.i("read data ", "checksum error");
                return data;
            }

            if(len == 6) {
                Log.i("read data ", "read fail!!");
                return data;
            }

            int tagCount = (recv[4] & 255) * 256 + (recv[5] & 255);
            int dataLen = recv[6] & 255;
            int readLen = recv[len - 4];
            int epcLen = dataLen - readLen - 4;
            if(dataLen <= readLen || dataLen < epcLen || epcLen < 0) {
                return data;
            }

            data = new byte[readLen + epcLen];
            System.arraycopy(recv, 9, data, 0, epcLen);
            System.arraycopy(recv, 11 + epcLen, data, epcLen, readLen);
        } else {
            Log.i("read data ", "指锟筋超时");
        }

        return data;
    }

    public byte[] readFrom6C(int memBank, int startAddr, int length, byte[] password) {
        byte[] data = null;
        byte[] cmd = new byte[12];
        cmd[0] = -96;
        cmd[1] = 10;
        cmd[2] = 1;
        cmd[3] = -127;
        cmd[4] = (byte)memBank;
        cmd[5] = (byte)startAddr;
        cmd[6] = (byte)length;
        if(password.length != 4) {
            Log.e("readFrom6C", "password error");
            return null;
        } else {
            cmd[7] = password[0];
            cmd[8] = password[1];
            cmd[9] = password[2];
            cmd[10] = password[3];
            cmd[11] = this.checkSum(cmd, cmd.length - 1);
            this.sendToReader(cmd);

            try {
                Thread.sleep(50L);
            } catch (InterruptedException var14) {
                var14.printStackTrace();
            }

            byte[] recv = this.read();
            if(recv != null) {
                int len = recv.length;
                if(recv[0] != -96) {
                    return data;
                }

                byte check = this.checkSum(recv, len - 1);
                if(check != recv[len - 1]) {
                    Log.i("read data ", "checksum error");
                    return data;
                }

                if(len == 6) {
                    Log.i("read data ", "read fail!!");
                    return data;
                }

                int tagCount = (recv[4] & 255) * 256 + (recv[5] & 255);
                int dataLen = recv[6] & 255;
                int readLen = recv[len - 4];
                int epcLen = dataLen - readLen - 4;
                if(dataLen <= readLen || dataLen < epcLen || epcLen < 0) {
                    return data;
                }

                if(recv.length < epcLen + 9) {
                    return data;
                }

                data = new byte[readLen + epcLen];
                System.arraycopy(recv, 9, data, 0, epcLen);
                System.arraycopy(recv, 11 + epcLen, data, epcLen, readLen);
            } else {
                Log.i("read data ", "指锟筋超时");
            }

            return data;
        }
    }

    public boolean writeTo6C(byte[] password, int memBank, int wordAdd, int dataLen, byte[] data) {
        boolean writeFlag = false;
        dataLen /= 2;
        int cmdLen = 12 + dataLen * 2;
        byte[] cmd = new byte[cmdLen];
        cmd[0] = -96;
        cmd[1] = (byte)(cmdLen - 2);
        cmd[2] = 1;
        cmd[3] = -126;
        System.arraycopy(password, 0, cmd, 4, password.length);
        cmd[8] = (byte)memBank;
        cmd[9] = (byte)wordAdd;
        cmd[10] = (byte)dataLen;
        System.arraycopy(data, 0, cmd, 11, data.length);
        cmd[11 + data.length] = this.checkSum(cmd, 11 + data.length);
        this.sendToReader(cmd);

        try {
            Thread.sleep(150L);
        } catch (InterruptedException var12) {
            var12.printStackTrace();
        }

        byte[] recv = this.read();
        if(recv != null) {
            int recvLength = recv.length;
            if(recvLength == 6) {
                return writeFlag;
            }

            if(this.isRecvData(recv) == 0) {
                writeFlag = true;
            }
        } else {
            Log.i("write data", "指锟筋超时");

            try {
                Thread.sleep(1500L);
            } catch (InterruptedException var11) {
                var11.printStackTrace();
            }

            this.read();
        }

        return writeFlag;
    }

    public boolean lock6C(byte[] password, int memBank, int lockType) {
        boolean lockFlag = false;
        byte[] cmd = new byte[11];
        cmd[0] = -96;
        cmd[1] = 9;
        cmd[2] = 1;
        cmd[3] = -125;
        System.arraycopy(password, 0, cmd, 4, password.length);
        cmd[8] = (byte)memBank;
        cmd[9] = (byte)lockType;
        cmd[10] = this.checkSum(cmd, cmd.length - 1);
        this.sendToReader(cmd);
        byte[] recv = this.read();
        if(recv != null) {
            if(recv.length == 6) {
                return lockFlag;
            }

            if(this.isRecvData(recv) == 0) {
                lockFlag = true;
            }
        } else {
            Log.i("Lock..6c", "锟斤拷时");
        }

        return lockFlag;
    }

    public boolean kill6C(byte[] password) {
        boolean killFlag = false;
        byte[] cmd = new byte[9];
        cmd[0] = -96;
        cmd[1] = 7;
        cmd[2] = -1;
        cmd[3] = -124;
        System.arraycopy(password, 0, cmd, 4, password.length);
        cmd[8] = this.checkSum(cmd, cmd.length - 1);
        byte[] recv = this.read();
        if(recv != null) {
            if(recv.length == 6) {
                return killFlag;
            }

            if(this.isRecvData(recv) == 0) {
                killFlag = true;
            }
        } else {
            Log.i("Kill ***", "锟斤拷时");
        }

        return killFlag;
    }

    public void inventory6B() {
        byte[] var10000 = new byte[]{-96, 3, 1, -80, 0};
    }

    private byte[] read() {
        int count = 0;
        int index = 0;

        byte[] resp = null;

        try {
            while(count < 3) {
                count = this.in.available();
                if(index > 50) {
                    return null;
                }

                ++index;

                try {
                    Thread.sleep(10L);
                } catch (InterruptedException var10) {
                    var10.printStackTrace();
                }
            }

            Thread.sleep(50L);
            int allCount = this.in.available();
            resp = new byte[allCount];
            Log.i("read allCount****", String.valueOf(allCount));

            for(int mcount = 0; count != 0; count = this.in.available()) {
                byte[] bytes = new byte[2];
                this.in.read(bytes);
                int length = bytes[1];
                count = this.in.available();
                Log.i("read count", String.valueOf(count));
                byte[] data = new byte[length];
                this.in.read(data);
                System.arraycopy(bytes, 0, resp, mcount, 2);
                System.arraycopy(data, 0, resp, mcount + 2, length);
                mcount = mcount + 2 + length;
            }
        } catch (Exception var11) {
            ;
        }

        return resp;
    }

    public boolean sendToReader(byte[] cmd) {
        try {
            this.out.write(cmd);
            this.out.flush();
            return false;
        } catch (IOException var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public byte checkSum(byte[] uBuff, int uBuffLen) {
        byte crc = 0;

        for(int i = 0; i < uBuffLen; ++i) {
            crc += uBuff[i];
        }

        crc = (byte)(255 & ~crc + 1);
        return crc;
    }

    public int isRecvData(byte[] recv) {
        if(recv.length < 5) {
            return -1;
        } else {
            String data = Tools.Bytes2HexString(recv, recv.length);
            if(recv[0] != -96) {
                return -2;
            } else {
                int recvDataLen = Integer.parseInt(data.substring(2, 4), 16);
                if(recvDataLen != recv.length - 2) {
                    return -3;
                } else {
                    byte crc = this.checkSum(recv, recv.length - 1);
                    return crc != recv[recv.length - 1]?-4:0;
                }
            }
        }
    }

    public void setSensitivity(int value) {
    }

    public void close() {
    }

    public byte checkSum(byte[] data) {
        return 0;
    }
}
