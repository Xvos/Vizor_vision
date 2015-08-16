//
// Created by Roman Chehowsky on 8/14/15.
//

#ifndef VIZOR_VISION_CORE_H
#define VIZOR_VISION_CORE_H


// Optimization macros

// Always force inline?
#define USE_FORCE_INLINE 1

// Min max without ifs?
#define USE_BRANCHLESS_MINMAX 1


// Inline

#ifdef USE_FORCE_INLINE
    #define FORCEINLINE __inline__
#else
    #define FORCEINLINE inline
#endif

// Type definitions

typedef unsigned char u8;
typedef signed char s8;

typedef unsigned short u16;
typedef signed short s16;

typedef unsigned int u32;
typedef signed int s32;

typedef unsigned long u64;
typedef signed long s64;

typedef float f32;
typedef double f64;

// True, false

#define TRUE 1
#define FALSE 0

// Branchless minmax

#ifdef USE_BRANCHLESS_MINMAX
    #define qMax(a, b) (a > b) * a + (a <= b) * b
    #define qMin(a, b) (a > b) * b + (a <= b) * a
#else
    #define qMax(a, b) a > b ? a : b
    #define qMin(a, b) a > b ? b : a
#endif

#define qAbs(a) a > 0 ? a : -a

#endif //VIZOR_VISION_CORE_H
