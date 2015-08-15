# без этой строчки никого STL (включая string) мы не дождемся
#APP_STL := stlport_static

#Собираем релизную версию
APP_OPTIM := release

#Собираем все ABI
APP_ABI := armeabi armeabi-v7a x86 mips

APP_CFLAGS += -Ofast

