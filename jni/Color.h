//
// Created by Roman Chehowsky on 8/9/15.
//

#ifndef VIZOR_VISION_COLOR_H
#define VIZOR_VISION_COLOR_H

#include "Blendmode.h"

class Color
{
public:
    unsigned int R, G, B;

    static const unsigned int sm_fMask = 0x000000FF;

    Color() : R(0), G(0), B(0)
    {

    }

    Color(unsigned int r, unsigned int g, unsigned int b) : R(r), G(g), B(b)
    {

    }

    static __inline__ void fromABGR(Color* color, unsigned int agbrColor)
    {
        color->B = ((agbrColor >> 16) & sm_fMask);
        color->G = ((agbrColor >> 8) & sm_fMask);
        color->R = ((agbrColor) & sm_fMask);
    }

    __inline__ unsigned int toABGR() const
    {
        return /*(((unsigned int)A) << 24) & 0xFF000000 |*/
               ((B) << 16) & 0x00FF0000 |
               ((G) <<  8) & 0x0000FF00 |
               ((R)) & 0x000000FF;
    }

    __inline__ void makeGrayscale()
    {
        const unsigned int lum = (unsigned int)(((float)R * 0.3f) + ((float)G * 0.59f) + ((float)B * 0.11f));
        R = lum;
        G = lum;
        B = lum;
    }
};

#endif //VIZOR_VISION_COLOR_H
