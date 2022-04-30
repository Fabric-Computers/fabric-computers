package com.lolzdev.fabriccomputers.cpu.riscv;

import net.minecraft.util.Pair;

import java.util.Optional;

import static com.lolzdev.fabriccomputers.cpu.riscv.Bus.CLINT_BASE;

public class Clint implements Device {

    public static final long CLINT_MTIMECMP = CLINT_BASE + 0x4000;
    public static final long CLINT_MTIME = CLINT_BASE + 0xbff8;

    public long mtime = 0, mtimecmp = 0;

    public Pair<Long, Optional<Exception>> load64(long addr) {
        if ( addr == CLINT_MTIME) { return new Pair<>(this.mtime, Optional.empty()); }
        else if ( addr == CLINT_MTIMECMP) { return new Pair<>(this.mtimecmp, Optional.empty()); }
        else { return new Pair<>(0L, Optional.of(Exception.LoadAccessFault)); }

    }

    public void store64(long addr, long value) {
        if ( addr == CLINT_MTIME) { this.mtime = value; }
        else if ( addr == CLINT_MTIMECMP) { this.mtimecmp = value; }
    }

    @Override
    public Pair<Long, Optional<Exception>> load(long addr, int size) {
        if (size == 64) {
            return this.load64(addr);
        }

        return new Pair<>(0L, Optional.of(Exception.LoadAccessFault));
    }

    @Override
    public Optional<Exception> store(long addr, int size, long value) {
        if (size == 64) {
            this.store64(addr, value);
        }

        return Optional.of(Exception.StoreAMOAccessFault);
    }
}
