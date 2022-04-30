package com.lolzdev.fabriccomputers.cpu.riscv;

import net.minecraft.util.Pair;

import java.util.Optional;

public interface Device {
    Pair<Long, Optional<Exception>> load(long addr, int size);
    Optional<Exception> store(long addr, int size, long value);
}
