package com.lolzdev.fabriccomputers.cpu.riscv;

import static com.lolzdev.fabriccomputers.cpu.riscv.Bus.DRAM_BASE;

public class Memory {
    public final int[][] content;

    public int last = 0;

    public Memory(int size) {
        this.content = new int[(int) Math.sqrt(size)][(int) Math.sqrt(size)];
        for (long i=0; i < size; i++) {
            this.set(i, 0);
        }
    }

    public int get(long index) {
        return content[(int) (index >> 32) & 0xFF][(int) (index) & 0xFF];
    }

    public void set(long index, int value) {
        content[(int) (index >> 32) & 0xFF][(int) (index) & 0xFF] = value;
        if (index > this.last) {
            this.last = (int)index;
        }
    }

    public long load(long address, int size) {
        switch (size) {
            case 8 -> {
                return load8(address);
            }
            case 16 -> {
                return load16(address);
            }
            case 32 -> {
                return load32(address);
            }
            case 64 -> {
                return load64(address);
            }
        }

        return -1;
    }

    private long load64(long address) {
        long index = (address - DRAM_BASE);
        return this.get(index) | (long) this.get(index + 1) << 8 | (long) this.get(index + 2) << 16 | (long) this.get(index + 3) << 24 | (long)this.get(index + 4) << 32 | (long)this.get(index + 5) << 40 | (long)this.get(index + 6) << 48 | (long)this.get(index + 7) << 56;
    }

    private long load32(long address) {
        long index = (address - DRAM_BASE);
        return this.get(index) | (long) this.get(index + 1) << 8 | (long) this.get(index + 2) << 16 | (long) this.get(index + 3) << 24;
    }

    private long load16(long address) {
        long index = (address - DRAM_BASE);
        return this.get(index) | (long) this.get(index + 1) << 8;
    }

    private long load8(long address) {
        long index = (address - DRAM_BASE);
        return this.get(index);
    }

    public void store(long address, int size, long value) {
        switch (size) {
            case 8 -> store8(address, value);
            case 16 -> store16(address, value);
            case 32 -> store32(address, value);
            case 64 -> store64(address, value);
        }
    }

    private void store64(long address, long value) {
        long index = (address - DRAM_BASE);
        this.set(index, (int)(value & 0xFF));
        this.set(index + 1, (int)((value >> 8) & 0xFF));
        this.set(index + 2, (int)((value >> 16) & 0xFF));
        this.set(index + 3, (int)((value >> 24) & 0xFF));
        this.set(index + 4, (int)((value >> 32) & 0xFF));
        this.set(index + 5, (int)((value >> 40) & 0xFF));
        this.set(index + 6, (int)((value >> 48) & 0xFF));
        this.set(index + 7, (int)((value >> 56) & 0xFF));
    }

    private void store32(long address, long value) {
        long index = (address - DRAM_BASE);
        this.set(index, (int)(value & 0xFF));
        this.set(index + 1, (int)((value >> 8) & 0xFF));
        this.set(index + 2, (int)((value >> 16) & 0xFF));
        this.set(index + 3, (int)((value >> 24) & 0xFF));
    }

    private void store16(long address, long value) {
        long index = (address - DRAM_BASE);
        this.set(index, (int)(value & 0xFF));
        this.set(index + 1, (int)((value >> 8) & 0xFF));
    }

    private void store8(long address, long value) {
        long index = (address - DRAM_BASE);
        this.set(index, (int)(value & 0xFF));
    }
}
