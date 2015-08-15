//
// Created by Roman Chehowsky on 8/9/15.
//

#ifndef VIZOR_VISION_BLENDER_H
#define VIZOR_VISION_BLENDER_H

#include "Core.h"
#include "Color.h"

#define LUM(color) (((f32)color.R * .3f) + ((f32)color.G * .59f) + ((f32)color.B * .11f))

#define ALPHA_BLEND(__cColor, __rColor, __function, __bopacity, __lopacity)\
        __cColor.R = ChannelBlend_AlphaF(__cColor.R, __rColor.R, __function, __bopacity, __lopacity);\
        __cColor.G = ChannelBlend_AlphaF(__cColor.G, __rColor.G, __function, __bopacity, __lopacity);\
        __cColor.B = ChannelBlend_AlphaF(__cColor.B, __rColor.B, __function, __bopacity, __lopacity);

#define BLEND_FILTER(data, size, function, rColor, opacity)\
        u32* bytes = (u32*)data;\
        u32* lastPtr = bytes + (size / sizeof(u32));\
        u32 bOpacity = (u32)(opacity * 256.0f);\
        u32 lOpacity = 256 - bOpacity;\
        color_t cColor;\
        while (bytes++ != lastPtr)\
        {\
            color_colorFromABGR(&cColor, *bytes);\
            ALPHA_BLEND(cColor, rColor, function, bOpacity, lOpacity);\
            color_colorToABGR(&cColor, bytes);\
        }

#ifdef __cplusplus
extern "C" {
#endif

void blender_Process(void* data, u32 size, s32 mode)
{
    switch (mode)
    {
        case 0: {
            //SoftLight, 97 67 20, 70%
            color_t rColor = color_fromComponents(97, 67, 20);
            BLEND_FILTER(data, size, ChannelBlend_SoftLight, rColor, 0.70f);
            break;
        }
        case 1: {
            //ColorDodge, 97 89 20, 50%
            color_t rColor = color_fromComponents(97, 89, 20);
            BLEND_FILTER(data, size, ChannelBlend_ColorDodge, rColor, 0.5f);
            break;
        }
        case 2: {
            //Overlay, 195 153 128, 50%
            color_t rColor = color_fromComponents(195, 153, 128);
            BLEND_FILTER(data, size, ChannelBlend_Overlay, rColor, 0.5f);
            break;
        }
        case 3: {
            //HardLight, 148 124 172, 50%
            color_t rColor = color_fromComponents(148, 124, 172);
            BLEND_FILTER(data, size, ChannelBlend_HardLight, rColor, 0.5f);
            break;
        }
        case 4: {
            //Lightin 10%, 255 243 214
            color_t rColor = color_fromComponents(255, 243, 214);
            BLEND_FILTER(data, size, ChannelBlend_HardLight, rColor, 0.1f);
            break;
        }
    }
}

#ifdef __cplusplus
}
#endif

//    FORCEINLINE void fast_grayscale(void* data, s32 size)
//    {
//        unsigned int* bytes = (unsigned int*)data;
//        unsigned int* lastPtr = bytes + (size / sizeof(unsigned int));
//
//        Color cColor;
//        unsigned char lum;
//
//        while (bytes++ != lastPtr)
//        {
//            Color::fromABGR(&cColor, *bytes);
//
//            lum = LUM(cColor);
//            cColor.R = lum;
//            cColor.G = lum;
//            cColor.B = lum;
//
//            *bytes = cColor.toABGR();
//        }
//    }

#endif //VIZOR_VISION_BLENDER_H
