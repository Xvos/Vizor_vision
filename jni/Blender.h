//
// Created by Roman Chehowsky on 8/9/15.
//

#ifndef VIZOR_VISION_BLENDER_H
#define VIZOR_VISION_BLENDER_H

#include "Core.h"
#include "Color.h"
#include "EBlendFunction.h"

#define LUM(color) (((f32)color.R * .3f) + ((f32)color.G * .59f) + ((f32)color.B * .11f))

#define ALPHA_BLEND(__cColor, __rColor, __function, __bopacity, __lopacity)\
        __cColor.A = __cColor.A;\
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


FORCEINLINE void blender_fastGrayscale(void* data, s32 size)
{
    u32* bytes = (unsigned int*)data;
    u32* lastPtr = bytes + (size / sizeof(unsigned int));

    color_t cColor;

    while (bytes++ != lastPtr)
    {
        color_colorFromABGR(&cColor, *bytes);

        cColor.R = LUM(cColor);
        cColor.G = cColor.R;
        cColor.B = cColor.R;

        color_colorToABGR(&cColor, bytes);
    }
}


FORCEINLINE void blender_blendImage(void* data1, void* data2, s32 size)
{
    u32* bytes1 = (u32*)data1;
    u32* bytes2 = (u32*)data2;

    u32* lastPtr = bytes1 + (size / sizeof(u32));

    color_t c1, c2;

    while (bytes1++ != lastPtr)
    {
        color_colorFromABGR(&c1, *bytes1);
        color_colorFromABGR(&c2, *bytes2);

        ALPHA_BLEND(c1, c2, ChannelBlend_SoftLight, 200, 56);

        color_colorToABGR(&c1, bytes1);

        bytes2++;
    }
}


/*
 * Data - data in ABRG8 format.
 * Size - size in bytes of the data
 * Mode - function to blend
 * IntColor - color to blend with as an integer
 * Float - opacity
 */
FORCEINLINE void blender_Process(void* data, u32 size, s32 mode, s32 intColor, float opacity)
{
    color_t rColor;
    color_colorFromARGB(&rColor, (u32)intColor);

    E_BLEND_FUNCTION blend_function = (E_BLEND_FUNCTION)mode;

    switch (blend_function)
    {
        case EBF_NORMAL: {
            BLEND_FILTER(data, size, ChannelBlend_Normal, rColor, opacity);
            break;
        }

        case EBF_LIGHTEN: {
            BLEND_FILTER(data, size, ChannelBlend_Lighten, rColor, opacity);
            break;
        }

        case EBF_DARKEN: {
            BLEND_FILTER(data, size, ChannelBlend_Darken, rColor, opacity);
            break;
        }

        case EBF_MULTIPLY: {
            BLEND_FILTER(data, size, ChannelBlend_Multiply, rColor, opacity);
            break;
        }

        case EBF_AVERAGE: {
            BLEND_FILTER(data, size, ChannelBlend_Average, rColor, opacity);
            break;
        }

        case EBF_ADD: {
            BLEND_FILTER(data, size, ChannelBlend_Add, rColor, opacity);
            break;
        }



        case EBF_SUBTRACT: {
            BLEND_FILTER(data, size, ChannelBlend_Subtract, rColor, opacity);
            break;
        }

        case EBF_DIFFERENCE: {
            BLEND_FILTER(data, size, ChannelBlend_Difference, rColor, opacity);
            break;
        }

        case EBF_NEGATION: {
            BLEND_FILTER(data, size, ChannelBlend_Negation, rColor, opacity);
            break;
        }

        case EBF_SCREEN: {
            BLEND_FILTER(data, size, ChannelBlend_Screen, rColor, opacity);
            break;
        }

        case EBF_EXCLUSION: {
            BLEND_FILTER(data, size, ChannelBlend_Exclusion, rColor, opacity);
            break;
        }

        case EBF_OVERLAY: {
            BLEND_FILTER(data, size, ChannelBlend_Overlay, rColor, opacity);
            break;
        }

        case EBF_SOFT_LIGHT: {
            BLEND_FILTER(data, size, ChannelBlend_SoftLight, rColor, opacity);
            break;
        }

        case EBF_HARD_LIGHT: {
            BLEND_FILTER(data, size, ChannelBlend_HardLight, rColor, opacity);
            break;
        }

        case EBF_COLOR_DODGE: {
            BLEND_FILTER(data, size, ChannelBlend_ColorDodge, rColor, opacity);
            break;
        }

        case EBF_COLOR_BURN: {
            BLEND_FILTER(data, size, ChannelBlend_ColorBurn, rColor, opacity);
            break;
        }

        case EBF_LINEAR_DODGE: {
            BLEND_FILTER(data, size, ChannelBlend_LinearDodge, rColor, opacity);
            break;
        }

        case EBF_LINEAR_BURN: {
            BLEND_FILTER(data, size, ChannelBlend_LinearBurn, rColor, opacity);
            break;
        }

        case EBF_LINEAR_LIGHT: {
            BLEND_FILTER(data, size, ChannelBlend_LinearLight, rColor, opacity);
            break;
        }

        case EBF_VIVID_LIGHT: {
            BLEND_FILTER(data, size, ChannelBlend_VividLight, rColor, opacity);
            break;
        }

        case EBF_PIN_LIGHT: {
            BLEND_FILTER(data, size, ChannelBlend_PinLight, rColor, opacity);
            break;
        }

        case EBF_HARD_MIX: {
            BLEND_FILTER(data, size, ChannelBlend_HardMix, rColor, opacity);
            break;
        }

        case EBF_REFLECT: {
            BLEND_FILTER(data, size, ChannelBlend_Reflect, rColor, opacity);
            break;
        }

        case EBF_GLOW: {
            BLEND_FILTER(data, size, ChannelBlend_Glow, rColor, opacity);
            break;
        }

        case EBF_PHOENIX: {
            BLEND_FILTER(data, size, ChannelBlend_Phoenix, rColor, opacity);
            break;
        }
    }
}

#ifdef __cplusplus
}
#endif



#endif //VIZOR_VISION_BLENDER_H
