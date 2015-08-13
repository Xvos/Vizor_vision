package com.example.Camera.editor;

/**
 * Created by netherwire on 8/12/15.
 */
public enum FilterType
{
    FILTER_0(0),
    FILTER_1(1),
    FILTER_2(2),
    FILTER_3(3),
    FILTER_4(4),
    FILTER_5(5),
    FILTER_6(6),
    FILTER_7(7),
    FILTER_8(8),
    FILTER_9(9);

    private FilterType(int value)
    {
        this.value = value;
    }

    public final int getValue()
    {
        return value;
    }

    private final int value;
}
