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

/*
 * Data - data in ABRG8 format.
 * Size - size in bytes of the data
 * Mode - function to blend
 * IntColor - color to blend with as an integer
 * Float - opacity
 */
void blender_Process(void* data, u32 size, s32 mode, s32 intColor, float opacity)
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
