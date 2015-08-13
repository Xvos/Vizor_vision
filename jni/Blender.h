//
// Created by Roman Chehowsky on 8/9/15.
//

#ifndef VIZOR_VISION_BLENDER_H
#define VIZOR_VISION_BLENDER_H

#include "Color.h"

#define LUM(color) (((float)color.R * .3f) + ((float)color.G * .59f) + ((float)color.B * .11f))

#define ALPHA_BLEND(__cColor, __rColor, __function, __opacity)\
        __cColor.R = ChannelBlend_AlphaF(__cColor.R, __rColor.R, __function, __opacity);\
        __cColor.G = ChannelBlend_AlphaF(__cColor.G, __rColor.G, __function, __opacity);\
        __cColor.B = ChannelBlend_AlphaF(__cColor.B, __rColor.B, __function, __opacity);

#define BLEND_FILTER(MACRO_data, MACRO_size, MACRO_function, MACRO_rColor, MACRO_opacity)\
        unsigned int* bytes = (unsigned int*)MACRO_data;\
        unsigned int* lastPtr = bytes + (MACRO_size / sizeof(unsigned int));\
        Color cColor;\
        while (bytes++ != lastPtr)\
        {\
            Color::fromABGR(&cColor, *bytes);\
            ALPHA_BLEND(cColor, MACRO_rColor, MACRO_function, MACRO_opacity);\
            *bytes = cColor.toABGR();\
        }

class Blender
{
public:
    __inline__ void process(void* data, int size, int mode)
    {
        switch (mode)
        {
            case 0: {
                Color rColor(97, 89, 20);
                BLEND_FILTER(data, size, ChannelBlend_ColorDodge, rColor, .5f);
                break;
            }
            case 1: {
                Color rColor(148, 124, 172);
                BLEND_FILTER(data, size, ChannelBlend_HardLight, rColor, .5f);
                break;
            }
        }

    }

    __inline__ void fast_grayscale(void* data, int size)
    {
        unsigned int* bytes = (unsigned int*)data;
        unsigned int* lastPtr = bytes + (size / sizeof(unsigned int));

        Color cColor;
        unsigned char lum;

        while (bytes++ != lastPtr)
        {
            Color::fromABGR(&cColor, *bytes);

            lum = LUM(cColor);
            cColor.R = lum;
            cColor.G = lum;
            cColor.B = lum;

            *bytes = cColor.toABGR();
        }
    }


};


#endif //VIZOR_VISION_BLENDER_H
