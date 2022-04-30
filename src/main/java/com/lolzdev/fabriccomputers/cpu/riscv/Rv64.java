package com.lolzdev.fabriccomputers.cpu.riscv;

import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.lolzdev.fabriccomputers.cpu.riscv.Bus.DRAM_BASE;

public class Rv64 {
    public int mode = 0;
    public int MEMORY_SIZE = 1024 * 1024 * 10;

    // Hardware thread ID.
    public static final int MHARTID = 0xf14;
    // Machine status register.
    public static final int MSTATUS = 0x300;
    // Machine exception delegation register.
    public static final int MEDELEG = 0x302;
    // Machine interrupt delegation register.
    public static final int MIDELEG = 0x303;
    // Machine interrupt-enable register.
    public static final int MIE = 0x304;
    // Machine trap-handler base address.
    public static final int MTVEC = 0x305;
    // Machine counter enable.
    public static final int MCOUNTEREN = 0x306;
    // Scratch register for machine trap handlers.
    public static final int MSCRATCH = 0x340;
    // Machine exception program counter.
    public static final int MEPC = 0x341;
    // Machine trap cause.
    public static final int MCAUSE = 0x342;
    // Machine bad address or instruction.
    public static final int MTVAL = 0x343;
    // Machine interrupt pending.
    public static final int MIP = 0x344;

    // Supervisor status register.
    public static final int SSTATUS = 0x100;
    // Supervisor interrupt-enable register.
    public static final int SIE = 0x104;
    // Supervisor trap handler base address.
    public static final int STVEC = 0x105;
    // Scratch register for supervisor trap handlers.
    public static final int SSCRATCH = 0x140;
    // Supervisor exception program counter.
    public static final int SEPC = 0x141;
    // Supervisor trap cause.
    public static final int SCAUSE = 0x142;
    // Supervisor bad address or instruction.
    public static final int STVAL = 0x143;
    // Supervisor interrupt pending.
    public static final int SIP = 0x144;
    // Supervisor address translation and protection.
    public static final int SATP = 0x180;

    long[] registers = new long[32];
    long[] csrs = new long[4096];
    long pc = DRAM_BASE;
    public Bus bus = new Bus();
    public boolean halted = true;

    public long loadCsr(long address) {
        if ((int)address == SIE) {
            return csrs[MIE] & csrs[MIDELEG];
        } else {
            return csrs[(int)address];
        }
    }

    public void storeCsr(long address, long value) {
        if ((int)address == SIE) {
            csrs[MIE] = (csrs[MIE] & ~csrs[MIDELEG]) | (value & csrs[MIDELEG]);
        } else {
            csrs[(int)address] = value;
        }
    }

    public Pair<Integer, Optional<Exception>> fetch() {
        Pair<Long, Optional<Exception>> exception = bus.load(pc, 32);
        int inst = exception.getLeft().intValue();

        return new Pair<>(inst, exception.getRight());
    }

