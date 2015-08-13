//
// Created by Roman Chehowsky on 8/9/15.
//

#ifndef VIZOR_VISION_COLOR_H
#define VIZOR_VISION_COLOR_H

#include "Core.h"
#include "Blendmode.h"

class Color
{
public:
    static const u32 sm_fMask = 0x000000FF;

    Color() : R(0), G(0), B(0)
    {

    }

    Color(u32 r, u32 g, u32 b) : R(r), G(g), B(b)
    {

    }

    FORCEINLINE static void fromABGR(Color* color, u32 agbrColor)
    {
        color->B = ((agbrColor >> 16) & sm_fMask);
        color->G = ((agbrColor >> 8) & sm_fMask);
        color->R = ((agbrColor) & sm_fMask);
    }

    FORCEINLINE u32 toABGR() const
    {
        return ((B) << 16) & 0x00FF0000 |
               ((G) <<  8) & 0x0000FF00 |
               ((R)) & 0x000000FF;
    }

    u32 R, G, B;
};

#endif //VIZOR_VISION_COLOR_H
