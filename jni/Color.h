//
// Created by Roman Chehowsky on 8/9/15.
//

#ifndef VIZOR_VISION_COLOR_H
#define VIZOR_VISION_COLOR_H

#include "Core.h"
#include "Blendmode.h"

#ifdef __cplusplus
extern "C" {
#endif

struct color_t {
    u32 A;
    u32 R;
    u32 G;
    u32 B;
};

color_t color_fromComponents(u32 A, u32 R, u32 G, u32 B)
{
    color_t newColor;

    newColor.A = A;
    newColor.R = R;
    newColor.G = G;
    newColor.B = B;

    return newColor;
}

void color_colorFromARGB(color_t *color, const u32 abgrColor) {
    color->A = ((abgrColor >> 24) & 0x000000FF);
    color->R = ((abgrColor >> 16) & 0x000000FF);
    color->G = ((abgrColor >> 8) & 0x000000FF);
    color->B = ((abgrColor) & 0x000000FF);
}

void color_colorFromABGR(color_t *color, const u32 abgrColor) {
    color->A = ((abgrColor >> 24) & 0x000000FF);
    color->B = ((abgrColor >> 16) & 0x000000FF);
    color->G = ((abgrColor >> 8) & 0x000000FF);
    color->R = ((abgrColor) & 0x000000FF);
}

void color_colorToABGR(const color_t *color, u32 *abgrColor) {
    *abgrColor =
            ((color->A) << 24) & 0xFF000000 |
            ((color->B) << 16) & 0x00FF0000 |
            ((color->G) <<  8) & 0x0000FF00 |
             (color->R) & 0x000000FF;
}

#ifdef __cplusplus
}
#endif

#endif //VIZOR_VISION_COLOR_H