    public Optional<Exception> execute(int instruction) {
        int opcode = instruction & 0x7f;
        int rd = (instruction & 0x00000f80) >> 7;
        int rs1 = (instruction & 0x000f8000) >> 15;
        int rs2 = (instruction & 0x01f00000) >> 20;
        int funct3 = (instruction & 0x00007000) >> 12;
        int funct7 = (instruction & 0xfe000000) >> 25;

        switch (opcode) {
            case 0x03 -> {
                long imm = ((instruction) >> 20);
                long address = (registers[rs1] + imm);
                switch (funct3) {
                    case 0x0 -> {
                        var exception = bus.load(address, 8);
                        long val = exception.getLeft();
                        if (exception.getRight().isPresent()) {
                            return exception.getRight();
                        }
                        registers[rd] = val;
                    }
                    case 0x1 -> {
                        var exception = bus.load(address, 16);
                        long val = exception.getLeft();
                        if (exception.getRight().isPresent()) {
                            return exception.getRight();
                        }
                        registers[rd] = val;
                    }
                    case 0x2 -> {
                        System.out.println("lw: " + (address - DRAM_BASE) + ", rs1: " + registers[rs1] + ", imm: " + imm + ", a+i: " + (registers[rs1] + imm));
                        var exception = bus.load(address, 32);
                        long val = exception.getLeft();
                        if (exception.getRight().isPresent()) {
                            return exception.getRight();
                        }
                        registers[rd] = val;
                    }
                    case 0x3 -> {
                        var exception = bus.load(address, 64);
                        long val = exception.getLeft();
                        if (exception.getRight().isPresent()) {
                            return exception.getRight();
                        }
                        registers[rd] = val;
                    }
                    default -> {
                        return Optional.of(Exception.IllegalInstruction);
                    }
                }
            }
            case 0x13 -> {
                long imm = ((instruction & 0xfff00000) >> 20);
                int shamt = (int)(imm & 0x3f);
                switch (funct3) {
                    // addi
                    case 0x0 -> registers[rd] = registers[rs1] + imm;
                    // slli
                    case 0x1 -> registers[rd] = registers[rs1] << shamt;
                    // slti
                    case 0x2 -> {
                        if (registers[rs1] < imm) {
                            registers[rd] = 1;
                        } else {
                            registers[rd] = 0;
                        }
                    }
                    // sltiu
                    case 0x3 -> {
                        if (Long.compareUnsigned(registers[rs1], imm) < 0) {
                            registers[rd] = 1;
                        } else {
                            registers[rd] = 0;
                        }
                    }
                    // xori
                    case 0x4 -> registers[rd] =  registers[rs1] ^ imm;
                    case 0x5 -> {
                        switch (funct7) {
                            // srli, srai
                            case 0x00, 0x10 -> registers[rd] = registers[rs1] >> shamt;
                            default -> {
                                return Optional.of(Exception.IllegalInstruction);
                            }
                        }
                    }
                    // ori
                    case 0x6 -> registers[rd] = registers[rs1] | imm;
                    // andi
                    case 0x7 -> registers[rd] = registers[rs1] & imm;
                    default -> {
                        return Optional.of(Exception.IllegalInstruction);
                    }
                }
            }
            case 0x17 -> {
                long imm = (instruction & 0xfffff000);
                registers[rd] = pc + imm - 4;
            }
            case 0x1b -> {
                long imm = (instruction >> 20);
                int shamt = (int)(imm & 0x1f);
                switch (funct3) {
                    // addiw
                    case 0x0 -> registers[rd] = registers[rs1] + imm;
                    // slliw
                    case 0x1 -> registers[rd] = registers[rs1] << shamt;
                    case 0x5 -> {
                        switch (funct7) {
                            // srliw, sraiw
                            case 0x00, 0x20 -> registers[rd] = registers[rs1] >> shamt;
                            default -> {
                                return Optional.of(Exception.IllegalInstruction);
                            }
                        }
                    }
                    default -> {
                        return Optional.of(Exception.IllegalInstruction);
                    }
                }
            }
            case 0x23 -> {
                long imm = (long)((instruction & 0xfe000000) >> 20) | ((instruction >> 7) & 0x1f);
                long address = (registers[rs1] + imm);
                switch (funct3) {
                    case 0x0 -> bus.store(address, 8, registers[rs2]);
                    case 0x1 -> bus.store(address, 16, registers[rs2]);
                    case 0x2 -> bus.store(address, 32, registers[rs2]);
                    case 0x3 -> bus.store(address, 64, registers[rs2]);
                    default -> {
                        return Optional.of(Exception.IllegalInstruction);
                    }
                }
            }
            case 0x33 -> {
                int shamt = (int)registers[rs2] & 0x3f;
                switch (funct3) {
                    case 0x0 -> {
                        switch (funct7) {
                            // add
                            case 0x00 -> registers[rd] = registers[rs1] + registers[rs2];
                            // mul
                            case 0x01 -> registers[rd] = registers[rs1] * registers[rs2];
                            // sub
                            case 0x20 -> registers[rd] = registers[rs1] - registers[rs2];
                            default -> {
                                return Optional.of(Exception.IllegalInstruction);
                            }
                        }
                    }
                    // sll
                    case 0x1 -> registers[rd] = registers[rs1] << shamt;
                    // slt
                    case 0x2 -> {
                        if (registers[rs1] < registers[rs2]) {
                            registers[rd] = 1;
                        } else {
                            registers[rd] = 0;
                        }
                    }
                    // sltu
                    case 0x3 -> {
                        if (Long.compareUnsigned(registers[rs1], registers[rs2]) < 0) {
                            registers[rd] = 1;
                        } else {
                            registers[rd] = 0;
                        }
                    }
                    // xor
                    case 0x4 -> registers[rd] = registers[rs1] ^ registers[rs2];
                    case 0x5 -> {
                        switch (funct7) {
                            // srl, sra
                            case 0x00, 0x20 -> registers[rd] = registers[rs1] >> shamt;
                            default -> {
                                return Optional.of(Exception.IllegalInstruction);
                            }
                        }
                    }
                    // or
                    case 0x6 -> registers[rd] = registers[rs1] | registers[rs2];
                    // and
                    case 0x7 -> registers[rd] = registers[rs1] & registers[rs2];
                    default -> {
                        return Optional.of(Exception.IllegalInstruction);
                    }
                }
            }
            // lui
            case 0x37 -> registers[rd] = instruction & 0xfffff000;
            case 0x3b -> {
                int shamt = (int)(registers[rs2] & 0x1f);
                switch (funct3) {
                    case 0x0 -> {
                        switch (funct7) {
                            // addw
                            case 0x00 -> registers[rd] = (int)registers[rs1] + (int)registers[rs2];
                            // subw
                            case 0x20 -> registers[rd] = ((int)registers[rs1] - (int)registers[rs2]);
                        }
                    }
                    // sllw
                    case 0x1 -> registers[rd] = ((int)registers[rs1] << shamt);
                    case 0x5 -> {
                        switch (funct7) {
                            // srlw, sraw
                            case 0x00, 0x20 -> registers[rd] = ((int)registers[rs1] >> shamt);
                            default -> {
                                return Optional.of(Exception.IllegalInstruction);
                            }
                        }
                    }
                    default -> {
                        return Optional.of(Exception.IllegalInstruction);
                    }
                }
            }
            case 0x63 -> {
                long imm = (instruction & 0x80000000) >> 19 | (instruction & 0x80) << 4 | (instruction >> 20) & 0x7e0 | (instruction >> 7) & 0x1e;

                switch (funct3) {
                    // beq
                    case 0x0 -> {
                        if (registers[rs1] == registers[rs2]) {
                            pc = pc + imm - 4;
                        }
                    }
                    // bne
                    case 0x1 -> {
                        if (registers[rs1] != registers[rs2]) {
                            pc = pc + imm - 4;
                        }
                    }
                    // blt
                    case 0x4 -> {
                        if (registers[rs1] < registers[rs2]) {
                            pc = pc + imm - 4;
                        }
                    }
                    // bge
                    case 0x5 -> {
                        if (registers[rs1] >= registers[rs2]) {
                            pc = pc + imm - 4;
                        }
                    }
                    // bltu
                    case 0x6 -> {
                        if (Long.compareUnsigned(registers[rs1], registers[rs2]) < 0) {
                            pc = pc + imm - 4;
                        }
                    }
                    // bgeu
                    case 0x7 -> {
                        if (Long.compareUnsigned(registers[rs1], registers[rs2]) > 0 || Long.compareUnsigned(registers[rs1], registers[rs2]) == 0) {
                            pc = pc + imm - 4;
                        }
                    }
                    default -> {
                        return Optional.of(Exception.IllegalInstruction);
                    }
                }
            }
            // jalr
            case 0x67 -> {
                long t = pc;
                long imm = (instruction & 0xfff00000) >> 20;
                pc = (registers[rs1] + imm) & ~1;

                registers[rd] = t;
            }
            // jal
            case 0x6f -> {
                registers[rd] = pc;

                long imm = (instruction & 0x80000000) >> 11 | (instruction & 0xff000) | (instruction >> 9) & 0x800 | (instruction >> 20) & 0x7fe;
                pc = pc + imm - 4;
            }
            case 0x73 -> {
                long csr_addr = (instruction & 0xfff00000) >> 20;

                switch (funct3) {
                    // csrrw
                    case 0x1 -> {
                        long t = loadCsr(csr_addr);
                        storeCsr(csr_addr, registers[rs1]);
                        registers[rd] = t;
                    }
                    // csrrs
                    case 0x2 -> {
                        long t = loadCsr(csr_addr);
                        storeCsr(csr_addr, t | registers[rs1]);
                        registers[rd] = t;
                    }
                    // csrrc
                    case 0x3 -> {
                        long t = loadCsr(csr_addr);
                        storeCsr(csr_addr, t & (~registers[rs1]));
                        registers[rd] = t;
                    }
                    // csrrwi
                    case 0x5 -> {
                        registers[rd] = loadCsr(csr_addr);
                        storeCsr(csr_addr, rs1);
                    }
                    // csrrsi
                    case 0x6 -> {
                        long t = loadCsr(csr_addr);
                        storeCsr(csr_addr, t | (long) rs1);
                        registers[rd] = t;
                    }
                    // csrrci
                    case 0x7 -> {
                        long t = loadCsr(csr_addr);
                        storeCsr(csr_addr, t & (~(long) rs1));
                        registers[rd] = t;
                    }
                    default -> {
                        return Optional.of(Exception.IllegalInstruction);
                    }
                }
            }
            default -> {
                return Optional.of(Exception.IllegalInstruction);
            }
        }

        return Optional.empty();
    }

    public void boot(ArrayList<Integer> program) {
        halted = false;
        registers[2] = MEMORY_SIZE;
        start();
    }

    public void start() {
        int runned = 0;
        while (!halted) {
            registers[0] = 0;

            Pair<Integer, Optional<Exception>> fetched = fetch();

            int inst = fetched.getLeft();

            if (fetched.getRight().isPresent()) {
                fetched.getRight().get().takeTrap(this);
                if (fetched.getRight().get().isFatal()) {
                    halted = true;
                    break;
                }
            }

            if (inst == 0 || inst == -1) {
                halted = true;
                break;
            }

            pc += 4;

            Optional<Exception> exception = execute(inst);

            if (exception.isPresent()) {
                exception.get().takeTrap(this);
                if (exception.get().isFatal()) {
                    halted = true;
                    break;
                }
            }

            runned++;

            if (pc == 0) {
                halted = true;
                break;
            }
        }

        System.out.println("Finish");
        System.out.println("Instructions: " + runned);
        System.out.println(Arrays.toString(registers));
        this.pc = DRAM_BASE;
        this.registers = new long[32];
        this.csrs = new long[4096];
    }
}
