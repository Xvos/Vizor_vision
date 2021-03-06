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

#define PREMULTIPLY_ALPHA(color)\
    color->R = (color->R * color->A) / 255;\
    color->G = (color->G * color->A) / 255;\
    color->B = (color->B * color->A) / 255;

#define UNPREMULTIPLY_ALPHA(color)\
    color->R = (color->R * 255) / color->A;\
    color->G = (color->G * 255) / color->A;\
    color->B = (color->B * 255) / color->A;

struct color_t {
    // Знаковые инты нужны для корректного сравнения в наших прекрасных режимах смешивания.
    s32 A, R, G, B;
};

color_t color_fromComponents(s32 A, s32 R, s32 G, s32 B)
{
    color_t newColor;

    newColor.A = A;
    newColor.R = R;
    newColor.G = G;
    newColor.B = B;

    return newColor;
}

void color_colorFromARGB(color_t *color, const u32 abgrColor)
{
    color->A = (s32)((abgrColor >> 24) & 0x000000FF);
    color->R = (s32)((abgrColor >> 16) & 0x000000FF);
    color->G = (s32)((abgrColor >>  8) & 0x000000FF);
    color->B = (s32)(abgrColor & 0x000000FF);

    UNPREMULTIPLY_ALPHA(color);
}

void color_colorFromABGR(color_t *color, const u32 abgrColor) {
    color->A = (s32)((abgrColor >> 24) & 0x000000FF);
    color->B = (s32)((abgrColor >> 16) & 0x000000FF);
    color->G = (s32)((abgrColor >> 8) & 0x000000FF);
    color->R = (s32)(abgrColor & 0x000000FF);
}

void color_colorToABGR(color_t *color, u32 *abgrColor)
{
    PREMULTIPLY_ALPHA(color);

    *abgrColor =
            (((u32)color->A) << 24) & 0xFF000000 |
            (((u32)color->B) << 16) & 0x00FF0000 |
            (((u32)color->G) <<  8) & 0x0000FF00 |
             ((u32)color->R) & 0x000000FF;
}

#ifdef __cplusplus
}
#endif

#endif //VIZOR_VISION_COLOR_H
