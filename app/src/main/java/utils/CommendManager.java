package utils;

import java.util.List;

/**
 * Created by administrator on 2018-01-02.
 */

public interface CommendManager {
    boolean setBaudrate();

    byte[] getFirmware();

    boolean setOutputPower(int var1);

    List<byte[]> inventoryRealTime();

    void selectEPC(byte[] var1);

    byte[] readFrom6C(int var1, int var2, int var3, byte[] var4);

    boolean writeTo6C(byte[] var1, int var2, int var3, int var4, byte[] var5);

    void setSensitivity(int var1);

    boolean lock6C(byte[] var1, int var2, int var3);

    boolean kill6C(byte[] var1);

    void close();

    byte checkSum(byte[] var1);

    int setFrequency(int var1, int var2, int var3);
}
