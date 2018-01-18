
#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include <assert.h>

#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>
#include <pthread.h>
#include <unistd.h>

#include "android/log.h"
//#include "libbarcode.h"


static const char *TAG = "Zst_uart";

//#define LOGI(fmt, args...) 0
//#define LOGD(fmt, args...) 0
//#define LOGE(fmt, args...) 0
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)


#define   WD210_10                                                 10
#define   WD210_OTG                                                     17
#define   BARCODE_RTS                                                 186
#define   BARCODE_CTS                                                 187
#define   BARCODE_TRIGEGR                                             75
#define   BARCODE_PWRDWN                                             102


static char *kClassName = "utils/ZstBarcodeCtrl";
#if 0
int barcode_set(char *cmdstr ,char *resp);

int NEO_GpioSetValue(int gpio_n,int value);
int NEO_GpioGetValue(int gpio_n);
int NEO_GpioSetDir(int gpio_n,int dir,int value);
int NEO_GpioGetDir(int gpio_n);







void native_barCodeSet(JNIEnv *env, jobject thiz, jint state)
{
    LOGI("Native set barcode io:%d", state);

    NEO_GpioSetDir(WD210_OTG,1,0);

    if(state)
        NEO_GpioSetValue(WD210_OTG,1);
    else
        NEO_GpioSetValue(WD210_OTG,0);
}

void native_Setgpio10(JNIEnv *env, jobject thiz, jint state)
{
    LOGI("Native set Setgpio10 io:%d", state);

    NEO_GpioSetDir(WD210_10,1,0);

    if(state)
        NEO_GpioSetValue(WD210_10,1);
    else
        NEO_GpioSetValue(WD210_10,0);
}
#endif
/////////////////////////FOR 8937//////////////////////////////////


#define NEO_GPIO_DEVIO 'N'

#define NEO_IOCTL_GPIO_SET_VALUE                _IOW(NEO_GPIO_DEVIO,1,char[3])
#define NEO_IOCTL_GPIO_GET_VALUE                _IOR(NEO_GPIO_DEVIO,2, char[3])
#define NEO_IOCTL_GPIO_SET_DIR           _IOW(NEO_GPIO_DEVIO,3,char[3])
#define NEO_IOCTL_GPIO_GET_DIR              _IOR(NEO_GPIO_DEVIO,4,char[3])

#define WD220B_SET_OTG                       48
#define WD220B_USB_PWR                       59

#define WD220_809_OTG                        17

#if 0
void native_set_wd220B_OTG(JNIEnv *env, jobject thiz, jint state)
{
    LOGI("Native set Setgpio48 io:%d", state);
    char arg[3];
    int fd=-1,cmd,ret;
    fd = open("/dev/neo_gpio_dev" ,O_RDWR);
//		if(fd < 0)
//			return JNI_FALSE;

    cmd = NEO_IOCTL_GPIO_SET_VALUE;
    arg[0] = WD220B_SET_OTG;
    if(state>0) arg[1] = 1;
    else arg[1]=0;
    ret = ioctl(fd, cmd, &arg);

    arg[0] = WD220B_USB_PWR;
    if(state>0) arg[1] = 0;
    else arg[1]=1;
    ret = ioctl(fd, cmd, &arg);


    close(fd);
//	if( ret < 0) return JNI_FALSE;
//	return JNI_TRUE;


}
void native_set_wd220_809otg(JNIEnv *env, jobject thiz, jint state)
{

    char arg[3];
    int fd=-1,cmd,ret;
    fd = open("/dev/neo_gpio_dev" ,O_RDWR);


    cmd = NEO_IOCTL_GPIO_SET_VALUE;
    arg[0] = WD220_809_OTG;
    if(state>0) arg[1] = 1;
    else arg[1]=0;
    ret = ioctl(fd, cmd, &arg);




    close(fd);



}

void native_set_wd220B_OTG(JNIEnv *env, jobject thiz, jint state)
{
    LOGI("Native set Setgpio48 io:%d", state);
    char arg[3];
    int fd=-1,cmd,ret;
    fd = open("/dev/neo_gpio_dev" ,O_RDWR);
//		if(fd < 0)
//			return JNI_FALSE;

    cmd = NEO_IOCTL_GPIO_SET_VALUE;
    arg[0] = WD220B_SET_OTG;
    if(state>0) arg[1] = 1;
    else arg[1]=0;
    ret = ioctl(fd, cmd, &arg);

    arg[0] = WD220B_USB_PWR;
    if(state>0) arg[1] = 0;
    else arg[1]=1;
    ret = ioctl(fd, cmd, &arg);


    close(fd);
//	if( ret < 0) return JNI_FALSE;
//	return JNI_TRUE;


}
#endif

void native_set_gpio(JNIEnv *env, jobject thiz, jint state, jint pin_num) {

    char arg[3];

    int fd = -1, cmd, ret;
    fd = open("/dev/neo_gpio_dev", O_RDWR);

    if (state == 2) cmd = NEO_IOCTL_GPIO_SET_DIR;
    else cmd = NEO_IOCTL_GPIO_SET_VALUE;
    arg[0] = pin_num;
    if (state == 1) arg[1] = 1;
    else arg[1] = 0;
    ret = ioctl(fd, cmd, &arg);
    close(fd);
}

#if 0
void native_set_USB_PWR(JNIEnv *env, jobject thiz, jint state)
{

    char arg[3];
    int fd=-1,cmd,ret;
    fd = open("/dev/neo_gpio_dev" ,O_RDWR);
cmd = NEO_IOCTL_GPIO_SET_VALUE;

    arg[0] = WD220B_USB_PWR;
    if(state>0) arg[1] = 1;
    else arg[1]=0;
    ret = ioctl(fd, cmd, &arg);
    close(fd);


}
#endif
static JNINativeMethod gMethods[] = {
//	{ "Setgpio42",   "(I)V",     (void*) native_set_wd220B_OTG }, 
//	{ "Set_WD220B_OTG",   "(I)V",     (void*) native_set_wd220B_OTG_V2 },
//	{ "Set_WD220B_USB_PWR",   "(I)V",     (void*) native_set_USB_PWR },
//	{ "Set_WD220_809_PWR",   "(I)V",     (void*) native_set_wd220_809otg},

        {"set_gpio", "(II)V", (void *) native_set_gpio},

};


static int registerNativeMethods(JNIEnv *env, const char *className,
                                 JNINativeMethod *gMethods, int numMethods) {
    jclass clazz;

    clazz = (*env)->FindClass(env, className);
    //(*env)->FindClass(env,classname);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if ((*env)->RegisterNatives(env, clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}


/* This function will be call when the library first be load.
* You can do some init in the libray. return which version jni it support.
*/
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    void *venv;
    //当库第一次加载时，这个函数将被调用
    if ((*vm)->GetEnv(vm, (void **) &venv, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }

    if (!registerNativeMethods((JNIEnv *) venv, kClassName, gMethods,
                               sizeof(gMethods) / sizeof(gMethods[0]))) {
        return -1;
    }

    return JNI_VERSION_1_4;
}






