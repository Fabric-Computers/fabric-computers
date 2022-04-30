package com.lolzdev.fabriccomputers.cpu.riscv;

public enum Exception implements Trap{
    InstructionAddressMisaligned,
    InstructionAccessFault,
    IllegalInstruction,
    Breakpoint,
    LoadAddressMisaligned,
    LoadAccessFault,
    StoreAMOAddressMisaligned,
    StoreAMOAccessFault,
    EnvironmentCallFromUMode,
    EnvironmentCallFromSMode,
    EnvironmentCallFromMMode,
    InstructionPageFault,
    LoadPageFault,
    StoreAMOPageFault,
    ;

    public boolean isFatal() {
        switch (this) {
            case InstructionAddressMisaligned, InstructionAccessFault, LoadAccessFault, StoreAMOAccessFault, StoreAMOAddressMisaligned -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public int exceptionCode() {
        switch (this) {
            case InstructionAddressMisaligned -> {
                return 0;
            }
            case InstructionAccessFault -> {
                return 1;
            }
            case IllegalInstruction -> {
                return 2;
            }
            case Breakpoint -> {
                return 3;
            }
            case LoadAddressMisaligned -> {
                return 4;
            }
            case LoadAccessFault -> {
                return 5;
            }
            case StoreAMOAddressMisaligned -> {
                return 6;
            }
            case StoreAMOAccessFault -> {
                return 7;
            }
            case EnvironmentCallFromUMode -> {
                return 8;
            }
            case EnvironmentCallFromSMode -> {
                return 9;
            }
            case EnvironmentCallFromMMode -> {
                return 11;
            }
            case InstructionPageFault -> {
                return 12;
            }
            case LoadPageFault -> {
                return 13;
            }
            case StoreAMOPageFault -> {
                return 15;
            }
        }

        return -1;
    }
}
