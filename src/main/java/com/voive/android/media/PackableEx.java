package com.voive.android.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
