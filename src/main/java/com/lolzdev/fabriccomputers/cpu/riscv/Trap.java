package com.lolzdev.fabriccomputers.cpu.riscv;

import static com.lolzdev.fabriccomputers.cpu.riscv.Bus.DRAM_BASE;
import static com.lolzdev.fabriccomputers.cpu.riscv.Rv64.*;

public interface Trap {
    int exceptionCode();
    default void takeTrap(Rv64 cpu) {
        long exception_pc = cpu.pc;
        int previous_mode = cpu.mode;

        int cause = exceptionCode();

        System.out.println("Trap: " + this + " at: " + (exception_pc));
        if (previous_mode > 1 && ((cpu.loadCsr(MEDELEG) >> cause) & 1) != 0) {
            cpu.mode = 1;
            cpu.pc = cpu.loadCsr(STVEC) & ~1;
            cpu.storeCsr(SCAUSE, cause);
            cpu.storeCsr(STVAL, 0);
            if (((cpu.loadCsr(SSTATUS) >> 1) & 1) == 1) {
                cpu.storeCsr(SSTATUS, cpu.loadCsr(SSTATUS) | (1 << 5));
            } else {
                cpu.storeCsr(SSTATUS, cpu.loadCsr(SSTATUS) & ~(1 << 5));
            }
            cpu.storeCsr(SSTATUS, cpu.loadCsr(SSTATUS) & ~(1 << 1));
            if (previous_mode == 3) {
                cpu.storeCsr(SSTATUS, cpu.loadCsr(SSTATUS) & ~(1 << 8));
            } else {
                cpu.storeCsr(SSTATUS, cpu.loadCsr(SSTATUS) | (1 << 8));
            }
        } else {
            cpu.mode = 0;
            cpu.pc = cpu.loadCsr(MTVEC) & ~1;
            cpu.storeCsr(MEPC, exception_pc & ~1);
            cpu.storeCsr(MCAUSE, cause);
            cpu.storeCsr(MTVAL, 0);
            if (((cpu.loadCsr(MSTATUS) >> 3) & 1) == 1) {
                cpu.storeCsr(MSTATUS, cpu.loadCsr(MSTATUS) | (1 << 7));
            } else {
                cpu.storeCsr(MSTATUS, cpu.loadCsr(MSTATUS) & ~(1 << 7));
            }

            cpu.storeCsr(MSTATUS, cpu.loadCsr(MSTATUS) & ~(1 << 3));
            cpu.storeCsr(MSTATUS, cpu.loadCsr(MSTATUS) & ~(0b11 << 11));
        }
    }
}
