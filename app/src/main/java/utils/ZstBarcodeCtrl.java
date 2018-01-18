package utils;

import android.util.Log;

public class ZstBarcodeCtrl {

   private static ZstBarcodeCtrl mZstBarcodeCtrl = new ZstBarcodeCtrl();
   
   private ZstBarcodeCtrl(){ }

   public static ZstBarcodeCtrl getInstance(){
      return mZstBarcodeCtrl; 
   }
   
   
  // public native void Setgpio42(int state);
  // public native void Set_WD220B_OTG(int state);
  // public native void Set_WD220B_USB_PWR(int state);
  // public native void Set_WD220_809_PWR(int state);
   public native void set_gpio(int state,int pin_num);
   static {
      System.loadLibrary("gpio");
       Log.e("TAG", "static initializer:  加载 gpio" );
   }
   
}