package com.lolzdev.fabriccomputers.cpu.riscv;

import net.minecraft.util.Pair;

import java.util.Optional;

public class Bus {
    public static final long DRAM_BASE = 0x0;
    public static final long DRAM_SIZE = 1024 * 1024 * 128;

    /// The address which the platform-level interrupt controller (PLIC) starts. The PLIC connects all external interrupts in the
    /// system to all hart contexts in the system, via the external interrupt source in each hart.
    public static final long PLIC_BASE = DRAM_BASE + DRAM_SIZE + 1;
    /// The size of PLIC.
    public static final long PLIC_SIZE = 0x400_0000;

    // The address which the core-local interruptor (CLINT) starts. It contains the timer and
    // generates per-hart software interrupts and timer
    // interrupts.
    public static final long CLINT_BASE = PLIC_BASE + PLIC_SIZE + 1;
    /// The size of CLINT.
    public static final long CLINT_SIZE = 0x10000;

    public Memory dram = new Memory((int) DRAM_SIZE);
    public Clint clint = new Clint();
    public Plic plic = new Plic();

    public Pair<Long, Optional<Exception>> load(long address, int size) {
        if (CLINT_BASE <= address && address < CLINT_BASE + CLINT_SIZE) {
            return clint.load(address, size);
        }
        if (PLIC_BASE <= address && address < PLIC_BASE + PLIC_SIZE) {
            return plic.load(address, size);
        }
        if (DRAM_BASE <= address) {
            return new Pair<>(dram.load(address, size), Optional.empty());
        }

        return new Pair<>(0L, Optional.of(Exception.LoadAccessFault));
    }

    public void store(long address, int size, long value) {
        if (DRAM_BASE <= address) {
            dram.store((int)address, size, value);
        }
    }

}
