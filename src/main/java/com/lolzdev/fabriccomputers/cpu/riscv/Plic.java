package com.lolzdev.fabriccomputers.cpu.riscv;

import net.minecraft.util.Pair;

import java.util.Optional;

import static com.lolzdev.fabriccomputers.cpu.riscv.Bus.PLIC_BASE;

public class Plic implements Device {

    // The address of interrupt pending bits.
    public static final long PLIC_PENDING = PLIC_BASE + 0x1000;
    /// The address of the regsiters to enable interrupts for S-mode.
    public static final long PLIC_SENABLE = PLIC_BASE + 0x2080;
    /// The address of the registers to set a priority for S-mode.
    public static final long PLIC_SPRIORITY = PLIC_BASE + 0x201000;
    /// The address of the claim/complete registers for S-mode.
    public static final long PLIC_SCLAIM = PLIC_BASE + 0x201004;

    long pending = 0, senable = 0, spriority = 0, sclaim = 0;

    public Pair<Long, Optional<Exception>> load32(long addr) {
            if ( addr == PLIC_PENDING) { return new Pair<>(this.pending, Optional.empty()); }
            else if ( addr == PLIC_SENABLE) { return new Pair<>(this.senable, Optional.empty()); }
            else if ( addr == PLIC_SPRIORITY) { return new Pair<>(this.spriority, Optional.empty()); }
            else if ( addr == PLIC_SCLAIM) { return new Pair<>(this.sclaim, Optional.empty()); }
            else { return new Pair<>(0L, Optional.of(Exception.LoadAccessFault)); }

    }

    public void store32(long addr, long value) {
        if ( addr == PLIC_PENDING) { this.pending = value; }
        else if ( addr == PLIC_SENABLE) { this.senable = value; }
        else if ( addr == PLIC_SPRIORITY) { this.spriority = value; }
        else if ( addr == PLIC_SCLAIM) { this.sclaim = value; }
    }

    @Override
    public Pair<Long, Optional<Exception>> load(long addr, int size) {
        if (size == 32) {
            return this.load32(addr);
        }

        return new Pair<>(0L, Optional.of(Exception.LoadAccessFault));
    }

    @Override
    public Optional<Exception> store(long addr, int size, long value) {
        if (size == 32) {
            this.store32(addr, value);
        }

        return Optional.of(Exception.StoreAMOAccessFault);
    }
}
